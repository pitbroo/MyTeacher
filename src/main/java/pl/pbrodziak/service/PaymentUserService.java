package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.PaymentUser;

/**
 * Service Interface for managing {@link PaymentUser}.
 */
public interface PaymentUserService {
    /**
     * Save a paymentUser.
     *
     * @param paymentUser the entity to save.
     * @return the persisted entity.
     */
    PaymentUser save(PaymentUser paymentUser);

    /**
     * Partially updates a paymentUser.
     *
     * @param paymentUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentUser> partialUpdate(PaymentUser paymentUser);

    /**
     * Get all the paymentUsers.
     *
     * @return the list of entities.
     */
    List<PaymentUser> findAll();

    /**
     * Get the "id" paymentUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentUser> findOne(Long id);

    /**
     * Delete the "id" paymentUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
