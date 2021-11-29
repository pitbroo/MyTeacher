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
import pl.pbrodziak.domain.EmailNotification;
import pl.pbrodziak.domain.EmailNotificationUser;
import pl.pbrodziak.repository.EmailNotificationRepository;
import pl.pbrodziak.service.criteria.EmailNotificationCriteria;

/**
 * Integration tests for the {@link EmailNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailNotificationResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TIME = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TEACHER = "AAAAAAAAAA";
    private static final String UPDATED_TEACHER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmailNotificationRepository emailNotificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailNotificationMockMvc;

    private EmailNotification emailNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailNotification createEntity(EntityManager em) {
        EmailNotification emailNotification = new EmailNotification().content(DEFAULT_CONTENT).time(DEFAULT_TIME).teacher(DEFAULT_TEACHER);
        return emailNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailNotification createUpdatedEntity(EntityManager em) {
        EmailNotification emailNotification = new EmailNotification().content(UPDATED_CONTENT).time(UPDATED_TIME).teacher(UPDATED_TEACHER);
        return emailNotification;
    }

    @BeforeEach
    public void initTest() {
        emailNotification = createEntity(em);
    }

    @Test
    @Transactional
    void createEmailNotification() throws Exception {
        int databaseSizeBeforeCreate = emailNotificationRepository.findAll().size();
        // Create the EmailNotification
        restEmailNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isCreated());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        EmailNotification testEmailNotification = emailNotificationList.get(emailNotificationList.size() - 1);
        assertThat(testEmailNotification.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEmailNotification.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testEmailNotification.getTeacher()).isEqualTo(DEFAULT_TEACHER);
    }

    @Test
    @Transactional
    void createEmailNotificationWithExistingId() throws Exception {
        // Create the EmailNotification with an existing ID
        emailNotification.setId(1L);

        int databaseSizeBeforeCreate = emailNotificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmailNotifications() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList
        restEmailNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].teacher").value(hasItem(DEFAULT_TEACHER)));
    }

    @Test
    @Transactional
    void getEmailNotification() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get the emailNotification
        restEmailNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, emailNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailNotification.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.teacher").value(DEFAULT_TEACHER));
    }

    @Test
    @Transactional
    void getEmailNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        Long id = emailNotification.getId();

        defaultEmailNotificationShouldBeFound("id.equals=" + id);
        defaultEmailNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultEmailNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmailNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultEmailNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmailNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where content equals to DEFAULT_CONTENT
        defaultEmailNotificationShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the emailNotificationList where content equals to UPDATED_CONTENT
        defaultEmailNotificationShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where content not equals to DEFAULT_CONTENT
        defaultEmailNotificationShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the emailNotificationList where content not equals to UPDATED_CONTENT
        defaultEmailNotificationShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultEmailNotificationShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the emailNotificationList where content equals to UPDATED_CONTENT
        defaultEmailNotificationShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where content is not null
        defaultEmailNotificationShouldBeFound("content.specified=true");

        // Get all the emailNotificationList where content is null
        defaultEmailNotificationShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByContentContainsSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where content contains DEFAULT_CONTENT
        defaultEmailNotificationShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the emailNotificationList where content contains UPDATED_CONTENT
        defaultEmailNotificationShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where content does not contain DEFAULT_CONTENT
        defaultEmailNotificationShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the emailNotificationList where content does not contain UPDATED_CONTENT
        defaultEmailNotificationShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time equals to DEFAULT_TIME
        defaultEmailNotificationShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the emailNotificationList where time equals to UPDATED_TIME
        defaultEmailNotificationShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time not equals to DEFAULT_TIME
        defaultEmailNotificationShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the emailNotificationList where time not equals to UPDATED_TIME
        defaultEmailNotificationShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time in DEFAULT_TIME or UPDATED_TIME
        defaultEmailNotificationShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the emailNotificationList where time equals to UPDATED_TIME
        defaultEmailNotificationShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time is not null
        defaultEmailNotificationShouldBeFound("time.specified=true");

        // Get all the emailNotificationList where time is null
        defaultEmailNotificationShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time is greater than or equal to DEFAULT_TIME
        defaultEmailNotificationShouldBeFound("time.greaterThanOrEqual=" + DEFAULT_TIME);

        // Get all the emailNotificationList where time is greater than or equal to UPDATED_TIME
        defaultEmailNotificationShouldNotBeFound("time.greaterThanOrEqual=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time is less than or equal to DEFAULT_TIME
        defaultEmailNotificationShouldBeFound("time.lessThanOrEqual=" + DEFAULT_TIME);

        // Get all the emailNotificationList where time is less than or equal to SMALLER_TIME
        defaultEmailNotificationShouldNotBeFound("time.lessThanOrEqual=" + SMALLER_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time is less than DEFAULT_TIME
        defaultEmailNotificationShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the emailNotificationList where time is less than UPDATED_TIME
        defaultEmailNotificationShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where time is greater than DEFAULT_TIME
        defaultEmailNotificationShouldNotBeFound("time.greaterThan=" + DEFAULT_TIME);

        // Get all the emailNotificationList where time is greater than SMALLER_TIME
        defaultEmailNotificationShouldBeFound("time.greaterThan=" + SMALLER_TIME);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where teacher equals to DEFAULT_TEACHER
        defaultEmailNotificationShouldBeFound("teacher.equals=" + DEFAULT_TEACHER);

        // Get all the emailNotificationList where teacher equals to UPDATED_TEACHER
        defaultEmailNotificationShouldNotBeFound("teacher.equals=" + UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTeacherIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where teacher not equals to DEFAULT_TEACHER
        defaultEmailNotificationShouldNotBeFound("teacher.notEquals=" + DEFAULT_TEACHER);

        // Get all the emailNotificationList where teacher not equals to UPDATED_TEACHER
        defaultEmailNotificationShouldBeFound("teacher.notEquals=" + UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTeacherIsInShouldWork() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where teacher in DEFAULT_TEACHER or UPDATED_TEACHER
        defaultEmailNotificationShouldBeFound("teacher.in=" + DEFAULT_TEACHER + "," + UPDATED_TEACHER);

        // Get all the emailNotificationList where teacher equals to UPDATED_TEACHER
        defaultEmailNotificationShouldNotBeFound("teacher.in=" + UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTeacherIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where teacher is not null
        defaultEmailNotificationShouldBeFound("teacher.specified=true");

        // Get all the emailNotificationList where teacher is null
        defaultEmailNotificationShouldNotBeFound("teacher.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTeacherContainsSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where teacher contains DEFAULT_TEACHER
        defaultEmailNotificationShouldBeFound("teacher.contains=" + DEFAULT_TEACHER);

        // Get all the emailNotificationList where teacher contains UPDATED_TEACHER
        defaultEmailNotificationShouldNotBeFound("teacher.contains=" + UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByTeacherNotContainsSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        // Get all the emailNotificationList where teacher does not contain DEFAULT_TEACHER
        defaultEmailNotificationShouldNotBeFound("teacher.doesNotContain=" + DEFAULT_TEACHER);

        // Get all the emailNotificationList where teacher does not contain UPDATED_TEACHER
        defaultEmailNotificationShouldBeFound("teacher.doesNotContain=" + UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void getAllEmailNotificationsByEmailNotificationUserIsEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);
        EmailNotificationUser emailNotificationUser = EmailNotificationUserResourceIT.createEntity(em);
        em.persist(emailNotificationUser);
        em.flush();
        emailNotification.addEmailNotificationUser(emailNotificationUser);
        emailNotificationRepository.saveAndFlush(emailNotification);
        Long emailNotificationUserId = emailNotificationUser.getId();

        // Get all the emailNotificationList where emailNotificationUser equals to emailNotificationUserId
        defaultEmailNotificationShouldBeFound("emailNotificationUserId.equals=" + emailNotificationUserId);

        // Get all the emailNotificationList where emailNotificationUser equals to (emailNotificationUserId + 1)
        defaultEmailNotificationShouldNotBeFound("emailNotificationUserId.equals=" + (emailNotificationUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailNotificationShouldBeFound(String filter) throws Exception {
        restEmailNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].teacher").value(hasItem(DEFAULT_TEACHER)));

        // Check, that the count call also returns 1
        restEmailNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailNotificationShouldNotBeFound(String filter) throws Exception {
        restEmailNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailNotification() throws Exception {
        // Get the emailNotification
        restEmailNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmailNotification() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();

        // Update the emailNotification
        EmailNotification updatedEmailNotification = emailNotificationRepository.findById(emailNotification.getId()).get();
        // Disconnect from session so that the updates on updatedEmailNotification are not directly saved in db
        em.detach(updatedEmailNotification);
        updatedEmailNotification.content(UPDATED_CONTENT).time(UPDATED_TIME).teacher(UPDATED_TEACHER);

        restEmailNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmailNotification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmailNotification))
            )
            .andExpect(status().isOk());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
        EmailNotification testEmailNotification = emailNotificationList.get(emailNotificationList.size() - 1);
        assertThat(testEmailNotification.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEmailNotification.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testEmailNotification.getTeacher()).isEqualTo(UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void putNonExistingEmailNotification() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        emailNotification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailNotification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailNotification() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        emailNotification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailNotification() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        emailNotification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailNotificationWithPatch() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();

        // Update the emailNotification using partial update
        EmailNotification partialUpdatedEmailNotification = new EmailNotification();
        partialUpdatedEmailNotification.setId(emailNotification.getId());

        partialUpdatedEmailNotification.time(UPDATED_TIME);

        restEmailNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailNotification))
            )
            .andExpect(status().isOk());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
        EmailNotification testEmailNotification = emailNotificationList.get(emailNotificationList.size() - 1);
        assertThat(testEmailNotification.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEmailNotification.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testEmailNotification.getTeacher()).isEqualTo(DEFAULT_TEACHER);
    }

    @Test
    @Transactional
    void fullUpdateEmailNotificationWithPatch() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();

        // Update the emailNotification using partial update
        EmailNotification partialUpdatedEmailNotification = new EmailNotification();
        partialUpdatedEmailNotification.setId(emailNotification.getId());

        partialUpdatedEmailNotification.content(UPDATED_CONTENT).time(UPDATED_TIME).teacher(UPDATED_TEACHER);

        restEmailNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailNotification))
            )
            .andExpect(status().isOk());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
        EmailNotification testEmailNotification = emailNotificationList.get(emailNotificationList.size() - 1);
        assertThat(testEmailNotification.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEmailNotification.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testEmailNotification.getTeacher()).isEqualTo(UPDATED_TEACHER);
    }

    @Test
    @Transactional
    void patchNonExistingEmailNotification() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        emailNotification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailNotification() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        emailNotification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailNotification() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        emailNotification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailNotification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailNotification in the database
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailNotification() throws Exception {
        // Initialize the database
        emailNotificationRepository.saveAndFlush(emailNotification);

        int databaseSizeBeforeDelete = emailNotificationRepository.findAll().size();

        // Delete the emailNotification
        restEmailNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailNotification> emailNotificationList = emailNotificationRepository.findAll();
        assertThat(emailNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
