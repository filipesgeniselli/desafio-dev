package com.filipegeniselli.desafiodev.transactions.query;

import com.filipegeniselli.desafiodev.exception.NotFoundException;
import com.filipegeniselli.desafiodev.transactions.CnabDto;
import com.filipegeniselli.desafiodev.transactions.CnabProcessingDto;
import com.filipegeniselli.desafiodev.transactions.StoreDto;
import com.filipegeniselli.desafiodev.transactions.TransactionDto;
import com.filipegeniselli.desafiodev.transactions.data.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionQueryHandler implements TransactionQueryService {

    private final CnabProcessStatusRepository cnabProcessStatusRepository;
    private final CnabRepository cnabRepository;
    private final TransactionRepository transactionRepository;
    private final StoreRepository storeRepository;

    public TransactionQueryHandler(CnabProcessStatusRepository cnabProcessStatusRepository,
                                   CnabRepository cnabRepository,
                                   TransactionRepository transactionRepository,
                                   StoreRepository storeRepository) {
        this.cnabProcessStatusRepository = cnabProcessStatusRepository;
        this.cnabRepository = cnabRepository;
        this.transactionRepository = transactionRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public List<CnabProcessingDto> handle(GetAllCnabProcessStatus query) {
        return cnabProcessStatusRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public CnabProcessingDto handle(GetCnabProcessStatusById query) {
        CnabProcessStatus status = cnabProcessStatusRepository
                .findById(query.id())
                .orElseThrow(() -> new NotFoundException("Nenhum prcessamento encontrado com o ID informado."));

        return convertEntityToDto(status);
    }

    @Override
    public List<CnabDto> handle(GetCnabByProcessId query) {
        return cnabRepository.findByCnabProcessStatus_Id(query.id())
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public List<StoreDto> handle(GetStores query) {
        return storeRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public List<TransactionDto> handle(GetTransactionsByStoreId query) {
        return transactionRepository.findByStore_Id(query.storeId())
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    private CnabProcessingDto convertEntityToDto(CnabProcessStatus entity) {
        return new CnabProcessingDto(entity.getId(),
                entity.getFileName(),
                entity.getStatus(),
                entity.getInsertTime(),
                entity.getStartProcessTime(),
                entity.getEndProcessTime(),
                entity.getErrorReason());
    }

    private CnabDto convertEntityToDto(Cnab entity) {
        return new CnabDto(entity.getType(),
                entity.getDate(),
                entity.getAmount(),
                entity.getCpf(),
                entity.getCard(),
                entity.getTime(),
                entity.getOwner(),
                entity.getStore(),
                entity.getErrorDescription());
    }

    private StoreDto convertEntityToDto(Store entity) {
        return new StoreDto(entity.getId(),
                entity.getStoreName(),
                entity.getOwnerName(),
                entity.getTransactions()
                        .stream()
                        .map(x -> x.getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private TransactionDto convertEntityToDto(Transaction entity) {
        return new TransactionDto(entity.getId(),
                entity.getType(),
                entity.getAmount(),
                entity.getTransactionTime(),
                entity.getCpf(),
                entity.getCard());
    }
}
