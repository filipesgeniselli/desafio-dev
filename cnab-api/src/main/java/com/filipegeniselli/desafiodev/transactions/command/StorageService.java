package com.filipegeniselli.desafiodev.transactions.command;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    Integer storeAndCreateProcessRecord(MultipartFile file);
}
