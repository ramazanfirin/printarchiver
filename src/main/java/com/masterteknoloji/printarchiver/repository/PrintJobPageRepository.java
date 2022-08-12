package com.masterteknoloji.printarchiver.repository;

import com.masterteknoloji.printarchiver.domain.PrintJobPage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PrintJobPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrintJobPageRepository extends JpaRepository<PrintJobPage, Long> {

}
