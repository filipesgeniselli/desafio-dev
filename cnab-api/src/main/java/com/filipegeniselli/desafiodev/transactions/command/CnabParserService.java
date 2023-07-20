package com.filipegeniselli.desafiodev.transactions.command;

import com.filipegeniselli.desafiodev.transactions.data.Cnab;

import java.util.List;

public interface CnabParserService {

    void processCnabList(List<Cnab> cnabList);

}
