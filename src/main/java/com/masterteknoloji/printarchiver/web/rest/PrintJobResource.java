package com.masterteknoloji.printarchiver.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.printarchiver.domain.PrintJob;

import com.masterteknoloji.printarchiver.repository.PrintJobRepository;
import com.masterteknoloji.printarchiver.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.printarchiver.web.rest.util.HeaderUtil;
import com.masterteknoloji.printarchiver.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PrintJob.
 */
@RestController
@RequestMapping("/api")
public class PrintJobResource {

    private final Logger log = LoggerFactory.getLogger(PrintJobResource.class);

    private static final String ENTITY_NAME = "printJob";

    private final PrintJobRepository printJobRepository;

    public PrintJobResource(PrintJobRepository printJobRepository) {
        this.printJobRepository = printJobRepository;
    }

    /**
     * POST  /print-jobs : Create a new printJob.
     *
     * @param printJob the printJob to create
     * @return the ResponseEntity with status 201 (Created) and with body the new printJob, or with status 400 (Bad Request) if the printJob has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/print-jobs")
    @Timed
    public ResponseEntity<PrintJob> createPrintJob(@RequestBody PrintJob printJob) throws URISyntaxException {
        log.debug("REST request to save PrintJob : {}", printJob);
        if (printJob.getId() != null) {
            throw new BadRequestAlertException("A new printJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrintJob result = printJobRepository.save(printJob);
        return ResponseEntity.created(new URI("/api/print-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /print-jobs : Updates an existing printJob.
     *
     * @param printJob the printJob to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated printJob,
     * or with status 400 (Bad Request) if the printJob is not valid,
     * or with status 500 (Internal Server Error) if the printJob couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/print-jobs")
    @Timed
    public ResponseEntity<PrintJob> updatePrintJob(@RequestBody PrintJob printJob) throws URISyntaxException {
        log.debug("REST request to update PrintJob : {}", printJob);
        if (printJob.getId() == null) {
            return createPrintJob(printJob);
        }
        PrintJob result = printJobRepository.save(printJob);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, printJob.getId().toString()))
            .body(result);
    }

    /**
     * GET  /print-jobs : get all the printJobs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of printJobs in body
     */
    @GetMapping("/print-jobs")
    @Timed
    public ResponseEntity<List<PrintJob>> getAllPrintJobs(Pageable pageable) {
        log.debug("REST request to get a page of PrintJobs");
        Page<PrintJob> page = printJobRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/print-jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /print-jobs/:id : get the "id" printJob.
     *
     * @param id the id of the printJob to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the printJob, or with status 404 (Not Found)
     */
    @GetMapping("/print-jobs/{id}")
    @Timed
    public ResponseEntity<PrintJob> getPrintJob(@PathVariable Long id) {
        log.debug("REST request to get PrintJob : {}", id);
        PrintJob printJob = printJobRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(printJob));
    }

    /**
     * DELETE  /print-jobs/:id : delete the "id" printJob.
     *
     * @param id the id of the printJob to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/print-jobs/{id}")
    @Timed
    public ResponseEntity<Void> deletePrintJob(@PathVariable Long id) {
        log.debug("REST request to delete PrintJob : {}", id);
        printJobRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
