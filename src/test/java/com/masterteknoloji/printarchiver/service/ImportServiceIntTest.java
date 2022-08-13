package com.masterteknoloji.printarchiver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.masterteknoloji.printarchiver.PrintarchiverApp;
import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.domain.User;
import com.masterteknoloji.printarchiver.domain.enumeration.ProcessStatus;
import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;
import com.masterteknoloji.printarchiver.repository.PrintJobRepository;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintarchiverApp.class)
public class ImportServiceIntTest {

    
    private JobImportService jobImportService;

    @Autowired
    private PrintJobRepository printJobRepository;
    
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
    final BigInteger PAGE_COUNT = new BigInteger("2");
    
    
    private List prepareJobDummyData() {
    	jobList = new ArrayList();
    	
    	Object[] object = new Object[45];    	
    	object[41] = ARCHIVE_PATH;
    	object[11] = CLIENT_MACHINE;
    	object[10] = DOCUMENT_NAME;
    	object[40] = JOB_UUID;
    	object[1] = INSERT_DATE;
    	object[7] = PRINTER_ID;
    	object[0] = PRINTER_JOB_LOG_ID;
    	object[3] = User_ID;
    	object[12] = PAGE_COUNT;
    	jobList.add(object);
    	return jobList;
    			
    }
    
    
    @Before
    public void init() {
    	when(printJobService.findJobs(anyInt())).thenReturn(prepareJobDummyData());
    	jobImportService = new JobImportService(printJobRepository,printJobService);
    	//printJobRepository.deleteAll();
    	
    }

    @Test
    @Transactional
    public void getJobList() throws ParseException {
    	jobImportService.getJobList();
    	List<PrintJob> list = printJobRepository.findAll();
    	
    	PrintJob printJob = list.get(0);
    	assertThat(printJob.getArchivePath()).isEqualTo(ARCHIVE_PATH);
    	assertThat(printJob.getClientMachine()).isEqualTo(CLIENT_MACHINE);
    	assertThat(printJob.getDocumentName()).isEqualTo(DOCUMENT_NAME);
    	assertThat(printJob.getJobId()).isEqualTo(JOB_UUID);
    	assertThat(printJob.getPrintDate()).isNotNull();
    	assertThat(printJob.getPrinterId().longValue()).isEqualTo(PRINTER_ID.longValue());
    	assertThat(printJob.getProcessStatus()).isEqualTo(ProcessStatus.NOT_CHECKED);
    	assertThat(printJob.getResultStatus()).isEqualTo(ResultStatus.SAFETY);
    	assertThat(printJob.getUserId().longValue()).isEqualTo(User_ID.longValue());
    	assertThat(printJob.getPageCount().longValue()).isEqualTo(PAGE_COUNT.longValue());
    }

   
    @Test
    @Transactional
    public void getJobListDublicate() throws ParseException {
    	jobImportService.getJobList();
    	List<PrintJob> list = printJobRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	
    	jobImportService.getJobList();
    	assertThat(list.size()).isEqualTo(1);
    	
    }

}
