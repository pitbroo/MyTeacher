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
import pl.pbrodziak.domain.EmailNotification;
import pl.pbrodziak.domain.EmailNotificationUser;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.EmailNotificationUserRepository;
import pl.pbrodziak.service.criteria.EmailNotificationUserCriteria;

/**
 * Integration tests for the {@link EmailNotificationUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailNotificationUserResourceIT {

    private static final String ENTITY_API_URL = "/api/email-notification-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmailNotificationUserRepository emailNotificationUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailNotificationUserMockMvc;

    private EmailNotificationUser emailNotificationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailNotificationUser createEntity(EntityManager em) {
        EmailNotificationUser emailNotificationUser = new EmailNotificationUser();
        return emailNotificationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailNotificationUser createUpdatedEntity(EntityManager em) {
        EmailNotificationUser emailNotificationUser = new EmailNotificationUser();
        return emailNotificationUser;
    }

    @BeforeEach
    public void initTest() {
        emailNotificationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createEmailNotificationUser() throws Exception {
        int databaseSizeBeforeCreate = emailNotificationUserRepository.findAll().size();
        // Create the EmailNotificationUser
        restEmailNotificationUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isCreated());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeCreate + 1);
        EmailNotificationUser testEmailNotificationUser = emailNotificationUserList.get(emailNotificationUserList.size() - 1);
    }

    @Test
    @Transactional
    void createEmailNotificationUserWithExistingId() throws Exception {
        // Create the EmailNotificationUser with an existing ID
        emailNotificationUser.setId(1L);

        int databaseSizeBeforeCreate = emailNotificationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailNotificationUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmailNotificationUsers() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        // Get all the emailNotificationUserList
        restEmailNotificationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailNotificationUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getEmailNotificationUser() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        // Get the emailNotificationUser
        restEmailNotificationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, emailNotificationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailNotificationUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getEmailNotificationUsersByIdFiltering() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        Long id = emailNotificationUser.getId();

        defaultEmailNotificationUserShouldBeFound("id.equals=" + id);
        defaultEmailNotificationUserShouldNotBeFound("id.notEquals=" + id);

        defaultEmailNotificationUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmailNotificationUserShouldNotBeFound("id.greaterThan=" + id);

        defaultEmailNotificationUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmailNotificationUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailNotificationUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        emailNotificationUser.setUser(user);
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);
        Long userId = user.getId();

        // Get all the emailNotificationUserList where user equals to userId
        defaultEmailNotificationUserShouldBeFound("userId.equals=" + userId);

        // Get all the emailNotificationUserList where user equals to (userId + 1)
        defaultEmailNotificationUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllEmailNotificationUsersByEmailNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);
        EmailNotification emailNotification = EmailNotificationResourceIT.createEntity(em);
        em.persist(emailNotification);
        em.flush();
        emailNotificationUser.setEmailNotification(emailNotification);
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);
        Long emailNotificationId = emailNotification.getId();

        // Get all the emailNotificationUserList where emailNotification equals to emailNotificationId
        defaultEmailNotificationUserShouldBeFound("emailNotificationId.equals=" + emailNotificationId);

        // Get all the emailNotificationUserList where emailNotification equals to (emailNotificationId + 1)
        defaultEmailNotificationUserShouldNotBeFound("emailNotificationId.equals=" + (emailNotificationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailNotificationUserShouldBeFound(String filter) throws Exception {
        restEmailNotificationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailNotificationUser.getId().intValue())));

        // Check, that the count call also returns 1
        restEmailNotificationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailNotificationUserShouldNotBeFound(String filter) throws Exception {
        restEmailNotificationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailNotificationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailNotificationUser() throws Exception {
        // Get the emailNotificationUser
        restEmailNotificationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmailNotificationUser() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();

        // Update the emailNotificationUser
        EmailNotificationUser updatedEmailNotificationUser = emailNotificationUserRepository.findById(emailNotificationUser.getId()).get();
        // Disconnect from session so that the updates on updatedEmailNotificationUser are not directly saved in db
        em.detach(updatedEmailNotificationUser);

        restEmailNotificationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmailNotificationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmailNotificationUser))
            )
            .andExpect(status().isOk());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
        EmailNotificationUser testEmailNotificationUser = emailNotificationUserList.get(emailNotificationUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingEmailNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();
        emailNotificationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailNotificationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailNotificationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();
        emailNotificationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();
        emailNotificationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailNotificationUserWithPatch() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();

        // Update the emailNotificationUser using partial update
        EmailNotificationUser partialUpdatedEmailNotificationUser = new EmailNotificationUser();
        partialUpdatedEmailNotificationUser.setId(emailNotificationUser.getId());

        restEmailNotificationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailNotificationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailNotificationUser))
            )
            .andExpect(status().isOk());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
        EmailNotificationUser testEmailNotificationUser = emailNotificationUserList.get(emailNotificationUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateEmailNotificationUserWithPatch() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();

        // Update the emailNotificationUser using partial update
        EmailNotificationUser partialUpdatedEmailNotificationUser = new EmailNotificationUser();
        partialUpdatedEmailNotificationUser.setId(emailNotificationUser.getId());

        restEmailNotificationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailNotificationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailNotificationUser))
            )
            .andExpect(status().isOk());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
        EmailNotificationUser testEmailNotificationUser = emailNotificationUserList.get(emailNotificationUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingEmailNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();
        emailNotificationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailNotificationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailNotificationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();
        emailNotificationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = emailNotificationUserRepository.findAll().size();
        emailNotificationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailNotificationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailNotificationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailNotificationUser in the database
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailNotificationUser() throws Exception {
        // Initialize the database
        emailNotificationUserRepository.saveAndFlush(emailNotificationUser);

        int databaseSizeBeforeDelete = emailNotificationUserRepository.findAll().size();

        // Delete the emailNotificationUser
        restEmailNotificationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailNotificationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailNotificationUser> emailNotificationUserList = emailNotificationUserRepository.findAll();
        assertThat(emailNotificationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
