package pl.pbrodziak.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pbrodziak.domain.TaskSolved;
import pl.pbrodziak.repository.TaskSolvedRepository;
import pl.pbrodziak.service.TaskSolvedQueryService;
import pl.pbrodziak.service.TaskSolvedService;
import pl.pbrodziak.service.criteria.TaskSolvedCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.TaskSolved}.
 */
@RestController
@RequestMapping("/api")
public class TaskSolvedResource {

    private final Logger log = LoggerFactory.getLogger(TaskSolvedResource.class);

    private static final String ENTITY_NAME = "taskSolved";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskSolvedService taskSolvedService;

    private final TaskSolvedRepository taskSolvedRepository;

    private final TaskSolvedQueryService taskSolvedQueryService;

    public TaskSolvedResource(
        TaskSolvedService taskSolvedService,
        TaskSolvedRepository taskSolvedRepository,
        TaskSolvedQueryService taskSolvedQueryService
    ) {
        this.taskSolvedService = taskSolvedService;
        this.taskSolvedRepository = taskSolvedRepository;
        this.taskSolvedQueryService = taskSolvedQueryService;
    }

    /**
     * {@code POST  /task-solveds} : Create a new taskSolved.
     *
     * @param taskSolved the taskSolved to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskSolved, or with status {@code 400 (Bad Request)} if the taskSolved has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-solveds")
    public ResponseEntity<TaskSolved> createTaskSolved(@RequestBody TaskSolved taskSolved) throws URISyntaxException {
        log.debug("REST request to save TaskSolved : {}", taskSolved);
        if (taskSolved.getId() != null) {
            throw new BadRequestAlertException("A new taskSolved cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskSolved result = taskSolvedService.save(taskSolved);
        return ResponseEntity
            .created(new URI("/api/task-solveds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-solveds/:id} : Updates an existing taskSolved.
     *
     * @param id the id of the taskSolved to save.
     * @param taskSolved the taskSolved to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskSolved,
     * or with status {@code 400 (Bad Request)} if the taskSolved is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskSolved couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-solveds/{id}")
    public ResponseEntity<TaskSolved> updateTaskSolved(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskSolved taskSolved
    ) throws URISyntaxException {
        log.debug("REST request to update TaskSolved : {}, {}", id, taskSolved);
        if (taskSolved.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskSolved.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskSolvedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskSolved result = taskSolvedService.save(taskSolved);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskSolved.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-solveds/:id} : Partial updates given fields of an existing taskSolved, field will ignore if it is null
     *
     * @param id the id of the taskSolved to save.
     * @param taskSolved the taskSolved to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskSolved,
     * or with status {@code 400 (Bad Request)} if the taskSolved is not valid,
     * or with status {@code 404 (Not Found)} if the taskSolved is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskSolved couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/task-solveds/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaskSolved> partialUpdateTaskSolved(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskSolved taskSolved
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskSolved partially : {}, {}", id, taskSolved);
        if (taskSolved.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskSolved.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskSolvedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskSolved> result = taskSolvedService.partialUpdate(taskSolved);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskSolved.getId().toString())
        );
    }

    /**
     * {@code GET  /task-solveds} : get all the taskSolveds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskSolveds in body.
     */
    @GetMapping("/task-solveds")
    public ResponseEntity<List<TaskSolved>> getAllTaskSolveds(TaskSolvedCriteria criteria) {
        log.debug("REST request to get TaskSolveds by criteria: {}", criteria);
        List<TaskSolved> entityList = taskSolvedQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /task-solveds/count} : count all the taskSolveds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/task-solveds/count")
    public ResponseEntity<Long> countTaskSolveds(TaskSolvedCriteria criteria) {
        log.debug("REST request to count TaskSolveds by criteria: {}", criteria);
        return ResponseEntity.ok().body(taskSolvedQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /task-solveds/:id} : get the "id" taskSolved.
     *
     * @param id the id of the taskSolved to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskSolved, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-solveds/{id}")
    public ResponseEntity<TaskSolved> getTaskSolved(@PathVariable Long id) {
        log.debug("REST request to get TaskSolved : {}", id);
        Optional<TaskSolved> taskSolved = taskSolvedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskSolved);
    }

    /**
     * {@code DELETE  /task-solveds/:id} : delete the "id" taskSolved.
     *
     * @param id the id of the taskSolved to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-solveds/{id}")
    public ResponseEntity<Void> deleteTaskSolved(@PathVariable Long id) {
        log.debug("REST request to delete TaskSolved : {}", id);
        taskSolvedService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
