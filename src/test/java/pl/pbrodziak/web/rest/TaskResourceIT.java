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
import org.springframework.util.Base64Utils;
import pl.pbrodziak.IntegrationTest;
import pl.pbrodziak.domain.Lesson;
import pl.pbrodziak.domain.Task;
import pl.pbrodziak.domain.TaskSolved;
import pl.pbrodziak.repository.TaskRepository;
import pl.pbrodziak.service.criteria.TaskCriteria;

/**
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_POINT_GRADE = 1L;
    private static final Long UPDATED_POINT_GRADE = 2L;
    private static final Long SMALLER_POINT_GRADE = 1L - 1L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEADLINE = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .title(DEFAULT_TITLE)
            .pointGrade(DEFAULT_POINT_GRADE)
            .content(DEFAULT_CONTENT)
            .deadline(DEFAULT_DEADLINE)
            .attachment(DEFAULT_ATTACHMENT)
            .attachmentContentType(DEFAULT_ATTACHMENT_CONTENT_TYPE);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .title(UPDATED_TITLE)
            .pointGrade(UPDATED_POINT_GRADE)
            .content(UPDATED_CONTENT)
            .deadline(UPDATED_DEADLINE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        // Create the Task
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTask.getPointGrade()).isEqualTo(DEFAULT_POINT_GRADE);
        assertThat(testTask.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTask.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testTask.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
        assertThat(testTask.getAttachmentContentType()).isEqualTo(DEFAULT_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);

        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].pointGrade").value(hasItem(DEFAULT_POINT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.pointGrade").value(DEFAULT_POINT_GRADE.intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.attachmentContentType").value(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachment").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENT)));
    }

    @Test
    @Transactional
    void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where title equals to DEFAULT_TITLE
        defaultTaskShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the taskList where title equals to UPDATED_TITLE
        defaultTaskShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where title not equals to DEFAULT_TITLE
        defaultTaskShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the taskList where title not equals to UPDATED_TITLE
        defaultTaskShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTaskShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the taskList where title equals to UPDATED_TITLE
        defaultTaskShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where title is not null
        defaultTaskShouldBeFound("title.specified=true");

        // Get all the taskList where title is null
        defaultTaskShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTitleContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where title contains DEFAULT_TITLE
        defaultTaskShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the taskList where title contains UPDATED_TITLE
        defaultTaskShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where title does not contain DEFAULT_TITLE
        defaultTaskShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the taskList where title does not contain UPDATED_TITLE
        defaultTaskShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade equals to DEFAULT_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.equals=" + DEFAULT_POINT_GRADE);

        // Get all the taskList where pointGrade equals to UPDATED_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.equals=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade not equals to DEFAULT_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.notEquals=" + DEFAULT_POINT_GRADE);

        // Get all the taskList where pointGrade not equals to UPDATED_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.notEquals=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade in DEFAULT_POINT_GRADE or UPDATED_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.in=" + DEFAULT_POINT_GRADE + "," + UPDATED_POINT_GRADE);

        // Get all the taskList where pointGrade equals to UPDATED_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.in=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade is not null
        defaultTaskShouldBeFound("pointGrade.specified=true");

        // Get all the taskList where pointGrade is null
        defaultTaskShouldNotBeFound("pointGrade.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade is greater than or equal to DEFAULT_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.greaterThanOrEqual=" + DEFAULT_POINT_GRADE);

        // Get all the taskList where pointGrade is greater than or equal to UPDATED_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.greaterThanOrEqual=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade is less than or equal to DEFAULT_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.lessThanOrEqual=" + DEFAULT_POINT_GRADE);

        // Get all the taskList where pointGrade is less than or equal to SMALLER_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.lessThanOrEqual=" + SMALLER_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade is less than DEFAULT_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.lessThan=" + DEFAULT_POINT_GRADE);

        // Get all the taskList where pointGrade is less than UPDATED_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.lessThan=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByPointGradeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where pointGrade is greater than DEFAULT_POINT_GRADE
        defaultTaskShouldNotBeFound("pointGrade.greaterThan=" + DEFAULT_POINT_GRADE);

        // Get all the taskList where pointGrade is greater than SMALLER_POINT_GRADE
        defaultTaskShouldBeFound("pointGrade.greaterThan=" + SMALLER_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTasksByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where content equals to DEFAULT_CONTENT
        defaultTaskShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the taskList where content equals to UPDATED_CONTENT
        defaultTaskShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTasksByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where content not equals to DEFAULT_CONTENT
        defaultTaskShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the taskList where content not equals to UPDATED_CONTENT
        defaultTaskShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTasksByContentIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultTaskShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the taskList where content equals to UPDATED_CONTENT
        defaultTaskShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTasksByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where content is not null
        defaultTaskShouldBeFound("content.specified=true");

        // Get all the taskList where content is null
        defaultTaskShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByContentContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where content contains DEFAULT_CONTENT
        defaultTaskShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the taskList where content contains UPDATED_CONTENT
        defaultTaskShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTasksByContentNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where content does not contain DEFAULT_CONTENT
        defaultTaskShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the taskList where content does not contain UPDATED_CONTENT
        defaultTaskShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline equals to DEFAULT_DEADLINE
        defaultTaskShouldBeFound("deadline.equals=" + DEFAULT_DEADLINE);

        // Get all the taskList where deadline equals to UPDATED_DEADLINE
        defaultTaskShouldNotBeFound("deadline.equals=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline not equals to DEFAULT_DEADLINE
        defaultTaskShouldNotBeFound("deadline.notEquals=" + DEFAULT_DEADLINE);

        // Get all the taskList where deadline not equals to UPDATED_DEADLINE
        defaultTaskShouldBeFound("deadline.notEquals=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline in DEFAULT_DEADLINE or UPDATED_DEADLINE
        defaultTaskShouldBeFound("deadline.in=" + DEFAULT_DEADLINE + "," + UPDATED_DEADLINE);

        // Get all the taskList where deadline equals to UPDATED_DEADLINE
        defaultTaskShouldNotBeFound("deadline.in=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline is not null
        defaultTaskShouldBeFound("deadline.specified=true");

        // Get all the taskList where deadline is null
        defaultTaskShouldNotBeFound("deadline.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline is greater than or equal to DEFAULT_DEADLINE
        defaultTaskShouldBeFound("deadline.greaterThanOrEqual=" + DEFAULT_DEADLINE);

        // Get all the taskList where deadline is greater than or equal to UPDATED_DEADLINE
        defaultTaskShouldNotBeFound("deadline.greaterThanOrEqual=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline is less than or equal to DEFAULT_DEADLINE
        defaultTaskShouldBeFound("deadline.lessThanOrEqual=" + DEFAULT_DEADLINE);

        // Get all the taskList where deadline is less than or equal to SMALLER_DEADLINE
        defaultTaskShouldNotBeFound("deadline.lessThanOrEqual=" + SMALLER_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline is less than DEFAULT_DEADLINE
        defaultTaskShouldNotBeFound("deadline.lessThan=" + DEFAULT_DEADLINE);

        // Get all the taskList where deadline is less than UPDATED_DEADLINE
        defaultTaskShouldBeFound("deadline.lessThan=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByDeadlineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where deadline is greater than DEFAULT_DEADLINE
        defaultTaskShouldNotBeFound("deadline.greaterThan=" + DEFAULT_DEADLINE);

        // Get all the taskList where deadline is greater than SMALLER_DEADLINE
        defaultTaskShouldBeFound("deadline.greaterThan=" + SMALLER_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTasksByLessonIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        Lesson lesson = LessonResourceIT.createEntity(em);
        em.persist(lesson);
        em.flush();
        task.setLesson(lesson);
        taskRepository.saveAndFlush(task);
        Long lessonId = lesson.getId();

        // Get all the taskList where lesson equals to lessonId
        defaultTaskShouldBeFound("lessonId.equals=" + lessonId);

        // Get all the taskList where lesson equals to (lessonId + 1)
        defaultTaskShouldNotBeFound("lessonId.equals=" + (lessonId + 1));
    }

    @Test
    @Transactional
    void getAllTasksByTaskSolvedIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        TaskSolved taskSolved = TaskSolvedResourceIT.createEntity(em);
        em.persist(taskSolved);
        em.flush();
        task.addTaskSolved(taskSolved);
        taskRepository.saveAndFlush(task);
        Long taskSolvedId = taskSolved.getId();

        // Get all the taskList where taskSolved equals to taskSolvedId
        defaultTaskShouldBeFound("taskSolvedId.equals=" + taskSolvedId);

        // Get all the taskList where taskSolved equals to (taskSolvedId + 1)
        defaultTaskShouldNotBeFound("taskSolvedId.equals=" + (taskSolvedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].pointGrade").value(hasItem(DEFAULT_POINT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))));

        // Check, that the count call also returns 1
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .title(UPDATED_TITLE)
            .pointGrade(UPDATED_POINT_GRADE)
            .content(UPDATED_CONTENT)
            .deadline(UPDATED_DEADLINE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE);

        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTask.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTask.getPointGrade()).isEqualTo(UPDATED_POINT_GRADE);
        assertThat(testTask.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTask.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testTask.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testTask.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, task.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask.pointGrade(UPDATED_POINT_GRADE).deadline(UPDATED_DEADLINE);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTask.getPointGrade()).isEqualTo(UPDATED_POINT_GRADE);
        assertThat(testTask.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTask.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testTask.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
        assertThat(testTask.getAttachmentContentType()).isEqualTo(DEFAULT_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .title(UPDATED_TITLE)
            .pointGrade(UPDATED_POINT_GRADE)
            .content(UPDATED_CONTENT)
            .deadline(UPDATED_DEADLINE)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTask.getPointGrade()).isEqualTo(UPDATED_POINT_GRADE);
        assertThat(testTask.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTask.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testTask.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testTask.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, task.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
