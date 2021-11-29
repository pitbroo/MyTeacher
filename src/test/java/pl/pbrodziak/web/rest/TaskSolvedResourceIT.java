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
import pl.pbrodziak.domain.Task;
import pl.pbrodziak.domain.TaskSolved;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.TaskSolvedRepository;
import pl.pbrodziak.service.criteria.TaskSolvedCriteria;

/**
 * Integration tests for the {@link TaskSolvedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskSolvedResourceIT {

    private static final Long DEFAULT_POINT_GRADE = 1L;
    private static final Long UPDATED_POINT_GRADE = 2L;
    private static final Long SMALLER_POINT_GRADE = 1L - 1L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEADLINE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_SEND_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SEND_DAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SEND_DAY = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/task-solveds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskSolvedRepository taskSolvedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskSolvedMockMvc;

    private TaskSolved taskSolved;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskSolved createEntity(EntityManager em) {
        TaskSolved taskSolved = new TaskSolved()
            .pointGrade(DEFAULT_POINT_GRADE)
            .content(DEFAULT_CONTENT)
            .deadline(DEFAULT_DEADLINE)
            .sendDay(DEFAULT_SEND_DAY)
            .answer(DEFAULT_ANSWER)
            .attachment(DEFAULT_ATTACHMENT)
            .attachmentContentType(DEFAULT_ATTACHMENT_CONTENT_TYPE);
        return taskSolved;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskSolved createUpdatedEntity(EntityManager em) {
        TaskSolved taskSolved = new TaskSolved()
            .pointGrade(UPDATED_POINT_GRADE)
            .content(UPDATED_CONTENT)
            .deadline(UPDATED_DEADLINE)
            .sendDay(UPDATED_SEND_DAY)
            .answer(UPDATED_ANSWER)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE);
        return taskSolved;
    }

    @BeforeEach
    public void initTest() {
        taskSolved = createEntity(em);
    }

    @Test
    @Transactional
    void createTaskSolved() throws Exception {
        int databaseSizeBeforeCreate = taskSolvedRepository.findAll().size();
        // Create the TaskSolved
        restTaskSolvedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskSolved)))
            .andExpect(status().isCreated());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeCreate + 1);
        TaskSolved testTaskSolved = taskSolvedList.get(taskSolvedList.size() - 1);
        assertThat(testTaskSolved.getPointGrade()).isEqualTo(DEFAULT_POINT_GRADE);
        assertThat(testTaskSolved.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTaskSolved.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testTaskSolved.getSendDay()).isEqualTo(DEFAULT_SEND_DAY);
        assertThat(testTaskSolved.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testTaskSolved.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
        assertThat(testTaskSolved.getAttachmentContentType()).isEqualTo(DEFAULT_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createTaskSolvedWithExistingId() throws Exception {
        // Create the TaskSolved with an existing ID
        taskSolved.setId(1L);

        int databaseSizeBeforeCreate = taskSolvedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskSolvedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskSolved)))
            .andExpect(status().isBadRequest());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaskSolveds() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList
        restTaskSolvedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskSolved.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointGrade").value(hasItem(DEFAULT_POINT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].sendDay").value(hasItem(DEFAULT_SEND_DAY.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))));
    }

    @Test
    @Transactional
    void getTaskSolved() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get the taskSolved
        restTaskSolvedMockMvc
            .perform(get(ENTITY_API_URL_ID, taskSolved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskSolved.getId().intValue()))
            .andExpect(jsonPath("$.pointGrade").value(DEFAULT_POINT_GRADE.intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.sendDay").value(DEFAULT_SEND_DAY.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.attachmentContentType").value(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachment").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENT)));
    }

    @Test
    @Transactional
    void getTaskSolvedsByIdFiltering() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        Long id = taskSolved.getId();

        defaultTaskSolvedShouldBeFound("id.equals=" + id);
        defaultTaskSolvedShouldNotBeFound("id.notEquals=" + id);

        defaultTaskSolvedShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskSolvedShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskSolvedShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskSolvedShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade equals to DEFAULT_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.equals=" + DEFAULT_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade equals to UPDATED_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.equals=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade not equals to DEFAULT_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.notEquals=" + DEFAULT_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade not equals to UPDATED_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.notEquals=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsInShouldWork() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade in DEFAULT_POINT_GRADE or UPDATED_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.in=" + DEFAULT_POINT_GRADE + "," + UPDATED_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade equals to UPDATED_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.in=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade is not null
        defaultTaskSolvedShouldBeFound("pointGrade.specified=true");

        // Get all the taskSolvedList where pointGrade is null
        defaultTaskSolvedShouldNotBeFound("pointGrade.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade is greater than or equal to DEFAULT_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.greaterThanOrEqual=" + DEFAULT_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade is greater than or equal to UPDATED_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.greaterThanOrEqual=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade is less than or equal to DEFAULT_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.lessThanOrEqual=" + DEFAULT_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade is less than or equal to SMALLER_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.lessThanOrEqual=" + SMALLER_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade is less than DEFAULT_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.lessThan=" + DEFAULT_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade is less than UPDATED_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.lessThan=" + UPDATED_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByPointGradeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where pointGrade is greater than DEFAULT_POINT_GRADE
        defaultTaskSolvedShouldNotBeFound("pointGrade.greaterThan=" + DEFAULT_POINT_GRADE);

        // Get all the taskSolvedList where pointGrade is greater than SMALLER_POINT_GRADE
        defaultTaskSolvedShouldBeFound("pointGrade.greaterThan=" + SMALLER_POINT_GRADE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where content equals to DEFAULT_CONTENT
        defaultTaskSolvedShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the taskSolvedList where content equals to UPDATED_CONTENT
        defaultTaskSolvedShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where content not equals to DEFAULT_CONTENT
        defaultTaskSolvedShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the taskSolvedList where content not equals to UPDATED_CONTENT
        defaultTaskSolvedShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultTaskSolvedShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the taskSolvedList where content equals to UPDATED_CONTENT
        defaultTaskSolvedShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where content is not null
        defaultTaskSolvedShouldBeFound("content.specified=true");

        // Get all the taskSolvedList where content is null
        defaultTaskSolvedShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByContentContainsSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where content contains DEFAULT_CONTENT
        defaultTaskSolvedShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the taskSolvedList where content contains UPDATED_CONTENT
        defaultTaskSolvedShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where content does not contain DEFAULT_CONTENT
        defaultTaskSolvedShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the taskSolvedList where content does not contain UPDATED_CONTENT
        defaultTaskSolvedShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline equals to DEFAULT_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.equals=" + DEFAULT_DEADLINE);

        // Get all the taskSolvedList where deadline equals to UPDATED_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.equals=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline not equals to DEFAULT_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.notEquals=" + DEFAULT_DEADLINE);

        // Get all the taskSolvedList where deadline not equals to UPDATED_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.notEquals=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsInShouldWork() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline in DEFAULT_DEADLINE or UPDATED_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.in=" + DEFAULT_DEADLINE + "," + UPDATED_DEADLINE);

        // Get all the taskSolvedList where deadline equals to UPDATED_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.in=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline is not null
        defaultTaskSolvedShouldBeFound("deadline.specified=true");

        // Get all the taskSolvedList where deadline is null
        defaultTaskSolvedShouldNotBeFound("deadline.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline is greater than or equal to DEFAULT_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.greaterThanOrEqual=" + DEFAULT_DEADLINE);

        // Get all the taskSolvedList where deadline is greater than or equal to UPDATED_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.greaterThanOrEqual=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline is less than or equal to DEFAULT_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.lessThanOrEqual=" + DEFAULT_DEADLINE);

        // Get all the taskSolvedList where deadline is less than or equal to SMALLER_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.lessThanOrEqual=" + SMALLER_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsLessThanSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline is less than DEFAULT_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.lessThan=" + DEFAULT_DEADLINE);

        // Get all the taskSolvedList where deadline is less than UPDATED_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.lessThan=" + UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByDeadlineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where deadline is greater than DEFAULT_DEADLINE
        defaultTaskSolvedShouldNotBeFound("deadline.greaterThan=" + DEFAULT_DEADLINE);

        // Get all the taskSolvedList where deadline is greater than SMALLER_DEADLINE
        defaultTaskSolvedShouldBeFound("deadline.greaterThan=" + SMALLER_DEADLINE);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay equals to DEFAULT_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.equals=" + DEFAULT_SEND_DAY);

        // Get all the taskSolvedList where sendDay equals to UPDATED_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.equals=" + UPDATED_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay not equals to DEFAULT_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.notEquals=" + DEFAULT_SEND_DAY);

        // Get all the taskSolvedList where sendDay not equals to UPDATED_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.notEquals=" + UPDATED_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsInShouldWork() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay in DEFAULT_SEND_DAY or UPDATED_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.in=" + DEFAULT_SEND_DAY + "," + UPDATED_SEND_DAY);

        // Get all the taskSolvedList where sendDay equals to UPDATED_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.in=" + UPDATED_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay is not null
        defaultTaskSolvedShouldBeFound("sendDay.specified=true");

        // Get all the taskSolvedList where sendDay is null
        defaultTaskSolvedShouldNotBeFound("sendDay.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay is greater than or equal to DEFAULT_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.greaterThanOrEqual=" + DEFAULT_SEND_DAY);

        // Get all the taskSolvedList where sendDay is greater than or equal to UPDATED_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.greaterThanOrEqual=" + UPDATED_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay is less than or equal to DEFAULT_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.lessThanOrEqual=" + DEFAULT_SEND_DAY);

        // Get all the taskSolvedList where sendDay is less than or equal to SMALLER_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.lessThanOrEqual=" + SMALLER_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsLessThanSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay is less than DEFAULT_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.lessThan=" + DEFAULT_SEND_DAY);

        // Get all the taskSolvedList where sendDay is less than UPDATED_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.lessThan=" + UPDATED_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsBySendDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where sendDay is greater than DEFAULT_SEND_DAY
        defaultTaskSolvedShouldNotBeFound("sendDay.greaterThan=" + DEFAULT_SEND_DAY);

        // Get all the taskSolvedList where sendDay is greater than SMALLER_SEND_DAY
        defaultTaskSolvedShouldBeFound("sendDay.greaterThan=" + SMALLER_SEND_DAY);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where answer equals to DEFAULT_ANSWER
        defaultTaskSolvedShouldBeFound("answer.equals=" + DEFAULT_ANSWER);

        // Get all the taskSolvedList where answer equals to UPDATED_ANSWER
        defaultTaskSolvedShouldNotBeFound("answer.equals=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByAnswerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where answer not equals to DEFAULT_ANSWER
        defaultTaskSolvedShouldNotBeFound("answer.notEquals=" + DEFAULT_ANSWER);

        // Get all the taskSolvedList where answer not equals to UPDATED_ANSWER
        defaultTaskSolvedShouldBeFound("answer.notEquals=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where answer in DEFAULT_ANSWER or UPDATED_ANSWER
        defaultTaskSolvedShouldBeFound("answer.in=" + DEFAULT_ANSWER + "," + UPDATED_ANSWER);

        // Get all the taskSolvedList where answer equals to UPDATED_ANSWER
        defaultTaskSolvedShouldNotBeFound("answer.in=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where answer is not null
        defaultTaskSolvedShouldBeFound("answer.specified=true");

        // Get all the taskSolvedList where answer is null
        defaultTaskSolvedShouldNotBeFound("answer.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByAnswerContainsSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where answer contains DEFAULT_ANSWER
        defaultTaskSolvedShouldBeFound("answer.contains=" + DEFAULT_ANSWER);

        // Get all the taskSolvedList where answer contains UPDATED_ANSWER
        defaultTaskSolvedShouldNotBeFound("answer.contains=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByAnswerNotContainsSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        // Get all the taskSolvedList where answer does not contain DEFAULT_ANSWER
        defaultTaskSolvedShouldNotBeFound("answer.doesNotContain=" + DEFAULT_ANSWER);

        // Get all the taskSolvedList where answer does not contain UPDATED_ANSWER
        defaultTaskSolvedShouldBeFound("answer.doesNotContain=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        taskSolved.setUser(user);
        taskSolvedRepository.saveAndFlush(taskSolved);
        Long userId = user.getId();

        // Get all the taskSolvedList where user equals to userId
        defaultTaskSolvedShouldBeFound("userId.equals=" + userId);

        // Get all the taskSolvedList where user equals to (userId + 1)
        defaultTaskSolvedShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllTaskSolvedsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);
        Task task = TaskResourceIT.createEntity(em);
        em.persist(task);
        em.flush();
        taskSolved.setTask(task);
        taskSolvedRepository.saveAndFlush(taskSolved);
        Long taskId = task.getId();

        // Get all the taskSolvedList where task equals to taskId
        defaultTaskSolvedShouldBeFound("taskId.equals=" + taskId);

        // Get all the taskSolvedList where task equals to (taskId + 1)
        defaultTaskSolvedShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskSolvedShouldBeFound(String filter) throws Exception {
        restTaskSolvedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskSolved.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointGrade").value(hasItem(DEFAULT_POINT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].sendDay").value(hasItem(DEFAULT_SEND_DAY.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))));

        // Check, that the count call also returns 1
        restTaskSolvedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskSolvedShouldNotBeFound(String filter) throws Exception {
        restTaskSolvedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskSolvedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaskSolved() throws Exception {
        // Get the taskSolved
        restTaskSolvedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaskSolved() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();

        // Update the taskSolved
        TaskSolved updatedTaskSolved = taskSolvedRepository.findById(taskSolved.getId()).get();
        // Disconnect from session so that the updates on updatedTaskSolved are not directly saved in db
        em.detach(updatedTaskSolved);
        updatedTaskSolved
            .pointGrade(UPDATED_POINT_GRADE)
            .content(UPDATED_CONTENT)
            .deadline(UPDATED_DEADLINE)
            .sendDay(UPDATED_SEND_DAY)
            .answer(UPDATED_ANSWER)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE);

        restTaskSolvedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaskSolved.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTaskSolved))
            )
            .andExpect(status().isOk());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
        TaskSolved testTaskSolved = taskSolvedList.get(taskSolvedList.size() - 1);
        assertThat(testTaskSolved.getPointGrade()).isEqualTo(UPDATED_POINT_GRADE);
        assertThat(testTaskSolved.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTaskSolved.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testTaskSolved.getSendDay()).isEqualTo(UPDATED_SEND_DAY);
        assertThat(testTaskSolved.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testTaskSolved.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testTaskSolved.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTaskSolved() throws Exception {
        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();
        taskSolved.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskSolvedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskSolved.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskSolved))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskSolved() throws Exception {
        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();
        taskSolved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskSolvedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskSolved))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskSolved() throws Exception {
        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();
        taskSolved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskSolvedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskSolved)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskSolvedWithPatch() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();

        // Update the taskSolved using partial update
        TaskSolved partialUpdatedTaskSolved = new TaskSolved();
        partialUpdatedTaskSolved.setId(taskSolved.getId());

        partialUpdatedTaskSolved.pointGrade(UPDATED_POINT_GRADE).sendDay(UPDATED_SEND_DAY).answer(UPDATED_ANSWER);

        restTaskSolvedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskSolved.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskSolved))
            )
            .andExpect(status().isOk());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
        TaskSolved testTaskSolved = taskSolvedList.get(taskSolvedList.size() - 1);
        assertThat(testTaskSolved.getPointGrade()).isEqualTo(UPDATED_POINT_GRADE);
        assertThat(testTaskSolved.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTaskSolved.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testTaskSolved.getSendDay()).isEqualTo(UPDATED_SEND_DAY);
        assertThat(testTaskSolved.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testTaskSolved.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
        assertThat(testTaskSolved.getAttachmentContentType()).isEqualTo(DEFAULT_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTaskSolvedWithPatch() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();

        // Update the taskSolved using partial update
        TaskSolved partialUpdatedTaskSolved = new TaskSolved();
        partialUpdatedTaskSolved.setId(taskSolved.getId());

        partialUpdatedTaskSolved
            .pointGrade(UPDATED_POINT_GRADE)
            .content(UPDATED_CONTENT)
            .deadline(UPDATED_DEADLINE)
            .sendDay(UPDATED_SEND_DAY)
            .answer(UPDATED_ANSWER)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE);

        restTaskSolvedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskSolved.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskSolved))
            )
            .andExpect(status().isOk());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
        TaskSolved testTaskSolved = taskSolvedList.get(taskSolvedList.size() - 1);
        assertThat(testTaskSolved.getPointGrade()).isEqualTo(UPDATED_POINT_GRADE);
        assertThat(testTaskSolved.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTaskSolved.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testTaskSolved.getSendDay()).isEqualTo(UPDATED_SEND_DAY);
        assertThat(testTaskSolved.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testTaskSolved.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testTaskSolved.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTaskSolved() throws Exception {
        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();
        taskSolved.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskSolvedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskSolved.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskSolved))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskSolved() throws Exception {
        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();
        taskSolved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskSolvedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskSolved))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskSolved() throws Exception {
        int databaseSizeBeforeUpdate = taskSolvedRepository.findAll().size();
        taskSolved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskSolvedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskSolved))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskSolved in the database
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskSolved() throws Exception {
        // Initialize the database
        taskSolvedRepository.saveAndFlush(taskSolved);

        int databaseSizeBeforeDelete = taskSolvedRepository.findAll().size();

        // Delete the taskSolved
        restTaskSolvedMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskSolved.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskSolved> taskSolvedList = taskSolvedRepository.findAll();
        assertThat(taskSolvedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
