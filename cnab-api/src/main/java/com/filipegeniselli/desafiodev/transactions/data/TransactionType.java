package com.filipegeniselli.desafiodev.transactions.data;

import java.util.Arrays;
import java.util.Optional;

public enum TransactionType {
    DEBITO("1"),
    BOLETO("2"),
    FINANCIAMENTO("3"),
    CREDITO("4"),
    RECEBIMENTO("5"),
    VENDAS("6"),
    RECEBIMENTO_TED("7"),
    RECEBIMENTO_DOC("8"),
    ALUGUEL("9");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getType() {
        return value;
    }

    public static Optional<TransactionType> get(String value) {
        return Arrays.stream(TransactionType.values())
                .filter(type -> type.value.equals(value))
                .findFirst();
    }
}
