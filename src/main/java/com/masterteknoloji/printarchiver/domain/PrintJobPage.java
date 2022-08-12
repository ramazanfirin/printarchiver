package com.masterteknoloji.printarchiver.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;

/**
 * A PrintJobPage.
 */
@Entity
@Table(name = "print_job_page")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrintJobPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_name")
    private String pageName;

    @Column(name = "page_path")
    private String pagePath;

    @Column(name = "jhi_index")
    private Long index;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status")
    private ResultStatus resultStatus;

    @Column(name = "restricted_keywords")
    private String restrictedKeywords;

    @Column(name = "processed")
    private Boolean processed;

    @Transient
    private String content;
    
    @ManyToOne
    private PrintJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public PrintJobPage pageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPagePath() {
        return pagePath;
    }

    public PrintJobPage pagePath(String pagePath) {
        this.pagePath = pagePath;
        return this;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public Long getIndex() {
        return index;
    }

    public PrintJobPage index(Long index) {
        this.index = index;
        return this;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public PrintJobPage resultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
        return this;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getRestrictedKeywords() {
        return restrictedKeywords;
    }

    public PrintJobPage restrictedKeywords(String restrictedKeywords) {
        this.restrictedKeywords = restrictedKeywords;
        return this;
    }

    public void setRestrictedKeywords(String restrictedKeywords) {
        this.restrictedKeywords = restrictedKeywords;
    }

    public Boolean isProcessed() {
        return processed;
    }

    public PrintJobPage processed(Boolean processed) {
        this.processed = processed;
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public PrintJob getJob() {
        return job;
    }

    public PrintJobPage job(PrintJob printJob) {
        this.job = printJob;
        return this;
    }

    public void setJob(PrintJob printJob) {
        this.job = printJob;
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
        PrintJobPage printJobPage = (PrintJobPage) o;
        if (printJobPage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), printJobPage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrintJobPage{" +
            "id=" + getId() +
            ", pageName='" + getPageName() + "'" +
            ", pagePath='" + getPagePath() + "'" +
            ", index=" + getIndex() +
            ", resultStatus='" + getResultStatus() + "'" +
            ", restrictedKeywords='" + getRestrictedKeywords() + "'" +
            ", processed='" + isProcessed() + "'" +
            "}";
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getProcessed() {
		return processed;
	}
}
