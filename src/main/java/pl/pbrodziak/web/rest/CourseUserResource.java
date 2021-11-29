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
import pl.pbrodziak.domain.CourseUser;
import pl.pbrodziak.repository.CourseUserRepository;
import pl.pbrodziak.service.CourseUserQueryService;
import pl.pbrodziak.service.CourseUserService;
import pl.pbrodziak.service.criteria.CourseUserCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.CourseUser}.
 */
@RestController
@RequestMapping("/api")
public class CourseUserResource {

    private final Logger log = LoggerFactory.getLogger(CourseUserResource.class);

    private static final String ENTITY_NAME = "courseUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseUserService courseUserService;

    private final CourseUserRepository courseUserRepository;

    private final CourseUserQueryService courseUserQueryService;

    public CourseUserResource(
        CourseUserService courseUserService,
        CourseUserRepository courseUserRepository,
        CourseUserQueryService courseUserQueryService
    ) {
        this.courseUserService = courseUserService;
        this.courseUserRepository = courseUserRepository;
        this.courseUserQueryService = courseUserQueryService;
    }

    /**
     * {@code POST  /course-users} : Create a new courseUser.
     *
     * @param courseUser the courseUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseUser, or with status {@code 400 (Bad Request)} if the courseUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-users")
    public ResponseEntity<CourseUser> createCourseUser(@RequestBody CourseUser courseUser) throws URISyntaxException {
        log.debug("REST request to save CourseUser : {}", courseUser);
        if (courseUser.getId() != null) {
            throw new BadRequestAlertException("A new courseUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseUser result = courseUserService.save(courseUser);
        return ResponseEntity
            .created(new URI("/api/course-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-users/:id} : Updates an existing courseUser.
     *
     * @param id the id of the courseUser to save.
     * @param courseUser the courseUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseUser,
     * or with status {@code 400 (Bad Request)} if the courseUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-users/{id}")
    public ResponseEntity<CourseUser> updateCourseUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseUser courseUser
    ) throws URISyntaxException {
        log.debug("REST request to update CourseUser : {}, {}", id, courseUser);
        if (courseUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseUser result = courseUserService.save(courseUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-users/:id} : Partial updates given fields of an existing courseUser, field will ignore if it is null
     *
     * @param id the id of the courseUser to save.
     * @param courseUser the courseUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseUser,
     * or with status {@code 400 (Bad Request)} if the courseUser is not valid,
     * or with status {@code 404 (Not Found)} if the courseUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseUser> partialUpdateCourseUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseUser courseUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseUser partially : {}, {}", id, courseUser);
        if (courseUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseUser> result = courseUserService.partialUpdate(courseUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseUser.getId().toString())
        );
    }

    /**
     * {@code GET  /course-users} : get all the courseUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseUsers in body.
     */
    @GetMapping("/course-users")
    public ResponseEntity<List<CourseUser>> getAllCourseUsers(CourseUserCriteria criteria) {
        log.debug("REST request to get CourseUsers by criteria: {}", criteria);
        List<CourseUser> entityList = courseUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /course-users/count} : count all the courseUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/course-users/count")
    public ResponseEntity<Long> countCourseUsers(CourseUserCriteria criteria) {
        log.debug("REST request to count CourseUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /course-users/:id} : get the "id" courseUser.
     *
     * @param id the id of the courseUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-users/{id}")
    public ResponseEntity<CourseUser> getCourseUser(@PathVariable Long id) {
        log.debug("REST request to get CourseUser : {}", id);
        Optional<CourseUser> courseUser = courseUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseUser);
    }

    /**
     * {@code DELETE  /course-users/:id} : delete the "id" courseUser.
     *
     * @param id the id of the courseUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-users/{id}")
    public ResponseEntity<Void> deleteCourseUser(@PathVariable Long id) {
        log.debug("REST request to delete CourseUser : {}", id);
        courseUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
