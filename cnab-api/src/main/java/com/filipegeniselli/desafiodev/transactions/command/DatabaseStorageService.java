package com.filipegeniselli.desafiodev.transactions.command;

import com.filipegeniselli.desafiodev.exception.BadRequestException;
import com.filipegeniselli.desafiodev.exception.ConflictException;
import com.filipegeniselli.desafiodev.exception.StorageException;
import com.filipegeniselli.desafiodev.transactions.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseStorageService implements StorageService {

    private final CnabProcessStatusRepository cnabProcessStatusRepository;
    private final CnabRepository cnabRepository;

    @Autowired
    public DatabaseStorageService(CnabProcessStatusRepository cnabProcessStatusRepository, CnabRepository cnabRepository) {
        this.cnabProcessStatusRepository = cnabProcessStatusRepository;
        this.cnabRepository = cnabRepository;
    }

    @Override
    public Integer storeAndCreateProcessRecord(MultipartFile file) {
        Optional<CnabProcessStatus> existingProcess = cnabProcessStatusRepository.findByFileName(file.getOriginalFilename());
        if (existingProcess.isPresent()) {
            throw new ConflictException("Arquivo com esse nome já existe.");
        }

        CnabProcessStatus cnabProcessStatus = CnabProcessStatus.CnabProcessStatusBuilder.aCnabProcessStatus()
                .fileName(file.getOriginalFilename())
                .insertTime(LocalDateTime.now())
                .status(ProcesssStatus.WAITING)
                .build();
        final CnabProcessStatus cnabProcessStatusSaved = this.cnabProcessStatusRepository.save(cnabProcessStatus);

        if (file.isEmpty()) {
            throw new BadRequestException("Não é possível processar um arquivo vazio.");
        }

        List<Cnab> lines = null;
        try {
            lines = new BufferedReader(
                    new InputStreamReader(file.getInputStream()))
                    .lines()
                    .map(Cnab::fromFileLine)
                    .map(m -> {
                        m.setCnabProcessStatus(cnabProcessStatusSaved);
                        return m;
                    })
                    .toList();
        } catch (IOException e) {
            throw new StorageException("Falha ao ler o arquivo para processamento.", e);
        }

        this.cnabRepository.saveAll(lines);

        return cnabProcessStatusSaved.getId();
    }
}
