package com.filipegeniselli.desafiodev.transactions.data;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    private TransactionType type;
    private LocalDateTime transactionTime;
    private BigDecimal amount;
    private String cpf;
    private String card;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(store, that.store) && type == that.type && Objects.equals(transactionTime, that.transactionTime) && Objects.equals(amount, that.amount) && Objects.equals(cpf, that.cpf) && Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, type, transactionTime, amount, cpf, card);
    }

    public static final class TransactionBuilder {
        private Integer id;
        private Store store;
        private TransactionType type;
        private LocalDateTime transactionTime;
        private BigDecimal amount;
        private String cpf;
        private String card;

        private TransactionBuilder() {
        }

        public static TransactionBuilder aTransaction() {
            return new TransactionBuilder();
        }

        public TransactionBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder store(Store store) {
            this.store = store;
            return this;
        }

        public TransactionBuilder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public TransactionBuilder transactionTime(LocalDateTime transactionTime) {
            this.transactionTime = transactionTime;
            return this;
        }

        public TransactionBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public TransactionBuilder card(String card) {
            this.card = card;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setStore(store);
            transaction.setType(type);
            transaction.setTransactionTime(transactionTime);
            transaction.setAmount(amount);
            transaction.setCpf(cpf);
            transaction.setCard(card);
            return transaction;
        }
    }
}
