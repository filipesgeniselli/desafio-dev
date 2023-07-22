package com.filipegeniselli.desafiodev.transactions.command;

import com.filipegeniselli.desafiodev.exception.NotFoundException;
import com.filipegeniselli.desafiodev.transactions.data.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CnabParserServiceImpl implements CnabParserService {

    private final CnabProcessStatusRepository cnabProcessStatusRepository;
    private final CnabRepository cnabRepository;
    private final StoreRepository storeRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CnabParserServiceImpl(CnabProcessStatusRepository cnabProcessStatusRepository,
                                 CnabRepository cnabRepository, StoreRepository storeRepository,
                                 TransactionRepository transactionRepository) {
        this.cnabProcessStatusRepository = cnabProcessStatusRepository;
        this.cnabRepository = cnabRepository;
        this.storeRepository = storeRepository;
        this.transactionRepository = transactionRepository;
    }

    @KafkaListener(topics = "${cnabparser.topics.cnabprocessing-topic}", groupId = "cnabparser-api")
    public void consume(ConsumerRecord<String, String> payload) {
        CnabProcessStatus status = this.cnabProcessStatusRepository.findById(Integer.valueOf(payload.value()))
                .orElseThrow(() -> new NotFoundException("Processamento não encontrado"));

        status.setStartProcessTime(LocalDateTime.now());
        status.setStatus(ProcesssStatus.PROCESSING);

        this.cnabProcessStatusRepository.save(status);
        processCnabList(this.cnabRepository.findByCnabProcessStatus_Id(status.getId()));

        status.setStatus(ProcesssStatus.SUCCESS);
        if(this.cnabRepository.findByCnabProcessStatus_Id(status.getId())
                .stream()
                .anyMatch(x -> StringUtils.hasText(x.getErrorDescription()))) {
            status.setStatus(ProcesssStatus.ERROR);
            status.setErrorReason("Erro durante o processamento do arquivo, verifique a(s) linha(s) com problema(s)");
        }

        status.setEndProcessTime(LocalDateTime.now());
        this.cnabProcessStatusRepository.save(status);
    }

    @Override
    public void processCnabList(List<Cnab> cnabList) {
        cnabList.parallelStream().forEach(line -> {
            Store store = getOrCreateStoreFromCnab(line);
            createTransactionFromCnab(line, store);
        });
    }

    private synchronized Store getOrCreateStoreFromCnab(Cnab cnab) {
        Optional<Store> result = this.storeRepository.findByOwnerNameAndStoreName(cnab.getOwner(), cnab.getStore());

        if (result.isPresent()) {
            return result.get();
        }

        Store store = Store.StoreBuilder.aStore()
                .storeName(cnab.getStore())
                .ownerName(cnab.getOwner())
                .build();

        store = this.storeRepository.save(store);
        return store;
    }

    private void createTransactionFromCnab(Cnab cnab, Store store) {
        boolean isError = false;

        Optional<TransactionType> type = TransactionType.get(cnab.getType());
        Optional<LocalDateTime> transactionTime = convertDateTime(cnab.getDate(), cnab.getTime());

        if (type.isEmpty()) {
            cnab.appendErrorDescription("Tipo inválido");
            isError = true;
        }

        if (transactionTime.isEmpty()) {
            cnab.appendErrorDescription("Data da transação inválida");
            isError = true;
        }

        Optional<BigDecimal> amount = convertAmount(cnab.getAmount(), type);
        if (amount.isEmpty()) {
            cnab.appendErrorDescription("Data da transação inválida");
            isError = true;
        }

        if (isError) {
            this.cnabRepository.save(cnab);
            return;
        }

        Transaction transaction = Transaction.TransactionBuilder.aTransaction()
                .type(type.get())
                .amount(amount.get())
                .transactionTime(transactionTime.get())
                .card(cnab.getCard())
                .cpf(cnab.getCpf())
                .store(store)
                .build();

        Optional<Transaction> existingTransaction = this.transactionRepository
                .findByStore_Id(store.getId())
                .stream()
                .filter(x -> x.equals(transaction))
                .findFirst();
        if(existingTransaction.isPresent()) {
            cnab.appendErrorDescription("Já existe uma transação com os mesmos dados para essa loja");
            this.cnabRepository.save(cnab);
            return;
        }

        this.transactionRepository.save(transaction);
    }

    private Optional<BigDecimal> convertAmount(String value, Optional<TransactionType> type) {
        BigDecimal amount = null;

        try {
            amount = new BigDecimal(value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (type.isPresent() &&
                List.of(TransactionType.BOLETO, TransactionType.FINANCIAMENTO, TransactionType.ALUGUEL).contains(type.get())) {
            amount = amount.multiply(BigDecimal.valueOf(-1));
        }

        return Optional.of(amount);
    }

    private Optional<LocalDateTime> convertDateTime(String date, String time) {
        try {
            return Optional.of(LocalDateTime.of(
                    Integer.valueOf(date.substring(0, 4)),
                    Integer.valueOf(date.substring(4, 6)),
                    Integer.valueOf(date.substring(6, 8)),
                    Integer.valueOf(time.substring(0, 2)),
                    Integer.valueOf(time.substring(2, 4)),
                    Integer.valueOf(time.substring(4, 6))));
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }
}
