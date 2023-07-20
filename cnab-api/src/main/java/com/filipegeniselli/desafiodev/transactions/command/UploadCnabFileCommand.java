package com.filipegeniselli.desafiodev.transactions.command;

import org.springframework.web.multipart.MultipartFile;

public record UploadCnabFileCommand(MultipartFile file) {
}
