package com.masterteknoloji.printarchiver.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterteknoloji.printarchiver.repository.PrintJobRepository;

@Service
@Transactional
public class PrintJobService {

	private final PrintJobRepository printJobRepository;
	
	
	
	public PrintJobService(PrintJobRepository printJobRepository) {
		super();
		this.printJobRepository = printJobRepository;
	}



	public List findJobs(Integer start) {
		return printJobRepository.findJobs(start);
	}
}
