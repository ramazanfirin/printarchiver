package com.masterteknoloji.printarchiver.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.masterteknoloji.printarchiver.domain.enumeration.ProcessStatus;

import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;

/**
 * A PrintJob.
 */
@Entity
@Table(name = "print_job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrintJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "printer_job_log_id")
    private Long printerJobLogId;

    @Column(name = "print_date")
    private LocalDate printDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "printer_id")
    private Long printerId;

    @Column(name = "printer_name")
    private String printerName;

    @Column(name = "page_count")
    private Long pageCount;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "client_machine")
    private String clientMachine;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "archive_path")
    private String archivePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status")
    private ProcessStatus processStatus = ProcessStatus.NOT_CHECKED;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status")
    private ResultStatus resultStatus = ResultStatus.SAFETY;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrinterJobLogId() {
        return printerJobLogId;
    }

    public PrintJob printerJobLogId(Long printerJobLogId) {
        this.printerJobLogId = printerJobLogId;
        return this;
    }

    public void setPrinterJobLogId(Long printerJobLogId) {
        this.printerJobLogId = printerJobLogId;
    }

    public LocalDate getPrintDate() {
        return printDate;
    }

    public PrintJob printDate(LocalDate printDate) {
        this.printDate = printDate;
        return this;
    }

    public void setPrintDate(LocalDate printDate) {
        this.printDate = printDate;
    }

    public Long getUserId() {
        return userId;
    }

    public PrintJob userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public PrintJob userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPrinterId() {
        return printerId;
    }

    public PrintJob printerId(Long printerId) {
        this.printerId = printerId;
        return this;
    }

    public void setPrinterId(Long printerId) {
        this.printerId = printerId;
    }

    public String getPrinterName() {
        return printerName;
    }

    public PrintJob printerName(String printerName) {
        this.printerName = printerName;
        return this;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public PrintJob pageCount(Long pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public String getDocumentName() {
        return documentName;
    }

    public PrintJob documentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getClientMachine() {
        return clientMachine;
    }

    public PrintJob clientMachine(String clientMachine) {
        this.clientMachine = clientMachine;
        return this;
    }

    public void setClientMachine(String clientMachine) {
        this.clientMachine = clientMachine;
    }

    public String getJobId() {
        return jobId;
    }

    public PrintJob jobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getArchivePath() {
        return archivePath;
    }

    public PrintJob archivePath(String archivePath) {
        this.archivePath = archivePath;
        return this;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public PrintJob processStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
        return this;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public PrintJob resultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
        return this;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrintJob printJob = (PrintJob) o;
        if (printJob.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), printJob.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrintJob{" +
            "id=" + getId() +
            ", printerJobLogId=" + getPrinterJobLogId() +
            ", printDate='" + getPrintDate() + "'" +
            ", userId=" + getUserId() +
            ", userName='" + getUserName() + "'" +
            ", printerId=" + getPrinterId() +
            ", printerName='" + getPrinterName() + "'" +
            ", pageCount=" + getPageCount() +
            ", documentName='" + getDocumentName() + "'" +
            ", clientMachine='" + getClientMachine() + "'" +
            ", jobId='" + getJobId() + "'" +
            ", archivePath='" + getArchivePath() + "'" +
            ", processStatus='" + getProcessStatus() + "'" +
            ", resultStatus='" + getResultStatus() + "'" +
            "}";
    }
}
