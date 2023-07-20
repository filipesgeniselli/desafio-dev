package com.filipegeniselli.desafiodev.transactions;

import java.math.BigDecimal;

public record StoreDto(Integer id, String name, String owner, BigDecimal balance) {
}
