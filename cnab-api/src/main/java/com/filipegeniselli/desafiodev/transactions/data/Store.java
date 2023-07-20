package com.filipegeniselli.desafiodev.transactions.data;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ownerName;
    private String storeName;

    @OneToMany(mappedBy = "store")
    private Set<Transaction> transactions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(ownerName, store.ownerName) && Objects.equals(storeName, store.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerName, storeName);
    }

    public static final class StoreBuilder {
        private Integer id;
        private String ownerName;
        private String storeName;
        private Set<Transaction> transactions;

        private StoreBuilder() {
        }

        public static StoreBuilder aStore() {
            return new StoreBuilder();
        }

        public StoreBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public StoreBuilder ownerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public StoreBuilder storeName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public StoreBuilder transactions(Set<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Store build() {
            Store store = new Store();
            store.setId(id);
            store.setOwnerName(ownerName);
            store.setStoreName(storeName);
            store.setTransactions(transactions);
            return store;
        }
    }
}
