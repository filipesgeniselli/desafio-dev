package com.filipegeniselli.desafiodev.transactions;

public record CnabDto(String type,
                      String date,
                      String amount,
                      String cpf,
                      String card,
                      String time,
                      String owner,
                      String store,
                      String errorDescription) {
}
