package com.masterteknoloji.printarchiver.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.domain.enumeration.ProcessStatus;
import com.masterteknoloji.printarchiver.repository.PrintJobRepository;

@Service
@Transactional
public class JobImportService {

	private final PrintJobRepository printJobRepository;
	
	private final PrintJobService printJobService;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	public JobImportService(PrintJobRepository printJobRepository, PrintJobService printJobService) {
		super();
		this.printJobRepository = printJobRepository;
		this.printJobService = printJobService;
	}
	
	//@Scheduled(fixedDelay = 15000)
	public void getJobList() throws ParseException {
		List<Object[]> list = printJobService.findJobs(5);
		
		for (Object[] object : list) {
			System.out.println(object[0]);
			PrintJob printJob = new PrintJob();
			BigInteger jobId = (BigInteger)object[0];
			if(printJobRepository.findByJobId(jobId.longValue()).size()>0)
				continue;
			
			printJob.setArchivePath(String.valueOf(object[41]));
			printJob.setClientMachine(String.valueOf(object[11]));
			printJob.setDocumentName(String.valueOf(object[10]));
			printJob.setJobId(String.valueOf(object[40]));
			printJob.setPrintDate(getDate(object[1]));
			printJob.setPrinterId(getLong(object[7].toString()));
			printJob.setPrinterJobLogId(getLong(object[0].toString()));
			printJob.setProcessStatus(ProcessStatus.NOT_CHECKED);
			printJob.setUserId(getLong(object[3].toString()));
			
			printJobRepository.save(printJob);
		}
		
		System.out.println("sdfsd");
		
	}
	
	public LocalDate getDate(Object object) throws ParseException {
		if(object == null)
			return null;
		
		Date date = simpleDateFormat.parse(object.toString());
		return convertToLocalDateViaInstant(date);
	}
	
	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	public Long getLong(Object object) {
		if(object == null)
			return null;
		
		return Long.valueOf(object.toString());
	}
}
