package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.EmailNotification;

/**
 * Service Interface for managing {@link EmailNotification}.
 */
public interface EmailNotificationService {
    /**
     * Save a emailNotification.
     *
     * @param emailNotification the entity to save.
     * @return the persisted entity.
     */
    EmailNotification save(EmailNotification emailNotification);

    /**
     * Partially updates a emailNotification.
     *
     * @param emailNotification the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmailNotification> partialUpdate(EmailNotification emailNotification);

    /**
     * Get all the emailNotifications.
     *
     * @return the list of entities.
     */
    List<EmailNotification> findAll();

    /**
     * Get the "id" emailNotification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailNotification> findOne(Long id);

    /**
     * Delete the "id" emailNotification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
