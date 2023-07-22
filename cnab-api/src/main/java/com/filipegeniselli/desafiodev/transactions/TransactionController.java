package com.filipegeniselli.desafiodev.transactions;

import com.filipegeniselli.desafiodev.transactions.command.TransactionCommandService;
import com.filipegeniselli.desafiodev.transactions.command.UploadCnabFileCommand;
import com.filipegeniselli.desafiodev.transactions.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionCommandService transactionCommandService;
    private final TransactionQueryService transactionQueryService;

    @Autowired
    public TransactionController(TransactionCommandService transactionCommandService, TransactionQueryService transactionQueryService) {
        this.transactionCommandService = transactionCommandService;
        this.transactionQueryService = transactionQueryService;
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

    @GetMapping("/upload")
    public List<CnabProcessingDto> getUploadStatus() {
        return this.transactionQueryService.handle(new GetAllCnabProcessStatus());
    }

    @GetMapping("/upload/{id}")
    public CnabProcessingDto getUploadStatus(@PathVariable("id") Integer id) {
        return this.transactionQueryService.handle(new GetCnabProcessStatusById(id));
    }

    @GetMapping("/upload/{id}/lines")
    public List<CnabDto> getUploadLines(@PathVariable("id") Integer id) {
        return this.transactionQueryService.handle(new GetCnabByProcessId(id));
    }

    @GetMapping("/stores")
    public List<StoreDto> getStores() {
        return this.transactionQueryService.handle(new GetStores());
    }

    @GetMapping("/stores/{id}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable("id") Integer id) {
        return this.transactionQueryService.handle(new GetTransactionsByStoreId(id));
    }
}
