package com.filipegeniselli.desafiodev.transactions;

import com.filipegeniselli.desafiodev.transactions.data.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(Integer id, TransactionType type, BigDecimal amount, LocalDateTime transactionTime, String cpf, String card) {
}
