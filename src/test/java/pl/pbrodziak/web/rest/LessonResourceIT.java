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
import pl.pbrodziak.domain.Course;
import pl.pbrodziak.domain.Lesson;
import pl.pbrodziak.domain.Task;
import pl.pbrodziak.repository.LessonRepository;
import pl.pbrodziak.service.criteria.LessonCriteria;

/**
 * Integration tests for the {@link LessonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LessonResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_START = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_START = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_END = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CLASSROOM_OR_ADDRES = "AAAAAAAAAA";
    private static final String UPDATED_CLASSROOM_OR_ADDRES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lessons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonMockMvc;

    private Lesson lesson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lesson createEntity(EntityManager em) {
        Lesson lesson = new Lesson()
            .status(DEFAULT_STATUS)
            .dateStart(DEFAULT_DATE_START)
            .dateEnd(DEFAULT_DATE_END)
            .classroomOrAddres(DEFAULT_CLASSROOM_OR_ADDRES);
        return lesson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lesson createUpdatedEntity(EntityManager em) {
        Lesson lesson = new Lesson()
            .status(UPDATED_STATUS)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .classroomOrAddres(UPDATED_CLASSROOM_OR_ADDRES);
        return lesson;
    }

    @BeforeEach
    public void initTest() {
        lesson = createEntity(em);
    }

    @Test
    @Transactional
    void createLesson() throws Exception {
        int databaseSizeBeforeCreate = lessonRepository.findAll().size();
        // Create the Lesson
        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isCreated());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeCreate + 1);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLesson.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testLesson.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testLesson.getClassroomOrAddres()).isEqualTo(DEFAULT_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void createLessonWithExistingId() throws Exception {
        // Create the Lesson with an existing ID
        lesson.setId(1L);

        int databaseSizeBeforeCreate = lessonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLessons() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].classroomOrAddres").value(hasItem(DEFAULT_CLASSROOM_OR_ADDRES)));
    }

    @Test
    @Transactional
    void getLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get the lesson
        restLessonMockMvc
            .perform(get(ENTITY_API_URL_ID, lesson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lesson.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()))
            .andExpect(jsonPath("$.classroomOrAddres").value(DEFAULT_CLASSROOM_OR_ADDRES));
    }

    @Test
    @Transactional
    void getLessonsByIdFiltering() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        Long id = lesson.getId();

        defaultLessonShouldBeFound("id.equals=" + id);
        defaultLessonShouldNotBeFound("id.notEquals=" + id);

        defaultLessonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLessonShouldNotBeFound("id.greaterThan=" + id);

        defaultLessonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLessonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLessonsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where status equals to DEFAULT_STATUS
        defaultLessonShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the lessonList where status equals to UPDATED_STATUS
        defaultLessonShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLessonsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where status not equals to DEFAULT_STATUS
        defaultLessonShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the lessonList where status not equals to UPDATED_STATUS
        defaultLessonShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLessonsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultLessonShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the lessonList where status equals to UPDATED_STATUS
        defaultLessonShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLessonsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where status is not null
        defaultLessonShouldBeFound("status.specified=true");

        // Get all the lessonList where status is null
        defaultLessonShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByStatusContainsSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where status contains DEFAULT_STATUS
        defaultLessonShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the lessonList where status contains UPDATED_STATUS
        defaultLessonShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLessonsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where status does not contain DEFAULT_STATUS
        defaultLessonShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the lessonList where status does not contain UPDATED_STATUS
        defaultLessonShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart equals to DEFAULT_DATE_START
        defaultLessonShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the lessonList where dateStart equals to UPDATED_DATE_START
        defaultLessonShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart not equals to DEFAULT_DATE_START
        defaultLessonShouldNotBeFound("dateStart.notEquals=" + DEFAULT_DATE_START);

        // Get all the lessonList where dateStart not equals to UPDATED_DATE_START
        defaultLessonShouldBeFound("dateStart.notEquals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultLessonShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the lessonList where dateStart equals to UPDATED_DATE_START
        defaultLessonShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart is not null
        defaultLessonShouldBeFound("dateStart.specified=true");

        // Get all the lessonList where dateStart is null
        defaultLessonShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart is greater than or equal to DEFAULT_DATE_START
        defaultLessonShouldBeFound("dateStart.greaterThanOrEqual=" + DEFAULT_DATE_START);

        // Get all the lessonList where dateStart is greater than or equal to UPDATED_DATE_START
        defaultLessonShouldNotBeFound("dateStart.greaterThanOrEqual=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart is less than or equal to DEFAULT_DATE_START
        defaultLessonShouldBeFound("dateStart.lessThanOrEqual=" + DEFAULT_DATE_START);

        // Get all the lessonList where dateStart is less than or equal to SMALLER_DATE_START
        defaultLessonShouldNotBeFound("dateStart.lessThanOrEqual=" + SMALLER_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsLessThanSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart is less than DEFAULT_DATE_START
        defaultLessonShouldNotBeFound("dateStart.lessThan=" + DEFAULT_DATE_START);

        // Get all the lessonList where dateStart is less than UPDATED_DATE_START
        defaultLessonShouldBeFound("dateStart.lessThan=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateStart is greater than DEFAULT_DATE_START
        defaultLessonShouldNotBeFound("dateStart.greaterThan=" + DEFAULT_DATE_START);

        // Get all the lessonList where dateStart is greater than SMALLER_DATE_START
        defaultLessonShouldBeFound("dateStart.greaterThan=" + SMALLER_DATE_START);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd equals to DEFAULT_DATE_END
        defaultLessonShouldBeFound("dateEnd.equals=" + DEFAULT_DATE_END);

        // Get all the lessonList where dateEnd equals to UPDATED_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.equals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd not equals to DEFAULT_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.notEquals=" + DEFAULT_DATE_END);

        // Get all the lessonList where dateEnd not equals to UPDATED_DATE_END
        defaultLessonShouldBeFound("dateEnd.notEquals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsInShouldWork() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd in DEFAULT_DATE_END or UPDATED_DATE_END
        defaultLessonShouldBeFound("dateEnd.in=" + DEFAULT_DATE_END + "," + UPDATED_DATE_END);

        // Get all the lessonList where dateEnd equals to UPDATED_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.in=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd is not null
        defaultLessonShouldBeFound("dateEnd.specified=true");

        // Get all the lessonList where dateEnd is null
        defaultLessonShouldNotBeFound("dateEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd is greater than or equal to DEFAULT_DATE_END
        defaultLessonShouldBeFound("dateEnd.greaterThanOrEqual=" + DEFAULT_DATE_END);

        // Get all the lessonList where dateEnd is greater than or equal to UPDATED_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.greaterThanOrEqual=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd is less than or equal to DEFAULT_DATE_END
        defaultLessonShouldBeFound("dateEnd.lessThanOrEqual=" + DEFAULT_DATE_END);

        // Get all the lessonList where dateEnd is less than or equal to SMALLER_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.lessThanOrEqual=" + SMALLER_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsLessThanSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd is less than DEFAULT_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.lessThan=" + DEFAULT_DATE_END);

        // Get all the lessonList where dateEnd is less than UPDATED_DATE_END
        defaultLessonShouldBeFound("dateEnd.lessThan=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByDateEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where dateEnd is greater than DEFAULT_DATE_END
        defaultLessonShouldNotBeFound("dateEnd.greaterThan=" + DEFAULT_DATE_END);

        // Get all the lessonList where dateEnd is greater than SMALLER_DATE_END
        defaultLessonShouldBeFound("dateEnd.greaterThan=" + SMALLER_DATE_END);
    }

    @Test
    @Transactional
    void getAllLessonsByClassroomOrAddresIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where classroomOrAddres equals to DEFAULT_CLASSROOM_OR_ADDRES
        defaultLessonShouldBeFound("classroomOrAddres.equals=" + DEFAULT_CLASSROOM_OR_ADDRES);

        // Get all the lessonList where classroomOrAddres equals to UPDATED_CLASSROOM_OR_ADDRES
        defaultLessonShouldNotBeFound("classroomOrAddres.equals=" + UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void getAllLessonsByClassroomOrAddresIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where classroomOrAddres not equals to DEFAULT_CLASSROOM_OR_ADDRES
        defaultLessonShouldNotBeFound("classroomOrAddres.notEquals=" + DEFAULT_CLASSROOM_OR_ADDRES);

        // Get all the lessonList where classroomOrAddres not equals to UPDATED_CLASSROOM_OR_ADDRES
        defaultLessonShouldBeFound("classroomOrAddres.notEquals=" + UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void getAllLessonsByClassroomOrAddresIsInShouldWork() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where classroomOrAddres in DEFAULT_CLASSROOM_OR_ADDRES or UPDATED_CLASSROOM_OR_ADDRES
        defaultLessonShouldBeFound("classroomOrAddres.in=" + DEFAULT_CLASSROOM_OR_ADDRES + "," + UPDATED_CLASSROOM_OR_ADDRES);

        // Get all the lessonList where classroomOrAddres equals to UPDATED_CLASSROOM_OR_ADDRES
        defaultLessonShouldNotBeFound("classroomOrAddres.in=" + UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void getAllLessonsByClassroomOrAddresIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where classroomOrAddres is not null
        defaultLessonShouldBeFound("classroomOrAddres.specified=true");

        // Get all the lessonList where classroomOrAddres is null
        defaultLessonShouldNotBeFound("classroomOrAddres.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByClassroomOrAddresContainsSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where classroomOrAddres contains DEFAULT_CLASSROOM_OR_ADDRES
        defaultLessonShouldBeFound("classroomOrAddres.contains=" + DEFAULT_CLASSROOM_OR_ADDRES);

        // Get all the lessonList where classroomOrAddres contains UPDATED_CLASSROOM_OR_ADDRES
        defaultLessonShouldNotBeFound("classroomOrAddres.contains=" + UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void getAllLessonsByClassroomOrAddresNotContainsSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where classroomOrAddres does not contain DEFAULT_CLASSROOM_OR_ADDRES
        defaultLessonShouldNotBeFound("classroomOrAddres.doesNotContain=" + DEFAULT_CLASSROOM_OR_ADDRES);

        // Get all the lessonList where classroomOrAddres does not contain UPDATED_CLASSROOM_OR_ADDRES
        defaultLessonShouldBeFound("classroomOrAddres.doesNotContain=" + UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void getAllLessonsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        lesson.setCourse(course);
        lessonRepository.saveAndFlush(lesson);
        Long courseId = course.getId();

        // Get all the lessonList where course equals to courseId
        defaultLessonShouldBeFound("courseId.equals=" + courseId);

        // Get all the lessonList where course equals to (courseId + 1)
        defaultLessonShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    @Test
    @Transactional
    void getAllLessonsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);
        Task task = TaskResourceIT.createEntity(em);
        em.persist(task);
        em.flush();
        lesson.addTask(task);
        lessonRepository.saveAndFlush(lesson);
        Long taskId = task.getId();

        // Get all the lessonList where task equals to taskId
        defaultLessonShouldBeFound("taskId.equals=" + taskId);

        // Get all the lessonList where task equals to (taskId + 1)
        defaultLessonShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLessonShouldBeFound(String filter) throws Exception {
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].classroomOrAddres").value(hasItem(DEFAULT_CLASSROOM_OR_ADDRES)));

        // Check, that the count call also returns 1
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLessonShouldNotBeFound(String filter) throws Exception {
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLesson() throws Exception {
        // Get the lesson
        restLessonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson
        Lesson updatedLesson = lessonRepository.findById(lesson.getId()).get();
        // Disconnect from session so that the updates on updatedLesson are not directly saved in db
        em.detach(updatedLesson);
        updatedLesson
            .status(UPDATED_STATUS)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .classroomOrAddres(UPDATED_CLASSROOM_OR_ADDRES);

        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLesson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLesson.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testLesson.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testLesson.getClassroomOrAddres()).isEqualTo(UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void putNonExistingLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lesson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonWithPatch() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson using partial update
        Lesson partialUpdatedLesson = new Lesson();
        partialUpdatedLesson.setId(lesson.getId());

        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLesson.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testLesson.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testLesson.getClassroomOrAddres()).isEqualTo(DEFAULT_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void fullUpdateLessonWithPatch() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson using partial update
        Lesson partialUpdatedLesson = new Lesson();
        partialUpdatedLesson.setId(lesson.getId());

        partialUpdatedLesson
            .status(UPDATED_STATUS)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .classroomOrAddres(UPDATED_CLASSROOM_OR_ADDRES);

        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLesson.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testLesson.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testLesson.getClassroomOrAddres()).isEqualTo(UPDATED_CLASSROOM_OR_ADDRES);
    }

    @Test
    @Transactional
    void patchNonExistingLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeDelete = lessonRepository.findAll().size();

        // Delete the lesson
        restLessonMockMvc
            .perform(delete(ENTITY_API_URL_ID, lesson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
