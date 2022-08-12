package com.masterteknoloji.printarchiver.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.printarchiver.domain.PrintJobPage;

import com.masterteknoloji.printarchiver.repository.PrintJobPageRepository;
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
 * REST controller for managing PrintJobPage.
 */
@RestController
@RequestMapping("/api")
public class PrintJobPageResource {

    private final Logger log = LoggerFactory.getLogger(PrintJobPageResource.class);

    private static final String ENTITY_NAME = "printJobPage";

    private final PrintJobPageRepository printJobPageRepository;

    public PrintJobPageResource(PrintJobPageRepository printJobPageRepository) {
        this.printJobPageRepository = printJobPageRepository;
    }

    /**
     * POST  /print-job-pages : Create a new printJobPage.
     *
     * @param printJobPage the printJobPage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new printJobPage, or with status 400 (Bad Request) if the printJobPage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/print-job-pages")
    @Timed
    public ResponseEntity<PrintJobPage> createPrintJobPage(@RequestBody PrintJobPage printJobPage) throws URISyntaxException {
        log.debug("REST request to save PrintJobPage : {}", printJobPage);
        if (printJobPage.getId() != null) {
            throw new BadRequestAlertException("A new printJobPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrintJobPage result = printJobPageRepository.save(printJobPage);
        return ResponseEntity.created(new URI("/api/print-job-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /print-job-pages : Updates an existing printJobPage.
     *
     * @param printJobPage the printJobPage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated printJobPage,
     * or with status 400 (Bad Request) if the printJobPage is not valid,
     * or with status 500 (Internal Server Error) if the printJobPage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/print-job-pages")
    @Timed
    public ResponseEntity<PrintJobPage> updatePrintJobPage(@RequestBody PrintJobPage printJobPage) throws URISyntaxException {
        log.debug("REST request to update PrintJobPage : {}", printJobPage);
        if (printJobPage.getId() == null) {
            return createPrintJobPage(printJobPage);
        }
        PrintJobPage result = printJobPageRepository.save(printJobPage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, printJobPage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /print-job-pages : get all the printJobPages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of printJobPages in body
     */
    @GetMapping("/print-job-pages")
    @Timed
    public ResponseEntity<List<PrintJobPage>> getAllPrintJobPages(Pageable pageable) {
        log.debug("REST request to get a page of PrintJobPages");
        Page<PrintJobPage> page = printJobPageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/print-job-pages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /print-job-pages/:id : get the "id" printJobPage.
     *
     * @param id the id of the printJobPage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the printJobPage, or with status 404 (Not Found)
     */
    @GetMapping("/print-job-pages/{id}")
    @Timed
    public ResponseEntity<PrintJobPage> getPrintJobPage(@PathVariable Long id) {
        log.debug("REST request to get PrintJobPage : {}", id);
        PrintJobPage printJobPage = printJobPageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(printJobPage));
    }

    /**
     * DELETE  /print-job-pages/:id : delete the "id" printJobPage.
     *
     * @param id the id of the printJobPage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/print-job-pages/{id}")
    @Timed
    public ResponseEntity<Void> deletePrintJobPage(@PathVariable Long id) {
        log.debug("REST request to delete PrintJobPage : {}", id);
        printJobPageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
