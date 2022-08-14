package com.masterteknoloji.printarchiver.web.rest;

import static com.masterteknoloji.printarchiver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

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

import com.masterteknoloji.printarchiver.PrintarchiverApp;
import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.domain.PrintJobPage;
import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;
import com.masterteknoloji.printarchiver.repository.PrintJobPageRepository;
import com.masterteknoloji.printarchiver.repository.PrintJobRepository;
import com.masterteknoloji.printarchiver.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the PrintJobPageResource REST controller.
 *
 * @see PrintJobPageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintarchiverApp.class)
public class PrintJobPageResourceIntTest {

    private static final String DEFAULT_PAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_INDEX = 1L;
    private static final Long UPDATED_INDEX = 2L;

    private static final ResultStatus DEFAULT_RESULT_STATUS = ResultStatus.SAFETY;
    private static final ResultStatus UPDATED_RESULT_STATUS = ResultStatus.NOT_SAFETY;

    private static final String DEFAULT_RESTRICTED_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_RESTRICTED_KEYWORDS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PROCESSED = false;
    private static final Boolean UPDATED_PROCESSED = true;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXPORT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_EXPORT_PATH = "BBBBBBBBBB";

    @Autowired
    private PrintJobPageRepository printJobPageRepository;
    
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

    private MockMvc restPrintJobPageMockMvc;

    private PrintJobPage printJobPage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrintJobPageResource printJobPageResource = new PrintJobPageResource(printJobPageRepository);
        this.restPrintJobPageMockMvc = MockMvcBuilders.standaloneSetup(printJobPageResource)
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
    public static PrintJobPage createEntity(EntityManager em) {
        PrintJobPage printJobPage = new PrintJobPage()
            .pageName(DEFAULT_PAGE_NAME)
            .pagePath(DEFAULT_PAGE_PATH)
            .index(DEFAULT_INDEX)
            .resultStatus(DEFAULT_RESULT_STATUS)
            .restrictedKeywords(DEFAULT_RESTRICTED_KEYWORDS)
            .processed(DEFAULT_PROCESSED)
            .fileName(DEFAULT_FILE_NAME)
            .exportPath(DEFAULT_EXPORT_PATH);
        return printJobPage;
    }

