package pl.pbrodziak.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.IntegrationTest;
import pl.pbrodziak.domain.TimeShit;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.TimeShitRepository;
import pl.pbrodziak.service.criteria.TimeShitCriteria;

/**
 * Integration tests for the {@link TimeShitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeShitResourceIT {

    private static final Boolean DEFAULT_PRESENT = false;
    private static final Boolean UPDATED_PRESENT = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/time-shits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeShitRepository timeShitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeShitMockMvc;

    private TimeShit timeShit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeShit createEntity(EntityManager em) {
        TimeShit timeShit = new TimeShit().present(DEFAULT_PRESENT).date(DEFAULT_DATE);
        return timeShit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeShit createUpdatedEntity(EntityManager em) {
        TimeShit timeShit = new TimeShit().present(UPDATED_PRESENT).date(UPDATED_DATE);
        return timeShit;
    }

    @BeforeEach
    public void initTest() {
        timeShit = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeShit() throws Exception {
        int databaseSizeBeforeCreate = timeShitRepository.findAll().size();
        // Create the TimeShit
        restTimeShitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeShit)))
            .andExpect(status().isCreated());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeCreate + 1);
        TimeShit testTimeShit = timeShitList.get(timeShitList.size() - 1);
        assertThat(testTimeShit.getPresent()).isEqualTo(DEFAULT_PRESENT);
        assertThat(testTimeShit.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createTimeShitWithExistingId() throws Exception {
        // Create the TimeShit with an existing ID
        timeShit.setId(1L);

        int databaseSizeBeforeCreate = timeShitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeShitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeShit)))
            .andExpect(status().isBadRequest());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTimeShits() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList
        restTimeShitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeShit.getId().intValue())))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getTimeShit() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get the timeShit
        restTimeShitMockMvc
            .perform(get(ENTITY_API_URL_ID, timeShit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeShit.getId().intValue()))
            .andExpect(jsonPath("$.present").value(DEFAULT_PRESENT.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getTimeShitsByIdFiltering() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        Long id = timeShit.getId();

        defaultTimeShitShouldBeFound("id.equals=" + id);
        defaultTimeShitShouldNotBeFound("id.notEquals=" + id);

        defaultTimeShitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTimeShitShouldNotBeFound("id.greaterThan=" + id);

        defaultTimeShitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTimeShitShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTimeShitsByPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where present equals to DEFAULT_PRESENT
        defaultTimeShitShouldBeFound("present.equals=" + DEFAULT_PRESENT);

        // Get all the timeShitList where present equals to UPDATED_PRESENT
        defaultTimeShitShouldNotBeFound("present.equals=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void getAllTimeShitsByPresentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where present not equals to DEFAULT_PRESENT
        defaultTimeShitShouldNotBeFound("present.notEquals=" + DEFAULT_PRESENT);

        // Get all the timeShitList where present not equals to UPDATED_PRESENT
        defaultTimeShitShouldBeFound("present.notEquals=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void getAllTimeShitsByPresentIsInShouldWork() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where present in DEFAULT_PRESENT or UPDATED_PRESENT
        defaultTimeShitShouldBeFound("present.in=" + DEFAULT_PRESENT + "," + UPDATED_PRESENT);

        // Get all the timeShitList where present equals to UPDATED_PRESENT
        defaultTimeShitShouldNotBeFound("present.in=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    void getAllTimeShitsByPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where present is not null
        defaultTimeShitShouldBeFound("present.specified=true");

        // Get all the timeShitList where present is null
        defaultTimeShitShouldNotBeFound("present.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date equals to DEFAULT_DATE
        defaultTimeShitShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the timeShitList where date equals to UPDATED_DATE
        defaultTimeShitShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date not equals to DEFAULT_DATE
        defaultTimeShitShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the timeShitList where date not equals to UPDATED_DATE
        defaultTimeShitShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTimeShitShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the timeShitList where date equals to UPDATED_DATE
        defaultTimeShitShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date is not null
        defaultTimeShitShouldBeFound("date.specified=true");

        // Get all the timeShitList where date is null
        defaultTimeShitShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date is greater than or equal to DEFAULT_DATE
        defaultTimeShitShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the timeShitList where date is greater than or equal to UPDATED_DATE
        defaultTimeShitShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date is less than or equal to DEFAULT_DATE
        defaultTimeShitShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the timeShitList where date is less than or equal to SMALLER_DATE
        defaultTimeShitShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date is less than DEFAULT_DATE
        defaultTimeShitShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the timeShitList where date is less than UPDATED_DATE
        defaultTimeShitShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        // Get all the timeShitList where date is greater than DEFAULT_DATE
        defaultTimeShitShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the timeShitList where date is greater than SMALLER_DATE
        defaultTimeShitShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTimeShitsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        timeShit.setUser(user);
        timeShitRepository.saveAndFlush(timeShit);
        Long userId = user.getId();

        // Get all the timeShitList where user equals to userId
        defaultTimeShitShouldBeFound("userId.equals=" + userId);

        // Get all the timeShitList where user equals to (userId + 1)
        defaultTimeShitShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimeShitShouldBeFound(String filter) throws Exception {
        restTimeShitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeShit.getId().intValue())))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restTimeShitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimeShitShouldNotBeFound(String filter) throws Exception {
        restTimeShitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimeShitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTimeShit() throws Exception {
        // Get the timeShit
        restTimeShitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTimeShit() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();

        // Update the timeShit
        TimeShit updatedTimeShit = timeShitRepository.findById(timeShit.getId()).get();
        // Disconnect from session so that the updates on updatedTimeShit are not directly saved in db
        em.detach(updatedTimeShit);
        updatedTimeShit.present(UPDATED_PRESENT).date(UPDATED_DATE);

        restTimeShitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimeShit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTimeShit))
            )
            .andExpect(status().isOk());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
        TimeShit testTimeShit = timeShitList.get(timeShitList.size() - 1);
        assertThat(testTimeShit.getPresent()).isEqualTo(UPDATED_PRESENT);
        assertThat(testTimeShit.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTimeShit() throws Exception {
        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();
        timeShit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeShitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeShit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeShit))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeShit() throws Exception {
        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();
        timeShit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeShitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeShit))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeShit() throws Exception {
        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();
        timeShit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeShitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeShit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeShitWithPatch() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();

        // Update the timeShit using partial update
        TimeShit partialUpdatedTimeShit = new TimeShit();
        partialUpdatedTimeShit.setId(timeShit.getId());

        partialUpdatedTimeShit.present(UPDATED_PRESENT).date(UPDATED_DATE);

        restTimeShitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeShit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeShit))
            )
            .andExpect(status().isOk());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
        TimeShit testTimeShit = timeShitList.get(timeShitList.size() - 1);
        assertThat(testTimeShit.getPresent()).isEqualTo(UPDATED_PRESENT);
        assertThat(testTimeShit.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTimeShitWithPatch() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();

        // Update the timeShit using partial update
        TimeShit partialUpdatedTimeShit = new TimeShit();
        partialUpdatedTimeShit.setId(timeShit.getId());

        partialUpdatedTimeShit.present(UPDATED_PRESENT).date(UPDATED_DATE);

        restTimeShitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeShit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeShit))
            )
            .andExpect(status().isOk());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
        TimeShit testTimeShit = timeShitList.get(timeShitList.size() - 1);
        assertThat(testTimeShit.getPresent()).isEqualTo(UPDATED_PRESENT);
        assertThat(testTimeShit.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTimeShit() throws Exception {
        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();
        timeShit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeShitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeShit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeShit))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeShit() throws Exception {
        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();
        timeShit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeShitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeShit))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeShit() throws Exception {
        int databaseSizeBeforeUpdate = timeShitRepository.findAll().size();
        timeShit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeShitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(timeShit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeShit in the database
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeShit() throws Exception {
        // Initialize the database
        timeShitRepository.saveAndFlush(timeShit);

        int databaseSizeBeforeDelete = timeShitRepository.findAll().size();

        // Delete the timeShit
        restTimeShitMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeShit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TimeShit> timeShitList = timeShitRepository.findAll();
        assertThat(timeShitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
