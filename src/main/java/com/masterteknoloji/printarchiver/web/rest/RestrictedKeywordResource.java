package com.masterteknoloji.printarchiver.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.printarchiver.domain.RestrictedKeyword;

import com.masterteknoloji.printarchiver.repository.RestrictedKeywordRepository;
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
 * REST controller for managing RestrictedKeyword.
 */
@RestController
@RequestMapping("/api")
public class RestrictedKeywordResource {

    private final Logger log = LoggerFactory.getLogger(RestrictedKeywordResource.class);

    private static final String ENTITY_NAME = "restrictedKeyword";

    private final RestrictedKeywordRepository restrictedKeywordRepository;

    public RestrictedKeywordResource(RestrictedKeywordRepository restrictedKeywordRepository) {
        this.restrictedKeywordRepository = restrictedKeywordRepository;
    }

    /**
     * POST  /restricted-keywords : Create a new restrictedKeyword.
     *
     * @param restrictedKeyword the restrictedKeyword to create
     * @return the ResponseEntity with status 201 (Created) and with body the new restrictedKeyword, or with status 400 (Bad Request) if the restrictedKeyword has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/restricted-keywords")
    @Timed
    public ResponseEntity<RestrictedKeyword> createRestrictedKeyword(@RequestBody RestrictedKeyword restrictedKeyword) throws URISyntaxException {
        log.debug("REST request to save RestrictedKeyword : {}", restrictedKeyword);
        if (restrictedKeyword.getId() != null) {
            throw new BadRequestAlertException("A new restrictedKeyword cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestrictedKeyword result = restrictedKeywordRepository.save(restrictedKeyword);
        return ResponseEntity.created(new URI("/api/restricted-keywords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /restricted-keywords : Updates an existing restrictedKeyword.
     *
     * @param restrictedKeyword the restrictedKeyword to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated restrictedKeyword,
     * or with status 400 (Bad Request) if the restrictedKeyword is not valid,
     * or with status 500 (Internal Server Error) if the restrictedKeyword couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/restricted-keywords")
    @Timed
    public ResponseEntity<RestrictedKeyword> updateRestrictedKeyword(@RequestBody RestrictedKeyword restrictedKeyword) throws URISyntaxException {
        log.debug("REST request to update RestrictedKeyword : {}", restrictedKeyword);
        if (restrictedKeyword.getId() == null) {
            return createRestrictedKeyword(restrictedKeyword);
        }
        RestrictedKeyword result = restrictedKeywordRepository.save(restrictedKeyword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, restrictedKeyword.getId().toString()))
            .body(result);
    }

    /**
     * GET  /restricted-keywords : get all the restrictedKeywords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of restrictedKeywords in body
     */
    @GetMapping("/restricted-keywords")
    @Timed
    public ResponseEntity<List<RestrictedKeyword>> getAllRestrictedKeywords(Pageable pageable) {
        log.debug("REST request to get a page of RestrictedKeywords");
        Page<RestrictedKeyword> page = restrictedKeywordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/restricted-keywords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /restricted-keywords/:id : get the "id" restrictedKeyword.
     *
     * @param id the id of the restrictedKeyword to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the restrictedKeyword, or with status 404 (Not Found)
     */
    @GetMapping("/restricted-keywords/{id}")
    @Timed
    public ResponseEntity<RestrictedKeyword> getRestrictedKeyword(@PathVariable Long id) {
        log.debug("REST request to get RestrictedKeyword : {}", id);
        RestrictedKeyword restrictedKeyword = restrictedKeywordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(restrictedKeyword));
    }

    /**
     * DELETE  /restricted-keywords/:id : delete the "id" restrictedKeyword.
     *
     * @param id the id of the restrictedKeyword to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/restricted-keywords/{id}")
    @Timed
    public ResponseEntity<Void> deleteRestrictedKeyword(@PathVariable Long id) {
        log.debug("REST request to delete RestrictedKeyword : {}", id);
        restrictedKeywordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
