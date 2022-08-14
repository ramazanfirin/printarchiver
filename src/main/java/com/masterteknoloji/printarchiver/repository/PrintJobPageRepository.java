package com.masterteknoloji.printarchiver.repository;

import com.masterteknoloji.printarchiver.domain.PrintJob;
import com.masterteknoloji.printarchiver.domain.PrintJobPage;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the PrintJobPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrintJobPageRepository extends JpaRepository<PrintJobPage, Long> {

	@Query(value = "SELECT p FROM PrintJobPage p where p.job.id = :printJobId order by p.index" )
	List<PrintJobPage> findByJobId(@Param("printJobId") Long printJobId);
}
