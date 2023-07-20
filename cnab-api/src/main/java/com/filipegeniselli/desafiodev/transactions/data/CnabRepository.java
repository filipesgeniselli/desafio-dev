package com.filipegeniselli.desafiodev.transactions.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CnabRepository extends JpaRepository<Cnab, Integer> {
    List<Cnab> findByCnabProcessStatus_Id(@NonNull Integer id);
}
