package com.filipegeniselli.desafiodev.transactions.command;

public interface TransactionCommandService {

    Integer handle(UploadCnabFileCommand command);

}
