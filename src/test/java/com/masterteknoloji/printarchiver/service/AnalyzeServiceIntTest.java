package com.masterteknoloji.printarchiver.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.masterteknoloji.printarchiver.PrintarchiverApp;
import com.masterteknoloji.printarchiver.config.ApplicationProperties;
import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.domain.PrintJobPage;
import com.masterteknoloji.printarchiver.domain.RestrictedKeyword;
import com.masterteknoloji.printarchiver.domain.User;
import com.masterteknoloji.printarchiver.domain.enumeration.ProcessStatus;
import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;
import com.masterteknoloji.printarchiver.repository.PrintJobPageRepository;
import com.masterteknoloji.printarchiver.repository.PrintJobRepository;
import com.masterteknoloji.printarchiver.repository.RestrictedKeywordRepository;



/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintarchiverApp.class)
public class AnalyzeServiceIntTest {

	
    private AnalyzeService analyzeService;

    @Autowired
    private PrintJobRepository printJobRepository;
    
    @Autowired
    private PrintJobPageRepository printJobPageRepository;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private OcrService ocrService;

    @Autowired
    private RestrictedKeywordRepository restrictedKeywordRepository;
    
   
    @Mock
    private PrintJobService printJobService;

    private User user;

    List jobList; 
    
    
    final String ARCHIVE_PATH = "2022/08/12/09";
    final String CLIENT_MACHINE = "DESKTOP-VU0HKAQ";
    final String DOCUMENT_NAME = "documentName";
    final String JOB_UUID = "897b2602636453f5cff4977a746dc587cee94af7";
    final String INSERT_DATE = "2022-08-07 15:03:36.0";
    final BigInteger PRINTER_ID = new BigInteger("7");
    final BigInteger PRINTER_JOB_LOG_ID = new BigInteger("0");
    final BigInteger User_ID = new BigInteger("10");
    
    PrintJob printJob;
    
    PrintJob printJob2Page;
    
    String path = "src/test/resources/ocrtestdata/standart";
	String pathExport = "src/test/resources/ocrtestdata/output";
	File file = new File(path);
	File fileExport = new File(pathExport);

    
    private void prepareJobDummyData() {
    	printJob = new PrintJob();
    	
    	applicationProperties.setArchiveRepo(file.getAbsolutePath());
    	applicationProperties.setExportDirectory(fileExport.getAbsolutePath());
    	
    	printJob.setArchivePath(ARCHIVE_PATH);
    	printJob.setClientMachine(CLIENT_MACHINE);
    	printJob.setDocumentName(DOCUMENT_NAME);
    	printJob.setJobId(JOB_UUID);
    	printJob.setPrinterId(PRINTER_ID.longValue());
    	printJob.setPrinterJobLogId(PRINTER_JOB_LOG_ID.longValue());
    	printJob.setProcessStatus(ProcessStatus.NOT_CHECKED);
    	printJob.setUserId(User_ID.longValue());
    	printJob.setPrintDate(LocalDate.now());
    	
    }
    
    private void prepare2Pagea() {
    	printJob = new PrintJob();
    	
    	String path = "src/test/resources/ocrtestdata/standart";

    	File file = new File(path);
    	String absolutePath = file.getAbsolutePath();
    	applicationProperties.setArchiveRepo(absolutePath);
    	
    	printJob.setArchivePath(ARCHIVE_PATH);
    	printJob.setClientMachine(CLIENT_MACHINE);
    	printJob.setDocumentName(DOCUMENT_NAME);
    	printJob.setJobId(JOB_UUID);
    	printJob.setPrinterId(PRINTER_ID.longValue());
    	printJob.setPrinterJobLogId(PRINTER_JOB_LOG_ID.longValue());
    	printJob.setProcessStatus(ProcessStatus.NOT_CHECKED);
    	printJob.setUserId(User_ID.longValue());
    	printJob.setPrintDate(LocalDate.now());
    	
    }
    
    
    @Before
    public void init() {
    	//when(printJobService.findJobs(anyInt())).thenReturn(prepareJobDummyData());
    	//jobImportService = new JobImportService(printJobRepository,printJobService);
    	prepareJobDummyData();
    	analyzeService = new AnalyzeService(printJobRepository, printJobService, applicationProperties, ocrService, restrictedKeywordRepository, printJobPageRepository);
    	printJobPageRepository.deleteAll();
    	printJobRepository.deleteAll();
    }

