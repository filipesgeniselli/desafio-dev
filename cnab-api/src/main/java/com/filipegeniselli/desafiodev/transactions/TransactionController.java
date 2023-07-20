package com.filipegeniselli.desafiodev.transactions;

import com.filipegeniselli.desafiodev.transactions.command.TransactionCommandService;
import com.filipegeniselli.desafiodev.transactions.command.UploadCnabFileCommand;
import com.filipegeniselli.desafiodev.transactions.data.StoreRepository;
import com.filipegeniselli.desafiodev.transactions.data.Transaction;
import com.filipegeniselli.desafiodev.transactions.data.TransactionRepository;
import com.filipegeniselli.desafiodev.transactions.query.GetCnabByProcessId;
import com.filipegeniselli.desafiodev.transactions.query.GetCnabProcessStatusById;
import com.filipegeniselli.desafiodev.transactions.query.TransactionQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionCommandService transactionCommandService;
    private final TransactionQueryService transactionQueryService;
    private final StoreRepository storeRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionCommandService transactionCommandService, TransactionQueryService transactionQueryService, StoreRepository storeRepository, TransactionRepository transactionRepository) {
        this.transactionCommandService = transactionCommandService;
        this.transactionQueryService = transactionQueryService;
        this.storeRepository = storeRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(MultipartFile file) {
        Integer result = this.transactionCommandService.handle(new UploadCnabFileCommand(file));
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(result)
                                .toUri())
                .build();
    }

    @GetMapping("/upload/{id}")
    public CnabProcessingDto getUploadStatus(@PathVariable("id") Integer id) {
        return this.transactionQueryService.handle(new GetCnabProcessStatusById((id)));
    }

    @GetMapping("/upload/{id}/lines")
    public List<CnabDto> getUploadLines(@PathVariable("id") Integer id) {
        return this.transactionQueryService.handle(new GetCnabByProcessId((id)));
    }

    @GetMapping("/stores")
    public List<StoreDto> getStores() {
        return this.storeRepository.findAll()
                .stream()
                .map(m -> new StoreDto(m.getId(),
                        m.getStoreName(),
                        m.getOwnerName(),
                        m.getTransactions()
                                .stream()
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .toList();
    }

    @GetMapping
    public List<TransactionDto> getTransactions() {
        return this.transactionRepository.findAll()
                .stream()
                .map(m -> new TransactionDto(m.getId(),
                        m.getType(),
                        m.getAmount(),
                        m.getTransactionTime(),
                        m.getCpf(),
                        m.getCard()))
                .toList();
    }

}
