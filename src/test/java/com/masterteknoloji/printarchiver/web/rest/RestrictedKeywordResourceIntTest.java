package com.masterteknoloji.printarchiver.web.rest;

import com.masterteknoloji.printarchiver.PrintarchiverApp;

import com.masterteknoloji.printarchiver.domain.RestrictedKeyword;
import com.masterteknoloji.printarchiver.repository.RestrictedKeywordRepository;
import com.masterteknoloji.printarchiver.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.masterteknoloji.printarchiver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RestrictedKeywordResource REST controller.
 *
 * @see RestrictedKeywordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintarchiverApp.class)
public class RestrictedKeywordResourceIntTest {

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private RestrictedKeywordRepository restrictedKeywordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRestrictedKeywordMockMvc;

    private RestrictedKeyword restrictedKeyword;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RestrictedKeywordResource restrictedKeywordResource = new RestrictedKeywordResource(restrictedKeywordRepository);
        this.restRestrictedKeywordMockMvc = MockMvcBuilders.standaloneSetup(restrictedKeywordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestrictedKeyword createEntity(EntityManager em) {
        RestrictedKeyword restrictedKeyword = new RestrictedKeyword()
            .keyword(DEFAULT_KEYWORD)
            .active(DEFAULT_ACTIVE);
        return restrictedKeyword;
    }

    @Before
    public void initTest() {
        restrictedKeyword = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestrictedKeyword() throws Exception {
        int databaseSizeBeforeCreate = restrictedKeywordRepository.findAll().size();

        // Create the RestrictedKeyword
        restRestrictedKeywordMockMvc.perform(post("/api/restricted-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restrictedKeyword)))
            .andExpect(status().isCreated());

        // Validate the RestrictedKeyword in the database
        List<RestrictedKeyword> restrictedKeywordList = restrictedKeywordRepository.findAll();
        assertThat(restrictedKeywordList).hasSize(databaseSizeBeforeCreate + 1);
        RestrictedKeyword testRestrictedKeyword = restrictedKeywordList.get(restrictedKeywordList.size() - 1);
        assertThat(testRestrictedKeyword.getKeyword()).isEqualTo(DEFAULT_KEYWORD);
        assertThat(testRestrictedKeyword.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createRestrictedKeywordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restrictedKeywordRepository.findAll().size();

        // Create the RestrictedKeyword with an existing ID
        restrictedKeyword.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestrictedKeywordMockMvc.perform(post("/api/restricted-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restrictedKeyword)))
            .andExpect(status().isBadRequest());

        // Validate the RestrictedKeyword in the database
        List<RestrictedKeyword> restrictedKeywordList = restrictedKeywordRepository.findAll();
        assertThat(restrictedKeywordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRestrictedKeywords() throws Exception {
        // Initialize the database
        restrictedKeywordRepository.saveAndFlush(restrictedKeyword);

        // Get all the restrictedKeywordList
        restRestrictedKeywordMockMvc.perform(get("/api/restricted-keywords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restrictedKeyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getRestrictedKeyword() throws Exception {
        // Initialize the database
        restrictedKeywordRepository.saveAndFlush(restrictedKeyword);

        // Get the restrictedKeyword
        restRestrictedKeywordMockMvc.perform(get("/api/restricted-keywords/{id}", restrictedKeyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restrictedKeyword.getId().intValue()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRestrictedKeyword() throws Exception {
        // Get the restrictedKeyword
        restRestrictedKeywordMockMvc.perform(get("/api/restricted-keywords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestrictedKeyword() throws Exception {
        // Initialize the database
        restrictedKeywordRepository.saveAndFlush(restrictedKeyword);
        int databaseSizeBeforeUpdate = restrictedKeywordRepository.findAll().size();

        // Update the restrictedKeyword
        RestrictedKeyword updatedRestrictedKeyword = restrictedKeywordRepository.findOne(restrictedKeyword.getId());
        // Disconnect from session so that the updates on updatedRestrictedKeyword are not directly saved in db
        em.detach(updatedRestrictedKeyword);
        updatedRestrictedKeyword
            .keyword(UPDATED_KEYWORD)
            .active(UPDATED_ACTIVE);

        restRestrictedKeywordMockMvc.perform(put("/api/restricted-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRestrictedKeyword)))
            .andExpect(status().isOk());

        // Validate the RestrictedKeyword in the database
        List<RestrictedKeyword> restrictedKeywordList = restrictedKeywordRepository.findAll();
        assertThat(restrictedKeywordList).hasSize(databaseSizeBeforeUpdate);
        RestrictedKeyword testRestrictedKeyword = restrictedKeywordList.get(restrictedKeywordList.size() - 1);
        assertThat(testRestrictedKeyword.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testRestrictedKeyword.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingRestrictedKeyword() throws Exception {
        int databaseSizeBeforeUpdate = restrictedKeywordRepository.findAll().size();

        // Create the RestrictedKeyword

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRestrictedKeywordMockMvc.perform(put("/api/restricted-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restrictedKeyword)))
            .andExpect(status().isCreated());

        // Validate the RestrictedKeyword in the database
        List<RestrictedKeyword> restrictedKeywordList = restrictedKeywordRepository.findAll();
        assertThat(restrictedKeywordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRestrictedKeyword() throws Exception {
        // Initialize the database
        restrictedKeywordRepository.saveAndFlush(restrictedKeyword);
        int databaseSizeBeforeDelete = restrictedKeywordRepository.findAll().size();

        // Get the restrictedKeyword
        restRestrictedKeywordMockMvc.perform(delete("/api/restricted-keywords/{id}", restrictedKeyword.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RestrictedKeyword> restrictedKeywordList = restrictedKeywordRepository.findAll();
        assertThat(restrictedKeywordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestrictedKeyword.class);
        RestrictedKeyword restrictedKeyword1 = new RestrictedKeyword();
        restrictedKeyword1.setId(1L);
        RestrictedKeyword restrictedKeyword2 = new RestrictedKeyword();
        restrictedKeyword2.setId(restrictedKeyword1.getId());
        assertThat(restrictedKeyword1).isEqualTo(restrictedKeyword2);
        restrictedKeyword2.setId(2L);
        assertThat(restrictedKeyword1).isNotEqualTo(restrictedKeyword2);
        restrictedKeyword1.setId(null);
        assertThat(restrictedKeyword1).isNotEqualTo(restrictedKeyword2);
    }
}