    @After
    public void deleteFiles() throws IOException {
    	FileUtils.cleanDirectory(fileExport);
    }	
    
    @Test
    @Transactional
    public void startAnalyzing() throws Exception {
    	printJobRepository.save(printJob);
    	analyzeService.analyze();;
    	List<PrintJob> list = printJobRepository.findAll();
    	
    	PrintJob printJob = list.get(0);
    	assertThat(printJob.getArchivePath()).isEqualTo(ARCHIVE_PATH);
    	assertThat(printJob.getClientMachine()).isEqualTo(CLIENT_MACHINE);
    	assertThat(printJob.getDocumentName()).isEqualTo(DOCUMENT_NAME);
    	assertThat(printJob.getJobId()).isEqualTo(JOB_UUID);
    	assertThat(printJob.getPrintDate()).isNotNull();
    	assertThat(printJob.getPrinterId().longValue()).isEqualTo(PRINTER_ID.longValue());
    	assertThat(printJob.getProcessStatus()).isEqualTo(ProcessStatus.CHECKED);
    	assertThat(printJob.getResultStatus()).isEqualTo(ResultStatus.SAFETY);
    	assertThat(printJob.getUserId().longValue()).isEqualTo(User_ID.longValue());
    	
    	List<PrintJobPage> list2 = printJobPageRepository.findAll();
    	assertThat(list2.size()).isEqualTo(2);
    	PrintJobPage printJobPage = list2.get(0);
    
    	assertThat(printJobPage.getProcessed()).isTrue();
    	assertThat(printJobPage.getResultStatus()).isEqualTo(ResultStatus.SAFETY);
    	assertThat(printJobPage.getRestrictedKeywords()).isNull();
    
    	PrintJobPage printJobPage2 = list2.get(0);
    	assertThat(printJobPage2.getProcessed()).isTrue();
    	assertThat(printJobPage2.getResultStatus()).isEqualTo(ResultStatus.SAFETY);
    	assertThat(printJobPage2.getRestrictedKeywords()).isNull();
    	
    	File file = new File(printJobPage.getExportPath());
    	assertThat(file.exists()).isTrue();
    	
    	String str =  new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    	assertThat(str).isNotNull();
    
    }
    @Test
    public void startAnalyzingOnePageRestrictedOneKeyword() throws Exception {
    	printJobRepository.save(printJob);
    	RestrictedKeyword keyword = new RestrictedKeyword();
    	keyword.setActive(true);
    	keyword.setKeyword("Mehaba");
    	restrictedKeywordRepository.save(keyword);
    	
    	
    	analyzeService.analyze();;
    	List<PrintJob> list = printJobRepository.findAll();
    	
    	PrintJob printJob = list.get(0);
    	assertThat(printJob.getArchivePath()).isEqualTo(ARCHIVE_PATH);
    	assertThat(printJob.getClientMachine()).isEqualTo(CLIENT_MACHINE);
    	assertThat(printJob.getDocumentName()).isEqualTo(DOCUMENT_NAME);
    	assertThat(printJob.getJobId()).isEqualTo(JOB_UUID);
    	assertThat(printJob.getPrintDate()).isNotNull();
    	assertThat(printJob.getPrinterId().longValue()).isEqualTo(PRINTER_ID.longValue());
    	assertThat(printJob.getProcessStatus()).isEqualTo(ProcessStatus.CHECKED);
    	assertThat(printJob.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJob.getUserId().longValue()).isEqualTo(User_ID.longValue());
    	
    	List<PrintJobPage> list2 = printJobPageRepository.findAll();
    	assertThat(list2.size()).isEqualTo(2);
    	
    	PrintJobPage printJobPage = list2.get(0);
      	assertThat(printJobPage.getProcessed()).isTrue();
    	assertThat(printJobPage.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJobPage.getRestrictedKeywords()).contains("Mehaba");
    
    	PrintJobPage printJobPage2 = list2.get(1);
      	assertThat(printJobPage2.getProcessed()).isTrue();
    	assertThat(printJobPage2.getResultStatus()).isEqualTo(ResultStatus.SAFETY);
    	assertThat(printJobPage2.getRestrictedKeywords()).isNull();
    
    	File file = new File(printJobPage.getExportPath());
    	assertThat(file.exists()).isTrue();
    	String str =  new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    	assertThat(str).isNotNull();
    	assertThat(str.contains("Mehaba")).isTrue();
    }

