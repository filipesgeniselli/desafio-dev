package com.filipegeniselli.desafiodev.transactions.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    Optional<Store> findByOwnerNameAndStoreName(@NonNull String ownerName, @NonNull String storeName);
}
