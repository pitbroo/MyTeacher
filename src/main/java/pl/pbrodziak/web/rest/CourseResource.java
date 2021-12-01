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
import pl.pbrodziak.domain.Course;
import pl.pbrodziak.domain.User;
import pl.pbrodziak.repository.CourseRepository;
import pl.pbrodziak.repository.UserRepository;
import pl.pbrodziak.security.SecurityUtils;
import pl.pbrodziak.service.CourseQueryService;
import pl.pbrodziak.service.CourseService;
import pl.pbrodziak.service.UserService;
import pl.pbrodziak.service.criteria.CourseCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.Course}.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseService courseService;

    private final UserService userService;
    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final CourseQueryService courseQueryService;

    public CourseResource(CourseService courseService, UserService userService, UserRepository userRepository, CourseRepository courseRepository, CourseQueryService courseQueryService) {
        this.courseService = courseService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseQueryService = courseQueryService;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Course result = courseService.save(course);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses/:id} : Updates an existing course.
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PutMapping("/courses/{id}")
//    public ResponseEntity<Course> updateCourse(@PathVariable(value = "id", required = false) final Long id, @RequestBody Course course)
//        throws URISyntaxException {
//        log.debug("REST request to update Course : {}, {}", id, course);
//        if (course.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, course.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }ntity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Course result = courseService.save(course);
//
//        if (!courseRepository.existsById(id)) {
//            throw new BadRequestAlertException("E
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, course.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing course, field will ignore if it is null
     *
     * @param id     the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 404 (Not Found)} if the course is not found,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Course> partialUpdateCourse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to partial update Course partially : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Course> result = courseService.partialUpdate(course);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, course.getId().toString())
        );
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(CourseCriteria criteria) {
        log.debug("REST request to get Courses by criteria: {}", criteria);
        List<Course> entityList;
        if (SecurityUtils.getCurrentUserLogin().isPresent() && SecurityUtils.hasCurrentUserThisAuthority("ROLE_TEACHER")) {
            entityList = courseService.findByUserIsCurrentUser();
        }
        else {
            entityList = courseQueryService.findByCriteria(criteria);
        }
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/courses/my")
    public ResponseEntity<List<Course>> getAllMyCourses(CourseCriteria criteria) {
        log.debug("REST request to get My Courses by criteria: {}", criteria);
        List<Course> entityList;
        if (SecurityUtils.getCurrentUserLogin().isPresent() && SecurityUtils.hasCurrentUserThisAuthority("ROLE_USER")) {
            entityList = courseService.findAllUserCoursesByCurrentCourse();
        }
        else {
            entityList = courseService.findAllUserCoursesByCurrentCourse();
        }
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /courses/count} : count all the courses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/courses/count")
    public ResponseEntity<Long> countCourses(CourseCriteria criteria) {
        log.debug("REST request to count Courses by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Optional<Course> course = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(course);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
