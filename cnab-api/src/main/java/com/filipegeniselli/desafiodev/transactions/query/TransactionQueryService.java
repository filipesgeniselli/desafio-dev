package com.filipegeniselli.desafiodev.transactions.query;

import com.filipegeniselli.desafiodev.transactions.CnabDto;
import com.filipegeniselli.desafiodev.transactions.CnabProcessingDto;
import com.filipegeniselli.desafiodev.transactions.StoreDto;
import com.filipegeniselli.desafiodev.transactions.TransactionDto;

import java.util.List;

public interface TransactionQueryService {

    List<CnabProcessingDto> handle(GetAllCnabProcessStatus query);

    CnabProcessingDto handle(GetCnabProcessStatusById query);

    List<CnabDto> handle(GetCnabByProcessId query);

    List<StoreDto> handle(GetStores query);

    List<TransactionDto> handle(GetTransactionsByStoreId query);

}
