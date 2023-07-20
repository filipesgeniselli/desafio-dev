package com.filipegeniselli.desafiodev.transactions.query;

import com.filipegeniselli.desafiodev.transactions.CnabDto;
import com.filipegeniselli.desafiodev.transactions.CnabProcessingDto;

import java.util.List;

public interface TransactionQueryService {

    CnabProcessingDto handle(GetCnabProcessStatusById query);

    List<CnabDto> handle(GetCnabByProcessId query);

}
