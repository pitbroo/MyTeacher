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
import pl.pbrodziak.domain.EmailNotificationUser;
import pl.pbrodziak.repository.EmailNotificationUserRepository;
import pl.pbrodziak.service.EmailNotificationUserQueryService;
import pl.pbrodziak.service.EmailNotificationUserService;
import pl.pbrodziak.service.criteria.EmailNotificationUserCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.EmailNotificationUser}.
 */
@RestController
@RequestMapping("/api")
public class EmailNotificationUserResource {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationUserResource.class);

    private static final String ENTITY_NAME = "emailNotificationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailNotificationUserService emailNotificationUserService;

    private final EmailNotificationUserRepository emailNotificationUserRepository;

    private final EmailNotificationUserQueryService emailNotificationUserQueryService;

    public EmailNotificationUserResource(
        EmailNotificationUserService emailNotificationUserService,
        EmailNotificationUserRepository emailNotificationUserRepository,
        EmailNotificationUserQueryService emailNotificationUserQueryService
    ) {
        this.emailNotificationUserService = emailNotificationUserService;
        this.emailNotificationUserRepository = emailNotificationUserRepository;
        this.emailNotificationUserQueryService = emailNotificationUserQueryService;
    }

    /**
     * {@code POST  /email-notification-users} : Create a new emailNotificationUser.
     *
     * @param emailNotificationUser the emailNotificationUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailNotificationUser, or with status {@code 400 (Bad Request)} if the emailNotificationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/email-notification-users")
    public ResponseEntity<EmailNotificationUser> createEmailNotificationUser(@RequestBody EmailNotificationUser emailNotificationUser)
        throws URISyntaxException {
        log.debug("REST request to save EmailNotificationUser : {}", emailNotificationUser);
        if (emailNotificationUser.getId() != null) {
            throw new BadRequestAlertException("A new emailNotificationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailNotificationUser result = emailNotificationUserService.save(emailNotificationUser);
        return ResponseEntity
            .created(new URI("/api/email-notification-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /email-notification-users/:id} : Updates an existing emailNotificationUser.
     *
     * @param id the id of the emailNotificationUser to save.
     * @param emailNotificationUser the emailNotificationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotificationUser,
     * or with status {@code 400 (Bad Request)} if the emailNotificationUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailNotificationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/email-notification-users/{id}")
    public ResponseEntity<EmailNotificationUser> updateEmailNotificationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailNotificationUser emailNotificationUser
    ) throws URISyntaxException {
        log.debug("REST request to update EmailNotificationUser : {}, {}", id, emailNotificationUser);
        if (emailNotificationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotificationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmailNotificationUser result = emailNotificationUserService.save(emailNotificationUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotificationUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /email-notification-users/:id} : Partial updates given fields of an existing emailNotificationUser, field will ignore if it is null
     *
     * @param id the id of the emailNotificationUser to save.
     * @param emailNotificationUser the emailNotificationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotificationUser,
     * or with status {@code 400 (Bad Request)} if the emailNotificationUser is not valid,
     * or with status {@code 404 (Not Found)} if the emailNotificationUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailNotificationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/email-notification-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EmailNotificationUser> partialUpdateEmailNotificationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailNotificationUser emailNotificationUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmailNotificationUser partially : {}, {}", id, emailNotificationUser);
        if (emailNotificationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotificationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailNotificationUser> result = emailNotificationUserService.partialUpdate(emailNotificationUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotificationUser.getId().toString())
        );
    }

    /**
     * {@code GET  /email-notification-users} : get all the emailNotificationUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailNotificationUsers in body.
     */
    @GetMapping("/email-notification-users")
    public ResponseEntity<List<EmailNotificationUser>> getAllEmailNotificationUsers(EmailNotificationUserCriteria criteria) {
        log.debug("REST request to get EmailNotificationUsers by criteria: {}", criteria);
        List<EmailNotificationUser> entityList = emailNotificationUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /email-notification-users/count} : count all the emailNotificationUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/email-notification-users/count")
    public ResponseEntity<Long> countEmailNotificationUsers(EmailNotificationUserCriteria criteria) {
        log.debug("REST request to count EmailNotificationUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailNotificationUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-notification-users/:id} : get the "id" emailNotificationUser.
     *
     * @param id the id of the emailNotificationUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailNotificationUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/email-notification-users/{id}")
    public ResponseEntity<EmailNotificationUser> getEmailNotificationUser(@PathVariable Long id) {
        log.debug("REST request to get EmailNotificationUser : {}", id);
        Optional<EmailNotificationUser> emailNotificationUser = emailNotificationUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailNotificationUser);
    }

    /**
     * {@code DELETE  /email-notification-users/:id} : delete the "id" emailNotificationUser.
     *
     * @param id the id of the emailNotificationUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/email-notification-users/{id}")
    public ResponseEntity<Void> deleteEmailNotificationUser(@PathVariable Long id) {
        log.debug("REST request to delete EmailNotificationUser : {}", id);
        emailNotificationUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
