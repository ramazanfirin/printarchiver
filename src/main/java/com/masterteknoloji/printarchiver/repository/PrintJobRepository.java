package com.masterteknoloji.printarchiver.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.printarchiver.domain.PrintJob;


/**
 * Spring Data JPA repository for the PrintJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrintJobRepository extends JpaRepository<PrintJob, Long> {

	@Query(value = "SELECT * FROM papercut.tbl_printer_usage_log where printer_usage_log_id> :start", nativeQuery = true)
	List findJobs(@Param("start") int start);
	
	@Query(value = "SELECT user_name FROM papercut.tbl_user where user_id = :userId", nativeQuery = true)
	List findUserName(@Param("userId") Long userId);
	
	@Query(value = "SELECT printer_name FROM papercut.tbl_printer where printer_Id = :printerId", nativeQuery = true)
	List findPrinterName(@Param("printerId") Long printerId);
	
	@Query(value = "SELECT p FROM PrintJob p where p.printerJobLogId = :jobId")
	List<PrintJob> findByJobId(@Param("jobId") Long jobId);
	
	@Query(value = "SELECT p FROM PrintJob p where p.processStatus = 'NOT_CHECKED'")
	List<PrintJob> findUnrocessedJobs();
	
}