    @Test
    public void startAnalyzingRestrictedOneKeywordCaseSensitive() throws Exception {
    	printJobRepository.save(printJob);
    	RestrictedKeyword keyword = new RestrictedKeyword();
    	keyword.setActive(true);
    	keyword.setKeyword("mehaba");
    	restrictedKeywordRepository.save(keyword);
    	
    	
    	analyzeService.analyze();;
    	List<PrintJob> list = printJobRepository.findAll();
    	
    	PrintJob printJob = list.get(0);
    	assertThat(printJob.getArchivePath()).isEqualTo(ARCHIVE_PATH);
    	assertThat(printJob.getClientMachine()).isEqualTo(CLIENT_MACHINE);
    	assertThat(printJob.getDocumentName()).isEqualTo(DOCUMENT_NAME);
    	assertThat(printJob.getJobId()).isEqualTo(JOB_UUID);
    	assertThat(printJob.getPrintDate()).isNotNull();
    	assertThat(printJob.getPrinterId().longValue()).isEqualTo(PRINTER_ID.longValue());
    	assertThat(printJob.getProcessStatus()).isEqualTo(ProcessStatus.CHECKED);
    	assertThat(printJob.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJob.getUserId().longValue()).isEqualTo(User_ID.longValue());
    	
    	List<PrintJobPage> list2 = printJobPageRepository.findAll();
    	assertThat(list2.size()).isEqualTo(2);
    	
    	PrintJobPage printJobPage = list2.get(0);
    	assertThat(printJobPage.getProcessed()).isTrue();
    	assertThat(printJobPage.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJobPage.getRestrictedKeywords()).contains("mehaba");
    	
    	PrintJobPage printJobPage2 = list2.get(1);
    	assertThat(printJobPage2.getProcessed()).isTrue();
    	assertThat(printJobPage2.getResultStatus()).isEqualTo(ResultStatus.SAFETY);
    	assertThat(printJobPage2.getRestrictedKeywords()).isNull();
    	
    	
    }
   
    @Test
    public void startAnalyzingRestrictedTwoKeyword() throws Exception {
    	printJobRepository.save(printJob);
    	RestrictedKeyword keyword = new RestrictedKeyword();
    	keyword.setActive(true);
    	keyword.setKeyword("mehaba");
    	restrictedKeywordRepository.save(keyword);
    	
    	RestrictedKeyword keyword2 = new RestrictedKeyword();
    	keyword2.setActive(true);
    	keyword2.setKeyword("ikinci");
    	restrictedKeywordRepository.save(keyword2);
    	
    	analyzeService.analyze();;
    	List<PrintJob> list = printJobRepository.findAll();
    	
    	PrintJob printJob = list.get(0);
    	assertThat(printJob.getArchivePath()).isEqualTo(ARCHIVE_PATH);
    	assertThat(printJob.getClientMachine()).isEqualTo(CLIENT_MACHINE);
    	assertThat(printJob.getDocumentName()).isEqualTo(DOCUMENT_NAME);
    	assertThat(printJob.getJobId()).isEqualTo(JOB_UUID);
    	assertThat(printJob.getPrintDate()).isNotNull();
    	assertThat(printJob.getPrinterId().longValue()).isEqualTo(PRINTER_ID.longValue());
    	assertThat(printJob.getProcessStatus()).isEqualTo(ProcessStatus.CHECKED);
    	assertThat(printJob.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJob.getUserId().longValue()).isEqualTo(User_ID.longValue());
    	
    	List<PrintJobPage> list2 = printJobPageRepository.findAll();
    	assertThat(list2.size()).isEqualTo(2);
    	
    	PrintJobPage printJobPage = list2.get(0);
    	assertThat(printJobPage.getProcessed()).isTrue();
    	assertThat(printJobPage.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJobPage.getRestrictedKeywords()).contains("mehaba");
    	
    	PrintJobPage printJobPage2 = list2.get(1);
    	assertThat(printJobPage2.getProcessed()).isTrue();
    	assertThat(printJobPage2.getResultStatus()).isEqualTo(ResultStatus.NOT_SAFETY);
    	assertThat(printJobPage2.getRestrictedKeywords()).contains("ikinci");
    }
   

}
