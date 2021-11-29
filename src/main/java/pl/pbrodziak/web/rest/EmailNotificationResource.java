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
import pl.pbrodziak.domain.EmailNotification;
import pl.pbrodziak.repository.EmailNotificationRepository;
import pl.pbrodziak.service.EmailNotificationQueryService;
import pl.pbrodziak.service.EmailNotificationService;
import pl.pbrodziak.service.criteria.EmailNotificationCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.EmailNotification}.
 */
@RestController
@RequestMapping("/api")
public class EmailNotificationResource {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationResource.class);

    private static final String ENTITY_NAME = "emailNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailNotificationService emailNotificationService;

    private final EmailNotificationRepository emailNotificationRepository;

    private final EmailNotificationQueryService emailNotificationQueryService;

    public EmailNotificationResource(
        EmailNotificationService emailNotificationService,
        EmailNotificationRepository emailNotificationRepository,
        EmailNotificationQueryService emailNotificationQueryService
    ) {
        this.emailNotificationService = emailNotificationService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.emailNotificationQueryService = emailNotificationQueryService;
    }

    /**
     * {@code POST  /email-notifications} : Create a new emailNotification.
     *
     * @param emailNotification the emailNotification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailNotification, or with status {@code 400 (Bad Request)} if the emailNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/email-notifications")
    public ResponseEntity<EmailNotification> createEmailNotification(@RequestBody EmailNotification emailNotification)
        throws URISyntaxException {
        log.debug("REST request to save EmailNotification : {}", emailNotification);
        if (emailNotification.getId() != null) {
            throw new BadRequestAlertException("A new emailNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailNotification result = emailNotificationService.save(emailNotification);
        return ResponseEntity
            .created(new URI("/api/email-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /email-notifications/:id} : Updates an existing emailNotification.
     *
     * @param id the id of the emailNotification to save.
     * @param emailNotification the emailNotification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotification,
     * or with status {@code 400 (Bad Request)} if the emailNotification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailNotification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/email-notifications/{id}")
    public ResponseEntity<EmailNotification> updateEmailNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailNotification emailNotification
    ) throws URISyntaxException {
        log.debug("REST request to update EmailNotification : {}, {}", id, emailNotification);
        if (emailNotification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmailNotification result = emailNotificationService.save(emailNotification);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotification.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /email-notifications/:id} : Partial updates given fields of an existing emailNotification, field will ignore if it is null
     *
     * @param id the id of the emailNotification to save.
     * @param emailNotification the emailNotification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotification,
     * or with status {@code 400 (Bad Request)} if the emailNotification is not valid,
     * or with status {@code 404 (Not Found)} if the emailNotification is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailNotification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/email-notifications/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EmailNotification> partialUpdateEmailNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailNotification emailNotification
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmailNotification partially : {}, {}", id, emailNotification);
        if (emailNotification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailNotification> result = emailNotificationService.partialUpdate(emailNotification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotification.getId().toString())
        );
    }

    /**
     * {@code GET  /email-notifications} : get all the emailNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailNotifications in body.
     */
    @GetMapping("/email-notifications")
    public ResponseEntity<List<EmailNotification>> getAllEmailNotifications(EmailNotificationCriteria criteria) {
        log.debug("REST request to get EmailNotifications by criteria: {}", criteria);
        List<EmailNotification> entityList = emailNotificationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /email-notifications/count} : count all the emailNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/email-notifications/count")
    public ResponseEntity<Long> countEmailNotifications(EmailNotificationCriteria criteria) {
        log.debug("REST request to count EmailNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-notifications/:id} : get the "id" emailNotification.
     *
     * @param id the id of the emailNotification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailNotification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/email-notifications/{id}")
    public ResponseEntity<EmailNotification> getEmailNotification(@PathVariable Long id) {
        log.debug("REST request to get EmailNotification : {}", id);
        Optional<EmailNotification> emailNotification = emailNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailNotification);
    }

    /**
     * {@code DELETE  /email-notifications/:id} : delete the "id" emailNotification.
     *
     * @param id the id of the emailNotification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/email-notifications/{id}")
    public ResponseEntity<Void> deleteEmailNotification(@PathVariable Long id) {
        log.debug("REST request to delete EmailNotification : {}", id);
        emailNotificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
