package com.filipegeniselli.desafiodev.transactions.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class CnabProcessStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fileName;
    private LocalDateTime insertTime;
    private LocalDateTime startProcessTime;
    private LocalDateTime endProcessTime;
    private ProcesssStatus status;
    private String errorReason;
    @OneToMany(mappedBy = "cnabProcessStatus")
    private List<Cnab> cnabList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(LocalDateTime insertTime) {
        this.insertTime = insertTime;
    }

    public LocalDateTime getStartProcessTime() {
        return startProcessTime;
    }

    public void setStartProcessTime(LocalDateTime startProcessTime) {
        this.startProcessTime = startProcessTime;
    }

    public LocalDateTime getEndProcessTime() {
        return endProcessTime;
    }

    public void setEndProcessTime(LocalDateTime endProcessTime) {
        this.endProcessTime = endProcessTime;
    }

    public ProcesssStatus getStatus() {
        return status;
    }

    public void setStatus(ProcesssStatus status) {
        this.status = status;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public List<Cnab> getCnabList() {
        return cnabList;
    }

    public void setCnabList(List<Cnab> cnabList) {
        this.cnabList = cnabList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CnabProcessStatus that = (CnabProcessStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName);
    }


    public static final class CnabProcessStatusBuilder {
        private Integer id;
        private String fileName;
        private LocalDateTime insertTime;
        private LocalDateTime startProcessTime;
        private LocalDateTime endProcessTime;
        private ProcesssStatus status;
        private String errorReason;

        private CnabProcessStatusBuilder() {
        }

        public static CnabProcessStatusBuilder aCnabProcessStatus() {
            return new CnabProcessStatusBuilder();
        }

        public CnabProcessStatusBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public CnabProcessStatusBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public CnabProcessStatusBuilder insertTime(LocalDateTime insertTime) {
            this.insertTime = insertTime;
            return this;
        }

        public CnabProcessStatusBuilder startProcessTime(LocalDateTime startProcessTime) {
            this.startProcessTime = startProcessTime;
            return this;
        }

        public CnabProcessStatusBuilder endProcessTime(LocalDateTime endProcessTime) {
            this.endProcessTime = endProcessTime;
            return this;
        }

        public CnabProcessStatusBuilder status(ProcesssStatus status) {
            this.status = status;
            return this;
        }

        public CnabProcessStatusBuilder errorReason(String errorReason) {
            this.errorReason = errorReason;
            return this;
        }

        public CnabProcessStatus build() {
            CnabProcessStatus cnabProcessStatus = new CnabProcessStatus();
            cnabProcessStatus.setId(id);
            cnabProcessStatus.setFileName(fileName);
            cnabProcessStatus.setInsertTime(insertTime);
            cnabProcessStatus.setStartProcessTime(startProcessTime);
            cnabProcessStatus.setEndProcessTime(endProcessTime);
            cnabProcessStatus.setStatus(status);
            cnabProcessStatus.setErrorReason(errorReason);
            return cnabProcessStatus;
        }
    }
}
