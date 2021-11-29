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
import pl.pbrodziak.domain.Point;
import pl.pbrodziak.repository.PointRepository;
import pl.pbrodziak.service.PointQueryService;
import pl.pbrodziak.service.PointService;
import pl.pbrodziak.service.criteria.PointCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.Point}.
 */
@RestController
@RequestMapping("/api")
public class PointResource {

    private final Logger log = LoggerFactory.getLogger(PointResource.class);

    private static final String ENTITY_NAME = "point";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointService pointService;

    private final PointRepository pointRepository;

    private final PointQueryService pointQueryService;

    public PointResource(PointService pointService, PointRepository pointRepository, PointQueryService pointQueryService) {
        this.pointService = pointService;
        this.pointRepository = pointRepository;
        this.pointQueryService = pointQueryService;
    }

    /**
     * {@code POST  /points} : Create a new point.
     *
     * @param point the point to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new point, or with status {@code 400 (Bad Request)} if the point has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/points")
    public ResponseEntity<Point> createPoint(@RequestBody Point point) throws URISyntaxException {
        log.debug("REST request to save Point : {}", point);
        if (point.getId() != null) {
            throw new BadRequestAlertException("A new point cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Point result = pointService.save(point);
        return ResponseEntity
            .created(new URI("/api/points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /points/:id} : Updates an existing point.
     *
     * @param id the id of the point to save.
     * @param point the point to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated point,
     * or with status {@code 400 (Bad Request)} if the point is not valid,
     * or with status {@code 500 (Internal Server Error)} if the point couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/points/{id}")
    public ResponseEntity<Point> updatePoint(@PathVariable(value = "id", required = false) final Long id, @RequestBody Point point)
        throws URISyntaxException {
        log.debug("REST request to update Point : {}, {}", id, point);
        if (point.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, point.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Point result = pointService.save(point);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, point.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /points/:id} : Partial updates given fields of an existing point, field will ignore if it is null
     *
     * @param id the id of the point to save.
     * @param point the point to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated point,
     * or with status {@code 400 (Bad Request)} if the point is not valid,
     * or with status {@code 404 (Not Found)} if the point is not found,
     * or with status {@code 500 (Internal Server Error)} if the point couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/points/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Point> partialUpdatePoint(@PathVariable(value = "id", required = false) final Long id, @RequestBody Point point)
        throws URISyntaxException {
        log.debug("REST request to partial update Point partially : {}, {}", id, point);
        if (point.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, point.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Point> result = pointService.partialUpdate(point);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, point.getId().toString())
        );
    }

    /**
     * {@code GET  /points} : get all the points.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of points in body.
     */
    @GetMapping("/points")
    public ResponseEntity<List<Point>> getAllPoints(PointCriteria criteria) {
        log.debug("REST request to get Points by criteria: {}", criteria);
        List<Point> entityList = pointQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /points/count} : count all the points.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/points/count")
    public ResponseEntity<Long> countPoints(PointCriteria criteria) {
        log.debug("REST request to count Points by criteria: {}", criteria);
        return ResponseEntity.ok().body(pointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /points/:id} : get the "id" point.
     *
     * @param id the id of the point to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the point, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/points/{id}")
    public ResponseEntity<Point> getPoint(@PathVariable Long id) {
        log.debug("REST request to get Point : {}", id);
        Optional<Point> point = pointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(point);
    }

    /**
     * {@code DELETE  /points/:id} : delete the "id" point.
     *
     * @param id the id of the point to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/points/{id}")
    public ResponseEntity<Void> deletePoint(@PathVariable Long id) {
        log.debug("REST request to delete Point : {}", id);
        pointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
