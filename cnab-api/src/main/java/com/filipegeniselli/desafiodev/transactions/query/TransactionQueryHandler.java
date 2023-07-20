package com.filipegeniselli.desafiodev.transactions.query;

import com.filipegeniselli.desafiodev.exception.NotFoundException;
import com.filipegeniselli.desafiodev.transactions.CnabDto;
import com.filipegeniselli.desafiodev.transactions.CnabProcessingDto;
import com.filipegeniselli.desafiodev.transactions.data.Cnab;
import com.filipegeniselli.desafiodev.transactions.data.CnabProcessStatus;
import com.filipegeniselli.desafiodev.transactions.data.CnabProcessStatusRepository;
import com.filipegeniselli.desafiodev.transactions.data.CnabRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionQueryHandler implements TransactionQueryService {

    private final CnabProcessStatusRepository cnabProcessStatusRepository;
    private final CnabRepository cnabRepository;

    public TransactionQueryHandler(CnabProcessStatusRepository cnabProcessStatusRepository,
                                   CnabRepository cnabRepository) {
        this.cnabProcessStatusRepository = cnabProcessStatusRepository;
        this.cnabRepository = cnabRepository;
    }

    @Override
    public CnabProcessingDto handle(GetCnabProcessStatusById query) {
        CnabProcessStatus status = cnabProcessStatusRepository
                .findById(query.id())
                .orElseThrow(() -> new NotFoundException("Nenhum prcessamento encontrado com o ID informado."));

        return convertEntityToDto(status);
    }

    @Override
    public List<CnabDto> handle(GetCnabByProcessId query) {
        return cnabRepository.findByCnabProcessStatus_Id(query.id())
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    private CnabProcessingDto convertEntityToDto(CnabProcessStatus entity) {
        return new CnabProcessingDto(entity.getId(),
                entity.getFileName(),
                entity.getStatus(),
                entity.getInsertTime(),
                entity.getStartProcessTime(),
                entity.getEndProcessTime(),
                entity.getErrorReason());
    }

    private CnabDto convertEntityToDto(Cnab entity) {
        return new CnabDto(entity.getType(),
                entity.getDate(),
                entity.getAmount(),
                entity.getCpf(),
                entity.getCard(),
                entity.getTime(),
                entity.getOwner(),
                entity.getStore(),
                entity.getErrorDescription());
    }

}