    @Before
    public void initTest() {
        printJobPage = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrintJobPage() throws Exception {
        int databaseSizeBeforeCreate = printJobPageRepository.findAll().size();

        // Create the PrintJobPage
        restPrintJobPageMockMvc.perform(post("/api/print-job-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printJobPage)))
            .andExpect(status().isCreated());

        // Validate the PrintJobPage in the database
        List<PrintJobPage> printJobPageList = printJobPageRepository.findAll();
        assertThat(printJobPageList).hasSize(databaseSizeBeforeCreate + 1);
        PrintJobPage testPrintJobPage = printJobPageList.get(printJobPageList.size() - 1);
        assertThat(testPrintJobPage.getPageName()).isEqualTo(DEFAULT_PAGE_NAME);
        assertThat(testPrintJobPage.getPagePath()).isEqualTo(DEFAULT_PAGE_PATH);
        assertThat(testPrintJobPage.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testPrintJobPage.getResultStatus()).isEqualTo(DEFAULT_RESULT_STATUS);
        assertThat(testPrintJobPage.getRestrictedKeywords()).isEqualTo(DEFAULT_RESTRICTED_KEYWORDS);
        assertThat(testPrintJobPage.isProcessed()).isEqualTo(DEFAULT_PROCESSED);
        assertThat(testPrintJobPage.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testPrintJobPage.getExportPath()).isEqualTo(DEFAULT_EXPORT_PATH);
    }

    @Test
    @Transactional
    public void createPrintJobPageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = printJobPageRepository.findAll().size();

        // Create the PrintJobPage with an existing ID
        printJobPage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrintJobPageMockMvc.perform(post("/api/print-job-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printJobPage)))
            .andExpect(status().isBadRequest());

        // Validate the PrintJobPage in the database
        List<PrintJobPage> printJobPageList = printJobPageRepository.findAll();
        assertThat(printJobPageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPrintJobPages() throws Exception {
        // Initialize the database
        printJobPageRepository.saveAndFlush(printJobPage);

        // Get all the printJobPageList
        restPrintJobPageMockMvc.perform(get("/api/print-job-pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(printJobPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageName").value(hasItem(DEFAULT_PAGE_NAME.toString())))
            .andExpect(jsonPath("$.[*].pagePath").value(hasItem(DEFAULT_PAGE_PATH.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].resultStatus").value(hasItem(DEFAULT_RESULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].restrictedKeywords").value(hasItem(DEFAULT_RESTRICTED_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].exportPath").value(hasItem(DEFAULT_EXPORT_PATH.toString())));
    }

    @Test
    @Transactional
    public void getByJobId() throws Exception {
        // Initialize the database
    	PrintJob printJob = new PrintJob();
    	printJobRepository.save(printJob);
    	printJobPage.setJob(printJob);
        printJobPageRepository.saveAndFlush(printJobPage);

        // Get all the printJobPageList
        restPrintJobPageMockMvc.perform(get("/api/print-job-pages/getByJobId/"+printJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(printJobPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageName").value(hasItem(DEFAULT_PAGE_NAME.toString())))
            .andExpect(jsonPath("$.[*].pagePath").value(hasItem(DEFAULT_PAGE_PATH.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].resultStatus").value(hasItem(DEFAULT_RESULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].restrictedKeywords").value(hasItem(DEFAULT_RESTRICTED_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].exportPath").value(hasItem(DEFAULT_EXPORT_PATH.toString())));
    }
    
    @Test
    @Transactional
    public void getPrintJobPage() throws Exception {
        // Initialize the database
        printJobPageRepository.saveAndFlush(printJobPage);

        // Get the printJobPage
        restPrintJobPageMockMvc.perform(get("/api/print-job-pages/{id}", printJobPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(printJobPage.getId().intValue()))
            .andExpect(jsonPath("$.pageName").value(DEFAULT_PAGE_NAME.toString()))
            .andExpect(jsonPath("$.pagePath").value(DEFAULT_PAGE_PATH.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.intValue()))
            .andExpect(jsonPath("$.resultStatus").value(DEFAULT_RESULT_STATUS.toString()))
            .andExpect(jsonPath("$.restrictedKeywords").value(DEFAULT_RESTRICTED_KEYWORDS.toString()))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.exportPath").value(DEFAULT_EXPORT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrintJobPage() throws Exception {
        // Get the printJobPage
        restPrintJobPageMockMvc.perform(get("/api/print-job-pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrintJobPage() throws Exception {
        // Initialize the database
        printJobPageRepository.saveAndFlush(printJobPage);
        int databaseSizeBeforeUpdate = printJobPageRepository.findAll().size();

        // Update the printJobPage
        PrintJobPage updatedPrintJobPage = printJobPageRepository.findOne(printJobPage.getId());
        // Disconnect from session so that the updates on updatedPrintJobPage are not directly saved in db
        em.detach(updatedPrintJobPage);
        updatedPrintJobPage
            .pageName(UPDATED_PAGE_NAME)
            .pagePath(UPDATED_PAGE_PATH)
            .index(UPDATED_INDEX)
            .resultStatus(UPDATED_RESULT_STATUS)
            .restrictedKeywords(UPDATED_RESTRICTED_KEYWORDS)
            .processed(UPDATED_PROCESSED)
            .fileName(UPDATED_FILE_NAME)
            .exportPath(UPDATED_EXPORT_PATH);

        restPrintJobPageMockMvc.perform(put("/api/print-job-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrintJobPage)))
            .andExpect(status().isOk());

        // Validate the PrintJobPage in the database
        List<PrintJobPage> printJobPageList = printJobPageRepository.findAll();
        assertThat(printJobPageList).hasSize(databaseSizeBeforeUpdate);
        PrintJobPage testPrintJobPage = printJobPageList.get(printJobPageList.size() - 1);
        assertThat(testPrintJobPage.getPageName()).isEqualTo(UPDATED_PAGE_NAME);
        assertThat(testPrintJobPage.getPagePath()).isEqualTo(UPDATED_PAGE_PATH);
        assertThat(testPrintJobPage.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testPrintJobPage.getResultStatus()).isEqualTo(UPDATED_RESULT_STATUS);
        assertThat(testPrintJobPage.getRestrictedKeywords()).isEqualTo(UPDATED_RESTRICTED_KEYWORDS);
        assertThat(testPrintJobPage.isProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testPrintJobPage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testPrintJobPage.getExportPath()).isEqualTo(UPDATED_EXPORT_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingPrintJobPage() throws Exception {
        int databaseSizeBeforeUpdate = printJobPageRepository.findAll().size();

        // Create the PrintJobPage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrintJobPageMockMvc.perform(put("/api/print-job-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printJobPage)))
            .andExpect(status().isCreated());

        // Validate the PrintJobPage in the database
        List<PrintJobPage> printJobPageList = printJobPageRepository.findAll();
        assertThat(printJobPageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrintJobPage() throws Exception {
        // Initialize the database
        printJobPageRepository.saveAndFlush(printJobPage);
        int databaseSizeBeforeDelete = printJobPageRepository.findAll().size();

        // Get the printJobPage
        restPrintJobPageMockMvc.perform(delete("/api/print-job-pages/{id}", printJobPage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PrintJobPage> printJobPageList = printJobPageRepository.findAll();
        assertThat(printJobPageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrintJobPage.class);
        PrintJobPage printJobPage1 = new PrintJobPage();
        printJobPage1.setId(1L);
        PrintJobPage printJobPage2 = new PrintJobPage();
        printJobPage2.setId(printJobPage1.getId());
        assertThat(printJobPage1).isEqualTo(printJobPage2);
        printJobPage2.setId(2L);
        assertThat(printJobPage1).isNotEqualTo(printJobPage2);
        printJobPage1.setId(null);
        assertThat(printJobPage1).isNotEqualTo(printJobPage2);
    }
}
