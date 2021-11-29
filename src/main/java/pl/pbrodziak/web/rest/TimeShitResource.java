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
import pl.pbrodziak.domain.TimeShit;
import pl.pbrodziak.repository.TimeShitRepository;
import pl.pbrodziak.service.TimeShitQueryService;
import pl.pbrodziak.service.TimeShitService;
import pl.pbrodziak.service.criteria.TimeShitCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.TimeShit}.
 */
@RestController
@RequestMapping("/api")
public class TimeShitResource {

    private final Logger log = LoggerFactory.getLogger(TimeShitResource.class);

    private static final String ENTITY_NAME = "timeShit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeShitService timeShitService;

    private final TimeShitRepository timeShitRepository;

    private final TimeShitQueryService timeShitQueryService;

    public TimeShitResource(
        TimeShitService timeShitService,
        TimeShitRepository timeShitRepository,
        TimeShitQueryService timeShitQueryService
    ) {
        this.timeShitService = timeShitService;
        this.timeShitRepository = timeShitRepository;
        this.timeShitQueryService = timeShitQueryService;
    }

    /**
     * {@code POST  /time-shits} : Create a new timeShit.
     *
     * @param timeShit the timeShit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeShit, or with status {@code 400 (Bad Request)} if the timeShit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/time-shits")
    public ResponseEntity<TimeShit> createTimeShit(@RequestBody TimeShit timeShit) throws URISyntaxException {
        log.debug("REST request to save TimeShit : {}", timeShit);
        if (timeShit.getId() != null) {
            throw new BadRequestAlertException("A new timeShit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeShit result = timeShitService.save(timeShit);
        return ResponseEntity
            .created(new URI("/api/time-shits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /time-shits/:id} : Updates an existing timeShit.
     *
     * @param id the id of the timeShit to save.
     * @param timeShit the timeShit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeShit,
     * or with status {@code 400 (Bad Request)} if the timeShit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeShit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-shits/{id}")
    public ResponseEntity<TimeShit> updateTimeShit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeShit timeShit
    ) throws URISyntaxException {
        log.debug("REST request to update TimeShit : {}, {}", id, timeShit);
        if (timeShit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeShit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeShitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TimeShit result = timeShitService.save(timeShit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeShit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /time-shits/:id} : Partial updates given fields of an existing timeShit, field will ignore if it is null
     *
     * @param id the id of the timeShit to save.
     * @param timeShit the timeShit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeShit,
     * or with status {@code 400 (Bad Request)} if the timeShit is not valid,
     * or with status {@code 404 (Not Found)} if the timeShit is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeShit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/time-shits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TimeShit> partialUpdateTimeShit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeShit timeShit
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeShit partially : {}, {}", id, timeShit);
        if (timeShit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeShit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeShitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeShit> result = timeShitService.partialUpdate(timeShit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeShit.getId().toString())
        );
    }

    /**
     * {@code GET  /time-shits} : get all the timeShits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeShits in body.
     */
    @GetMapping("/time-shits")
    public ResponseEntity<List<TimeShit>> getAllTimeShits(TimeShitCriteria criteria) {
        log.debug("REST request to get TimeShits by criteria: {}", criteria);
        List<TimeShit> entityList = timeShitQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /time-shits/count} : count all the timeShits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/time-shits/count")
    public ResponseEntity<Long> countTimeShits(TimeShitCriteria criteria) {
        log.debug("REST request to count TimeShits by criteria: {}", criteria);
        return ResponseEntity.ok().body(timeShitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /time-shits/:id} : get the "id" timeShit.
     *
     * @param id the id of the timeShit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeShit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-shits/{id}")
    public ResponseEntity<TimeShit> getTimeShit(@PathVariable Long id) {
        log.debug("REST request to get TimeShit : {}", id);
        Optional<TimeShit> timeShit = timeShitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeShit);
    }

    /**
     * {@code DELETE  /time-shits/:id} : delete the "id" timeShit.
     *
     * @param id the id of the timeShit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/time-shits/{id}")
    public ResponseEntity<Void> deleteTimeShit(@PathVariable Long id) {
        log.debug("REST request to delete TimeShit : {}", id);
        timeShitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
