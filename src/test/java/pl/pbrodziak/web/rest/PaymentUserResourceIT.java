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
import pl.pbrodziak.domain.Payment;
import pl.pbrodziak.domain.PaymentUser;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.PaymentUserRepository;
import pl.pbrodziak.service.criteria.PaymentUserCriteria;

/**
 * Integration tests for the {@link PaymentUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentUserResourceIT {

    private static final String ENTITY_API_URL = "/api/payment-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentUserRepository paymentUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentUserMockMvc;

    private PaymentUser paymentUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentUser createEntity(EntityManager em) {
        PaymentUser paymentUser = new PaymentUser();
        return paymentUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentUser createUpdatedEntity(EntityManager em) {
        PaymentUser paymentUser = new PaymentUser();
        return paymentUser;
    }

    @BeforeEach
    public void initTest() {
        paymentUser = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentUser() throws Exception {
        int databaseSizeBeforeCreate = paymentUserRepository.findAll().size();
        // Create the PaymentUser
        restPaymentUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentUser)))
            .andExpect(status().isCreated());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentUser testPaymentUser = paymentUserList.get(paymentUserList.size() - 1);
    }

    @Test
    @Transactional
    void createPaymentUserWithExistingId() throws Exception {
        // Create the PaymentUser with an existing ID
        paymentUser.setId(1L);

        int databaseSizeBeforeCreate = paymentUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentUser)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentUsers() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        // Get all the paymentUserList
        restPaymentUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getPaymentUser() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        // Get the paymentUser
        restPaymentUserMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getPaymentUsersByIdFiltering() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        Long id = paymentUser.getId();

        defaultPaymentUserShouldBeFound("id.equals=" + id);
        defaultPaymentUserShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentUserShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentUsersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        paymentUser.setPayment(payment);
        paymentUserRepository.saveAndFlush(paymentUser);
        Long paymentId = payment.getId();

        // Get all the paymentUserList where payment equals to paymentId
        defaultPaymentUserShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the paymentUserList where payment equals to (paymentId + 1)
        defaultPaymentUserShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        paymentUser.setUser(user);
        paymentUserRepository.saveAndFlush(paymentUser);
        Long userId = user.getId();

        // Get all the paymentUserList where user equals to userId
        defaultPaymentUserShouldBeFound("userId.equals=" + userId);

        // Get all the paymentUserList where user equals to (userId + 1)
        defaultPaymentUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentUserShouldBeFound(String filter) throws Exception {
        restPaymentUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentUser.getId().intValue())));

        // Check, that the count call also returns 1
        restPaymentUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentUserShouldNotBeFound(String filter) throws Exception {
        restPaymentUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentUser() throws Exception {
        // Get the paymentUser
        restPaymentUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentUser() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();

        // Update the paymentUser
        PaymentUser updatedPaymentUser = paymentUserRepository.findById(paymentUser.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentUser are not directly saved in db
        em.detach(updatedPaymentUser);

        restPaymentUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaymentUser))
            )
            .andExpect(status().isOk());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
        PaymentUser testPaymentUser = paymentUserList.get(paymentUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPaymentUser() throws Exception {
        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();
        paymentUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentUser() throws Exception {
        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();
        paymentUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentUser() throws Exception {
        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();
        paymentUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentUserWithPatch() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();

        // Update the paymentUser using partial update
        PaymentUser partialUpdatedPaymentUser = new PaymentUser();
        partialUpdatedPaymentUser.setId(paymentUser.getId());

        restPaymentUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentUser))
            )
            .andExpect(status().isOk());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
        PaymentUser testPaymentUser = paymentUserList.get(paymentUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePaymentUserWithPatch() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();

        // Update the paymentUser using partial update
        PaymentUser partialUpdatedPaymentUser = new PaymentUser();
        partialUpdatedPaymentUser.setId(paymentUser.getId());

        restPaymentUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentUser))
            )
            .andExpect(status().isOk());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
        PaymentUser testPaymentUser = paymentUserList.get(paymentUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentUser() throws Exception {
        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();
        paymentUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentUser() throws Exception {
        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();
        paymentUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentUser() throws Exception {
        int databaseSizeBeforeUpdate = paymentUserRepository.findAll().size();
        paymentUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentUser in the database
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentUser() throws Exception {
        // Initialize the database
        paymentUserRepository.saveAndFlush(paymentUser);

        int databaseSizeBeforeDelete = paymentUserRepository.findAll().size();

        // Delete the paymentUser
        restPaymentUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentUser> paymentUserList = paymentUserRepository.findAll();
        assertThat(paymentUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
