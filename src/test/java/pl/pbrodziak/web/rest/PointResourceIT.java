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
import pl.pbrodziak.domain.Point;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.PointRepository;
import pl.pbrodziak.service.criteria.PointCriteria;

/**
 * Integration tests for the {@link PointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;
    private static final Long SMALLER_VALUE = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/points";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointMockMvc;

    private Point point;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Point createEntity(EntityManager em) {
        Point point = new Point().date(DEFAULT_DATE).value(DEFAULT_VALUE);
        return point;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Point createUpdatedEntity(EntityManager em) {
        Point point = new Point().date(UPDATED_DATE).value(UPDATED_VALUE);
        return point;
    }

    @BeforeEach
    public void initTest() {
        point = createEntity(em);
    }

    @Test
    @Transactional
    void createPoint() throws Exception {
        int databaseSizeBeforeCreate = pointRepository.findAll().size();
        // Create the Point
        restPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(point)))
            .andExpect(status().isCreated());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeCreate + 1);
        Point testPoint = pointList.get(pointList.size() - 1);
        assertThat(testPoint.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPoint.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createPointWithExistingId() throws Exception {
        // Create the Point with an existing ID
        point.setId(1L);

        int databaseSizeBeforeCreate = pointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(point)))
            .andExpect(status().isBadRequest());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPoints() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList
        restPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(point.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }

    @Test
    @Transactional
    void getPoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get the point
        restPointMockMvc
            .perform(get(ENTITY_API_URL_ID, point.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(point.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    void getPointsByIdFiltering() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        Long id = point.getId();

        defaultPointShouldBeFound("id.equals=" + id);
        defaultPointShouldNotBeFound("id.notEquals=" + id);

        defaultPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPointShouldNotBeFound("id.greaterThan=" + id);

        defaultPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPointShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date equals to DEFAULT_DATE
        defaultPointShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the pointList where date equals to UPDATED_DATE
        defaultPointShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date not equals to DEFAULT_DATE
        defaultPointShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the pointList where date not equals to UPDATED_DATE
        defaultPointShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date in DEFAULT_DATE or UPDATED_DATE
        defaultPointShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the pointList where date equals to UPDATED_DATE
        defaultPointShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date is not null
        defaultPointShouldBeFound("date.specified=true");

        // Get all the pointList where date is null
        defaultPointShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllPointsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date is greater than or equal to DEFAULT_DATE
        defaultPointShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the pointList where date is greater than or equal to UPDATED_DATE
        defaultPointShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date is less than or equal to DEFAULT_DATE
        defaultPointShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the pointList where date is less than or equal to SMALLER_DATE
        defaultPointShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date is less than DEFAULT_DATE
        defaultPointShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the pointList where date is less than UPDATED_DATE
        defaultPointShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where date is greater than DEFAULT_DATE
        defaultPointShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the pointList where date is greater than SMALLER_DATE
        defaultPointShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value equals to DEFAULT_VALUE
        defaultPointShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the pointList where value equals to UPDATED_VALUE
        defaultPointShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value not equals to DEFAULT_VALUE
        defaultPointShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the pointList where value not equals to UPDATED_VALUE
        defaultPointShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultPointShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the pointList where value equals to UPDATED_VALUE
        defaultPointShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value is not null
        defaultPointShouldBeFound("value.specified=true");

        // Get all the pointList where value is null
        defaultPointShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllPointsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value is greater than or equal to DEFAULT_VALUE
        defaultPointShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the pointList where value is greater than or equal to UPDATED_VALUE
        defaultPointShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value is less than or equal to DEFAULT_VALUE
        defaultPointShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the pointList where value is less than or equal to SMALLER_VALUE
        defaultPointShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value is less than DEFAULT_VALUE
        defaultPointShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the pointList where value is less than UPDATED_VALUE
        defaultPointShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where value is greater than DEFAULT_VALUE
        defaultPointShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the pointList where value is greater than SMALLER_VALUE
        defaultPointShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllPointsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        point.setUser(user);
        pointRepository.saveAndFlush(point);
        Long userId = user.getId();

        // Get all the pointList where user equals to userId
        defaultPointShouldBeFound("userId.equals=" + userId);

        // Get all the pointList where user equals to (userId + 1)
        defaultPointShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPointShouldBeFound(String filter) throws Exception {
        restPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(point.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));

        // Check, that the count call also returns 1
        restPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPointShouldNotBeFound(String filter) throws Exception {
        restPointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPointMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPoint() throws Exception {
        // Get the point
        restPointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        int databaseSizeBeforeUpdate = pointRepository.findAll().size();

        // Update the point
        Point updatedPoint = pointRepository.findById(point.getId()).get();
        // Disconnect from session so that the updates on updatedPoint are not directly saved in db
        em.detach(updatedPoint);
        updatedPoint.date(UPDATED_DATE).value(UPDATED_VALUE);

        restPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoint))
            )
            .andExpect(status().isOk());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
        Point testPoint = pointList.get(pointList.size() - 1);
        assertThat(testPoint.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPoint.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();
        point.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, point.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(point))
            )
            .andExpect(status().isBadRequest());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();
        point.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(point))
            )
            .andExpect(status().isBadRequest());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();
        point.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(point)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointWithPatch() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        int databaseSizeBeforeUpdate = pointRepository.findAll().size();

        // Update the point using partial update
        Point partialUpdatedPoint = new Point();
        partialUpdatedPoint.setId(point.getId());

        partialUpdatedPoint.value(UPDATED_VALUE);

        restPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoint))
            )
            .andExpect(status().isOk());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
        Point testPoint = pointList.get(pointList.size() - 1);
        assertThat(testPoint.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPoint.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdatePointWithPatch() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        int databaseSizeBeforeUpdate = pointRepository.findAll().size();

        // Update the point using partial update
        Point partialUpdatedPoint = new Point();
        partialUpdatedPoint.setId(point.getId());

        partialUpdatedPoint.date(UPDATED_DATE).value(UPDATED_VALUE);

        restPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoint))
            )
            .andExpect(status().isOk());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
        Point testPoint = pointList.get(pointList.size() - 1);
        assertThat(testPoint.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPoint.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();
        point.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, point.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(point))
            )
            .andExpect(status().isBadRequest());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();
        point.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(point))
            )
            .andExpect(status().isBadRequest());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();
        point.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(point)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        int databaseSizeBeforeDelete = pointRepository.findAll().size();

        // Delete the point
        restPointMockMvc
            .perform(delete(ENTITY_API_URL_ID, point.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
