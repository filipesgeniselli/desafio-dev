package com.filipegeniselli.desafiodev.transactions.data;

import com.filipegeniselli.desafiodev.exception.InvalidCnabException;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table
public class Cnab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cnab_proccess_id")
    private CnabProcessStatus cnabProcessStatus;
    private String type;
    private String date;
    private String amount;
    private String cpf;
    private String card;
    private String time;
    private String owner;
    private String store;
    private String errorDescription;

    protected Cnab() {
    }

    public Cnab(String type, String date, String amount, String cpf, String card, String time, String owner, String store) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.cpf = cpf;
        this.card = card;
        this.time = time;
        this.owner = owner;
        this.store = store;
    }

    public static Cnab fromFileLine(String fileLine) throws InvalidCnabException {
        if (fileLine.length() < 80 || fileLine.length() > 80) {
            throw new InvalidCnabException("Linha não está no formato correto");
        }

        return new Cnab(fileLine.substring(0, 1),
                fileLine.substring(1, 9),
                fileLine.substring(9, 19),
                fileLine.substring(19, 30),
                fileLine.substring(30, 42),
                fileLine.substring(42, 48),
                fileLine.substring(48, 62).trim(),
                fileLine.substring(62).trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CnabProcessStatus getCnabProcessStatus() {
        return cnabProcessStatus;
    }

    public void setCnabProcessStatus(CnabProcessStatus cnabProcessStatus) {
        this.cnabProcessStatus = cnabProcessStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cnab cnab = (Cnab) o;
        return Objects.equals(type, cnab.type) &&
                Objects.equals(date, cnab.date) &&
                Objects.equals(amount, cnab.amount) &&
                Objects.equals(cpf, cnab.cpf) &&
                Objects.equals(card, cnab.card) &&
                Objects.equals(time, cnab.time) &&
                Objects.equals(owner, cnab.owner) &&
                Objects.equals(store, cnab.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, date, amount, cpf, card, time, owner, store);
    }
}