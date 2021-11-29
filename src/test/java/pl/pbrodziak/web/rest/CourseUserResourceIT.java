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
import pl.pbrodziak.domain.Course;
import pl.pbrodziak.domain.CourseUser;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.CourseUserRepository;
import pl.pbrodziak.service.criteria.CourseUserCriteria;

/**
 * Integration tests for the {@link CourseUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseUserResourceIT {

    private static final String ENTITY_API_URL = "/api/course-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseUserMockMvc;

    private CourseUser courseUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseUser createEntity(EntityManager em) {
        CourseUser courseUser = new CourseUser();
        return courseUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseUser createUpdatedEntity(EntityManager em) {
        CourseUser courseUser = new CourseUser();
        return courseUser;
    }

    @BeforeEach
    public void initTest() {
        courseUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseUser() throws Exception {
        int databaseSizeBeforeCreate = courseUserRepository.findAll().size();
        // Create the CourseUser
        restCourseUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseUser)))
            .andExpect(status().isCreated());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeCreate + 1);
        CourseUser testCourseUser = courseUserList.get(courseUserList.size() - 1);
    }

    @Test
    @Transactional
    void createCourseUserWithExistingId() throws Exception {
        // Create the CourseUser with an existing ID
        courseUser.setId(1L);

        int databaseSizeBeforeCreate = courseUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseUser)))
            .andExpect(status().isBadRequest());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCourseUsers() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        // Get all the courseUserList
        restCourseUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getCourseUser() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        // Get the courseUser
        restCourseUserMockMvc
            .perform(get(ENTITY_API_URL_ID, courseUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getCourseUsersByIdFiltering() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        Long id = courseUser.getId();

        defaultCourseUserShouldBeFound("id.equals=" + id);
        defaultCourseUserShouldNotBeFound("id.notEquals=" + id);

        defaultCourseUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseUserShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCourseUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        courseUser.setUser(user);
        courseUserRepository.saveAndFlush(courseUser);
        Long userId = user.getId();

        // Get all the courseUserList where user equals to userId
        defaultCourseUserShouldBeFound("userId.equals=" + userId);

        // Get all the courseUserList where user equals to (userId + 1)
        defaultCourseUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCourseUsersByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        courseUser.setCourse(course);
        courseUserRepository.saveAndFlush(courseUser);
        Long courseId = course.getId();

        // Get all the courseUserList where course equals to courseId
        defaultCourseUserShouldBeFound("courseId.equals=" + courseId);

        // Get all the courseUserList where course equals to (courseId + 1)
        defaultCourseUserShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseUserShouldBeFound(String filter) throws Exception {
        restCourseUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseUser.getId().intValue())));

        // Check, that the count call also returns 1
        restCourseUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseUserShouldNotBeFound(String filter) throws Exception {
        restCourseUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCourseUser() throws Exception {
        // Get the courseUser
        restCourseUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseUser() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();

        // Update the courseUser
        CourseUser updatedCourseUser = courseUserRepository.findById(courseUser.getId()).get();
        // Disconnect from session so that the updates on updatedCourseUser are not directly saved in db
        em.detach(updatedCourseUser);

        restCourseUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseUser))
            )
            .andExpect(status().isOk());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
        CourseUser testCourseUser = courseUserList.get(courseUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCourseUser() throws Exception {
        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();
        courseUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseUser() throws Exception {
        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();
        courseUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseUser() throws Exception {
        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();
        courseUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseUserWithPatch() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();

        // Update the courseUser using partial update
        CourseUser partialUpdatedCourseUser = new CourseUser();
        partialUpdatedCourseUser.setId(courseUser.getId());

        restCourseUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseUser))
            )
            .andExpect(status().isOk());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
        CourseUser testCourseUser = courseUserList.get(courseUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCourseUserWithPatch() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();

        // Update the courseUser using partial update
        CourseUser partialUpdatedCourseUser = new CourseUser();
        partialUpdatedCourseUser.setId(courseUser.getId());

        restCourseUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseUser))
            )
            .andExpect(status().isOk());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
        CourseUser testCourseUser = courseUserList.get(courseUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCourseUser() throws Exception {
        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();
        courseUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseUser() throws Exception {
        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();
        courseUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseUser() throws Exception {
        int databaseSizeBeforeUpdate = courseUserRepository.findAll().size();
        courseUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseUser in the database
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseUser() throws Exception {
        // Initialize the database
        courseUserRepository.saveAndFlush(courseUser);

        int databaseSizeBeforeDelete = courseUserRepository.findAll().size();

        // Delete the courseUser
        restCourseUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseUser> courseUserList = courseUserRepository.findAll();
        assertThat(courseUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
