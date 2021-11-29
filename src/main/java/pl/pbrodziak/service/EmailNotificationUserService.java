package pl.pbrodziak.service;

import java.util.List;
import java.util.Optional;
import pl.pbrodziak.domain.EmailNotificationUser;

/**
 * Service Interface for managing {@link EmailNotificationUser}.
 */
public interface EmailNotificationUserService {
    /**
     * Save a emailNotificationUser.
     *
     * @param emailNotificationUser the entity to save.
     * @return the persisted entity.
     */
    EmailNotificationUser save(EmailNotificationUser emailNotificationUser);

    /**
     * Partially updates a emailNotificationUser.
     *
     * @param emailNotificationUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmailNotificationUser> partialUpdate(EmailNotificationUser emailNotificationUser);

    /**
     * Get all the emailNotificationUsers.
     *
     * @return the list of entities.
     */
    List<EmailNotificationUser> findAll();

    /**
     * Get the "id" emailNotificationUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailNotificationUser> findOne(Long id);

    /**
     * Delete the "id" emailNotificationUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
