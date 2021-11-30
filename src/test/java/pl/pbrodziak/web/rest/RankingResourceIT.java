package pl.pbrodziak.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import pl.pbrodziak.domain.Ranking;
import pl.pbrodziak.repository.RankingRepository;
import pl.pbrodziak.service.criteria.RankingCriteria;

/**
 * Integration tests for the {@link RankingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RankingResourceIT {

    private static final Long DEFAULT_POINTS = 1L;
    private static final Long UPDATED_POINTS = 2L;
    private static final Long SMALLER_POINTS = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/rankings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRankingMockMvc;

    private Ranking ranking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranking createEntity(EntityManager em) {
        Ranking ranking = new Ranking().points(DEFAULT_POINTS);
        return ranking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranking createUpdatedEntity(EntityManager em) {
        Ranking ranking = new Ranking().points(UPDATED_POINTS);
        return ranking;
    }

    @BeforeEach
    public void initTest() {
        ranking = createEntity(em);
    }

    @Test
    @Transactional
    void createRanking() throws Exception {
        int databaseSizeBeforeCreate = rankingRepository.findAll().size();
        // Create the Ranking
        restRankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isCreated());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeCreate + 1);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createRankingWithExistingId() throws Exception {
        // Create the Ranking with an existing ID
        ranking.setId(1L);

        int databaseSizeBeforeCreate = rankingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRankings() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList
        restRankingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ranking.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.intValue())));
    }

    @Test
    @Transactional
    void getRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get the ranking
        restRankingMockMvc
            .perform(get(ENTITY_API_URL_ID, ranking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ranking.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.intValue()));
    }

    @Test
    @Transactional
    void getRankingsByIdFiltering() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        Long id = ranking.getId();

        defaultRankingShouldBeFound("id.equals=" + id);
        defaultRankingShouldNotBeFound("id.notEquals=" + id);

        defaultRankingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRankingShouldNotBeFound("id.greaterThan=" + id);

        defaultRankingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRankingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points equals to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the rankingList where points equals to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points not equals to DEFAULT_POINTS
        defaultRankingShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the rankingList where points not equals to UPDATED_POINTS
        defaultRankingShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultRankingShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the rankingList where points equals to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is not null
        defaultRankingShouldBeFound("points.specified=true");

        // Get all the rankingList where points is null
        defaultRankingShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is greater than or equal to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the rankingList where points is greater than or equal to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is less than or equal to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the rankingList where points is less than or equal to SMALLER_POINTS
        defaultRankingShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is less than DEFAULT_POINTS
        defaultRankingShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the rankingList where points is less than UPDATED_POINTS
        defaultRankingShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllRankingsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is greater than DEFAULT_POINTS
        defaultRankingShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the rankingList where points is greater than SMALLER_POINTS
        defaultRankingShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRankingShouldBeFound(String filter) throws Exception {
        restRankingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ranking.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.intValue())));

        // Check, that the count call also returns 1
        restRankingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRankingShouldNotBeFound(String filter) throws Exception {
        restRankingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRankingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRanking() throws Exception {
        // Get the ranking
        restRankingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking
        Ranking updatedRanking = rankingRepository.findById(ranking.getId()).get();
        // Disconnect from session so that the updates on updatedRanking are not directly saved in db
        em.detach(updatedRanking);
        updatedRanking.points(UPDATED_POINTS);

        restRankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRanking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRanking))
            )
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ranking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRankingWithPatch() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking using partial update
        Ranking partialUpdatedRanking = new Ranking();
        partialUpdatedRanking.setId(ranking.getId());

        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRanking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRanking))
            )
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateRankingWithPatch() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking using partial update
        Ranking partialUpdatedRanking = new Ranking();
        partialUpdatedRanking.setId(ranking.getId());

        partialUpdatedRanking.points(UPDATED_POINTS);

        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRanking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRanking))
            )
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ranking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeDelete = rankingRepository.findAll().size();

        // Delete the ranking
        restRankingMockMvc
            .perform(delete(ENTITY_API_URL_ID, ranking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
