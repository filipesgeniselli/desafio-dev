package com.filipegeniselli.desafiodev.transactions.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CnabProcessStatusRepository extends JpaRepository<CnabProcessStatus, Integer> {
    Optional<CnabProcessStatus> findByFileName(String fileName);
}
