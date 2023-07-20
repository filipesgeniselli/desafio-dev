package com.filipegeniselli.desafiodev.transactions;

import com.filipegeniselli.desafiodev.transactions.data.ProcesssStatus;

import java.time.LocalDateTime;

public record CnabProcessingDto(Integer id,
                                String fileName,
                                ProcesssStatus status,
                                LocalDateTime insertTime,

                                LocalDateTime startProcessTime,

                                LocalDateTime endProcessTime,

                                String errorReason) {
}
