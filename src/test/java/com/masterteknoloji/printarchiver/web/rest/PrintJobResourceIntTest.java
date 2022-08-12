package com.masterteknoloji.printarchiver.web.rest;

import com.masterteknoloji.printarchiver.PrintarchiverApp;

import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.repository.PrintJobRepository;
import com.masterteknoloji.printarchiver.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.masterteknoloji.printarchiver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.masterteknoloji.printarchiver.domain.enumeration.ProcessStatus;
import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;
/**
 * Test class for the PrintJobResource REST controller.
 *
 * @see PrintJobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintarchiverApp.class)
public class PrintJobResourceIntTest {

    private static final Long DEFAULT_PRINTER_JOB_LOG_ID = 1L;
    private static final Long UPDATED_PRINTER_JOB_LOG_ID = 2L;

    private static final LocalDate DEFAULT_PRINT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRINT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_PRINTER_ID = 1L;
    private static final Long UPDATED_PRINTER_ID = 2L;

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_MACHINE = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_MACHINE = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_ID = "AAAAAAAAAA";
    private static final String UPDATED_JOB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ARCHIVE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ARCHIVE_PATH = "BBBBBBBBBB";

    private static final ProcessStatus DEFAULT_PROCESS_STATUS = ProcessStatus.NOT_CHECKED;
    private static final ProcessStatus UPDATED_PROCESS_STATUS = ProcessStatus.CHECKED;

    private static final ResultStatus DEFAULT_RESULT_STATUS = ResultStatus.SAFETY;
    private static final ResultStatus UPDATED_RESULT_STATUS = ResultStatus.NOT_SAFETY;

    @Autowired
    private PrintJobRepository printJobRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrintJobMockMvc;

    private PrintJob printJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrintJobResource printJobResource = new PrintJobResource(printJobRepository);
        this.restPrintJobMockMvc = MockMvcBuilders.standaloneSetup(printJobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrintJob createEntity(EntityManager em) {
        PrintJob printJob = new PrintJob()
            .printerJobLogId(DEFAULT_PRINTER_JOB_LOG_ID)
            .printDate(DEFAULT_PRINT_DATE)
            .userId(DEFAULT_USER_ID)
            .printerId(DEFAULT_PRINTER_ID)
            .documentName(DEFAULT_DOCUMENT_NAME)
            .clientMachine(DEFAULT_CLIENT_MACHINE)
            .jobId(DEFAULT_JOB_ID)
            .archivePath(DEFAULT_ARCHIVE_PATH)
            .processStatus(DEFAULT_PROCESS_STATUS)
            .resultStatus(DEFAULT_RESULT_STATUS);
        return printJob;
    }

    @Before
    public void initTest() {
        printJob = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrintJob() throws Exception {
        int databaseSizeBeforeCreate = printJobRepository.findAll().size();

        // Create the PrintJob
        restPrintJobMockMvc.perform(post("/api/print-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printJob)))
            .andExpect(status().isCreated());

        // Validate the PrintJob in the database
        List<PrintJob> printJobList = printJobRepository.findAll();
        assertThat(printJobList).hasSize(databaseSizeBeforeCreate + 1);
        PrintJob testPrintJob = printJobList.get(printJobList.size() - 1);
        assertThat(testPrintJob.getPrinterJobLogId()).isEqualTo(DEFAULT_PRINTER_JOB_LOG_ID);
        assertThat(testPrintJob.getPrintDate()).isEqualTo(DEFAULT_PRINT_DATE);
        assertThat(testPrintJob.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPrintJob.getPrinterId()).isEqualTo(DEFAULT_PRINTER_ID);
        assertThat(testPrintJob.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testPrintJob.getClientMachine()).isEqualTo(DEFAULT_CLIENT_MACHINE);
        assertThat(testPrintJob.getJobId()).isEqualTo(DEFAULT_JOB_ID);
        assertThat(testPrintJob.getArchivePath()).isEqualTo(DEFAULT_ARCHIVE_PATH);
        assertThat(testPrintJob.getProcessStatus()).isEqualTo(DEFAULT_PROCESS_STATUS);
        assertThat(testPrintJob.getResultStatus()).isEqualTo(DEFAULT_RESULT_STATUS);
    }

    @Test
    @Transactional
    public void createPrintJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = printJobRepository.findAll().size();

        // Create the PrintJob with an existing ID
        printJob.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrintJobMockMvc.perform(post("/api/print-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printJob)))
            .andExpect(status().isBadRequest());

        // Validate the PrintJob in the database
        List<PrintJob> printJobList = printJobRepository.findAll();
        assertThat(printJobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPrintJobs() throws Exception {
        // Initialize the database
        printJobRepository.saveAndFlush(printJob);

        // Get all the printJobList
        restPrintJobMockMvc.perform(get("/api/print-jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(printJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].printerJobLogId").value(hasItem(DEFAULT_PRINTER_JOB_LOG_ID.intValue())))
            .andExpect(jsonPath("$.[*].printDate").value(hasItem(DEFAULT_PRINT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].printerId").value(hasItem(DEFAULT_PRINTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].clientMachine").value(hasItem(DEFAULT_CLIENT_MACHINE.toString())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID.toString())))
            .andExpect(jsonPath("$.[*].archivePath").value(hasItem(DEFAULT_ARCHIVE_PATH.toString())))
            .andExpect(jsonPath("$.[*].processStatus").value(hasItem(DEFAULT_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].resultStatus").value(hasItem(DEFAULT_RESULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getPrintJob() throws Exception {
        // Initialize the database
        printJobRepository.saveAndFlush(printJob);

        // Get the printJob
        restPrintJobMockMvc.perform(get("/api/print-jobs/{id}", printJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(printJob.getId().intValue()))
            .andExpect(jsonPath("$.printerJobLogId").value(DEFAULT_PRINTER_JOB_LOG_ID.intValue()))
            .andExpect(jsonPath("$.printDate").value(DEFAULT_PRINT_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.printerId").value(DEFAULT_PRINTER_ID.intValue()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME.toString()))
            .andExpect(jsonPath("$.clientMachine").value(DEFAULT_CLIENT_MACHINE.toString()))
            .andExpect(jsonPath("$.jobId").value(DEFAULT_JOB_ID.toString()))
            .andExpect(jsonPath("$.archivePath").value(DEFAULT_ARCHIVE_PATH.toString()))
            .andExpect(jsonPath("$.processStatus").value(DEFAULT_PROCESS_STATUS.toString()))
            .andExpect(jsonPath("$.resultStatus").value(DEFAULT_RESULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrintJob() throws Exception {
        // Get the printJob
        restPrintJobMockMvc.perform(get("/api/print-jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrintJob() throws Exception {
        // Initialize the database
        printJobRepository.saveAndFlush(printJob);
        int databaseSizeBeforeUpdate = printJobRepository.findAll().size();

        // Update the printJob
        PrintJob updatedPrintJob = printJobRepository.findOne(printJob.getId());
        // Disconnect from session so that the updates on updatedPrintJob are not directly saved in db
        em.detach(updatedPrintJob);
        updatedPrintJob
            .printerJobLogId(UPDATED_PRINTER_JOB_LOG_ID)
            .printDate(UPDATED_PRINT_DATE)
            .userId(UPDATED_USER_ID)
            .printerId(UPDATED_PRINTER_ID)
            .documentName(UPDATED_DOCUMENT_NAME)
            .clientMachine(UPDATED_CLIENT_MACHINE)
            .jobId(UPDATED_JOB_ID)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .processStatus(UPDATED_PROCESS_STATUS)
            .resultStatus(UPDATED_RESULT_STATUS);

        restPrintJobMockMvc.perform(put("/api/print-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrintJob)))
            .andExpect(status().isOk());

        // Validate the PrintJob in the database
        List<PrintJob> printJobList = printJobRepository.findAll();
        assertThat(printJobList).hasSize(databaseSizeBeforeUpdate);
        PrintJob testPrintJob = printJobList.get(printJobList.size() - 1);
        assertThat(testPrintJob.getPrinterJobLogId()).isEqualTo(UPDATED_PRINTER_JOB_LOG_ID);
        assertThat(testPrintJob.getPrintDate()).isEqualTo(UPDATED_PRINT_DATE);
        assertThat(testPrintJob.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPrintJob.getPrinterId()).isEqualTo(UPDATED_PRINTER_ID);
        assertThat(testPrintJob.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testPrintJob.getClientMachine()).isEqualTo(UPDATED_CLIENT_MACHINE);
        assertThat(testPrintJob.getJobId()).isEqualTo(UPDATED_JOB_ID);
        assertThat(testPrintJob.getArchivePath()).isEqualTo(UPDATED_ARCHIVE_PATH);
        assertThat(testPrintJob.getProcessStatus()).isEqualTo(UPDATED_PROCESS_STATUS);
        assertThat(testPrintJob.getResultStatus()).isEqualTo(UPDATED_RESULT_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingPrintJob() throws Exception {
        int databaseSizeBeforeUpdate = printJobRepository.findAll().size();

        // Create the PrintJob

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrintJobMockMvc.perform(put("/api/print-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printJob)))
            .andExpect(status().isCreated());

        // Validate the PrintJob in the database
        List<PrintJob> printJobList = printJobRepository.findAll();
        assertThat(printJobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrintJob() throws Exception {
        // Initialize the database
        printJobRepository.saveAndFlush(printJob);
        int databaseSizeBeforeDelete = printJobRepository.findAll().size();

        // Get the printJob
        restPrintJobMockMvc.perform(delete("/api/print-jobs/{id}", printJob.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PrintJob> printJobList = printJobRepository.findAll();
        assertThat(printJobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrintJob.class);
        PrintJob printJob1 = new PrintJob();
        printJob1.setId(1L);
        PrintJob printJob2 = new PrintJob();
        printJob2.setId(printJob1.getId());
        assertThat(printJob1).isEqualTo(printJob2);
        printJob2.setId(2L);
        assertThat(printJob1).isNotEqualTo(printJob2);
        printJob1.setId(null);
        assertThat(printJob1).isNotEqualTo(printJob2);
    }
}
