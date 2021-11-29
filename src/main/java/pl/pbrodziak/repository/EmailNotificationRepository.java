package pl.pbrodziak.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.EmailNotification;

/**
 * Spring Data SQL repository for the EmailNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long>, JpaSpecificationExecutor<EmailNotification> {}
