package com.masterteknoloji.printarchiver.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterteknoloji.printarchiver.config.ApplicationProperties;
import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.domain.PrintJobPage;
import com.masterteknoloji.printarchiver.domain.RestrictedKeyword;
import com.masterteknoloji.printarchiver.domain.enumeration.ProcessStatus;
import com.masterteknoloji.printarchiver.domain.enumeration.ResultStatus;
import com.masterteknoloji.printarchiver.repository.PrintJobPageRepository;
import com.masterteknoloji.printarchiver.repository.PrintJobRepository;
import com.masterteknoloji.printarchiver.repository.RestrictedKeywordRepository;

@Service
@Transactional
public class AnalyzeService {

	private final PrintJobRepository printJobRepository;
	
	private final PrintJobPageRepository printJobPageRepository;
	
	private final PrintJobService printJobService;
	
	private final ApplicationProperties applicationProperties;

	private final OcrService ocrService;
	
	private final RestrictedKeywordRepository restrictedKeywordRepository;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	public AnalyzeService(PrintJobRepository printJobRepository, PrintJobService printJobService, 
			ApplicationProperties applicationProperties, OcrService ocrService, RestrictedKeywordRepository restrictedKeywordRepository,
			PrintJobPageRepository printJobPageRepository) {
		super();
		this.printJobRepository = printJobRepository;
		this.printJobService = printJobService;
		this.applicationProperties = applicationProperties;
		this.ocrService = ocrService;
		this.restrictedKeywordRepository = restrictedKeywordRepository;
		this.printJobPageRepository = printJobPageRepository;
	}
	
	//@Scheduled(fixedDelay = 15000)
	public void analyze() throws Exception {
		List<PrintJob> list = printJobRepository.findUnrocessedJobs();
		
		for (PrintJob printJob : list) {
			String path = applicationProperties.getArchiveRepo()+"\\"+printJob.getArchivePath().replace("/", "\\")+"\\"+printJob.getJobId();
			System.out.println(path);
			
			File directoryPath = new File(path);
		    File filesList[] = directoryPath.listFiles();
			for (int i = 0; i < filesList.length; i++) {
				File file = filesList[i];
				if(!file.getName().startsWith("page") || !file.getName().contains("bh3000"))
					continue;
				String result = ocrService.analyze(file);
				PrintJobPage printJobPage = createPrintJob(printJob, file, result);
				printJobPage = decision(printJobPage);
				if(printJobPage.getResultStatus().equals(ResultStatus.NOT_SAFETY)) {
					printJob.setResultStatus(ResultStatus.NOT_SAFETY);
				    printJobRepository.save(printJob);
				}
			}
			
			printJob.setProcessStatus(ProcessStatus.CHECKED);
			printJobRepository.save(printJob);
		}
		
		
		System.out.println("sdfsd");
		
	}
	
	private PrintJobPage decision(PrintJobPage printJobPage) {
		List<RestrictedKeyword> list = restrictedKeywordRepository.findAll();
		for (RestrictedKeyword restrictedKeyword : list) {
			if(printJobPage.getContent().toLowerCase().contains(restrictedKeyword.getKeyword().toLowerCase())){
				printJobPage.setRestrictedKeywords(printJobPage.getRestrictedKeywords()+"," + restrictedKeyword);
				printJobPage.setResultStatus(ResultStatus.NOT_SAFETY);
			}
		}
		return printJobPageRepository.save(printJobPage);
	}
	
	
	private PrintJobPage createPrintJob(PrintJob printJob,File file,String result) throws IOException {
		PrintJobPage printJobPage = new PrintJobPage();
		printJobPage.setJob(printJob);
		printJobPage.setPagePath(file.getAbsolutePath());
		printJobPage.setProcessed(true);
		printJobPage.setContent(result);
		printJobPage.setResultStatus(ResultStatus.SAFETY);
		printJobPage.setFileName(file.getName());
		String exportPath = applicationProperties.getExportDirectory()+"\\"+file.getName()+".txt";
		printJobPage.setExportPath(exportPath);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath));
	    writer.write(result);
	    writer.close();
		
	    return printJobPageRepository.save(printJobPage);
	}
}
