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
import pl.pbrodziak.domain.PaymentUser;
import pl.pbrodziak.repository.PaymentUserRepository;
import pl.pbrodziak.service.PaymentUserQueryService;
import pl.pbrodziak.service.PaymentUserService;
import pl.pbrodziak.service.criteria.PaymentUserCriteria;
import pl.pbrodziak.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.pbrodziak.domain.PaymentUser}.
 */
@RestController
@RequestMapping("/api")
public class PaymentUserResource {

    private final Logger log = LoggerFactory.getLogger(PaymentUserResource.class);

    private static final String ENTITY_NAME = "paymentUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentUserService paymentUserService;

    private final PaymentUserRepository paymentUserRepository;

    private final PaymentUserQueryService paymentUserQueryService;

    public PaymentUserResource(
        PaymentUserService paymentUserService,
        PaymentUserRepository paymentUserRepository,
        PaymentUserQueryService paymentUserQueryService
    ) {
        this.paymentUserService = paymentUserService;
        this.paymentUserRepository = paymentUserRepository;
        this.paymentUserQueryService = paymentUserQueryService;
    }

    /**
     * {@code POST  /payment-users} : Create a new paymentUser.
     *
     * @param paymentUser the paymentUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentUser, or with status {@code 400 (Bad Request)} if the paymentUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-users")
    public ResponseEntity<PaymentUser> createPaymentUser(@RequestBody PaymentUser paymentUser) throws URISyntaxException {
        log.debug("REST request to save PaymentUser : {}", paymentUser);
        if (paymentUser.getId() != null) {
            throw new BadRequestAlertException("A new paymentUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentUser result = paymentUserService.save(paymentUser);
        return ResponseEntity
            .created(new URI("/api/payment-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-users/:id} : Updates an existing paymentUser.
     *
     * @param id the id of the paymentUser to save.
     * @param paymentUser the paymentUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentUser,
     * or with status {@code 400 (Bad Request)} if the paymentUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-users/{id}")
    public ResponseEntity<PaymentUser> updatePaymentUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentUser paymentUser
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentUser : {}, {}", id, paymentUser);
        if (paymentUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentUser result = paymentUserService.save(paymentUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-users/:id} : Partial updates given fields of an existing paymentUser, field will ignore if it is null
     *
     * @param id the id of the paymentUser to save.
     * @param paymentUser the paymentUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentUser,
     * or with status {@code 400 (Bad Request)} if the paymentUser is not valid,
     * or with status {@code 404 (Not Found)} if the paymentUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PaymentUser> partialUpdatePaymentUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentUser paymentUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentUser partially : {}, {}", id, paymentUser);
        if (paymentUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentUser> result = paymentUserService.partialUpdate(paymentUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentUser.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-users} : get all the paymentUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentUsers in body.
     */
    @GetMapping("/payment-users")
    public ResponseEntity<List<PaymentUser>> getAllPaymentUsers(PaymentUserCriteria criteria) {
        log.debug("REST request to get PaymentUsers by criteria: {}", criteria);
        List<PaymentUser> entityList = paymentUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /payment-users/count} : count all the paymentUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-users/count")
    public ResponseEntity<Long> countPaymentUsers(PaymentUserCriteria criteria) {
        log.debug("REST request to count PaymentUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-users/:id} : get the "id" paymentUser.
     *
     * @param id the id of the paymentUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-users/{id}")
    public ResponseEntity<PaymentUser> getPaymentUser(@PathVariable Long id) {
        log.debug("REST request to get PaymentUser : {}", id);
        Optional<PaymentUser> paymentUser = paymentUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentUser);
    }

    /**
     * {@code DELETE  /payment-users/:id} : delete the "id" paymentUser.
     *
     * @param id the id of the paymentUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-users/{id}")
    public ResponseEntity<Void> deletePaymentUser(@PathVariable Long id) {
        log.debug("REST request to delete PaymentUser : {}", id);
        paymentUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
