package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.EmailNotificationUser;

/**
 * Spring Data SQL repository for the EmailNotificationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailNotificationUserRepository
    extends JpaRepository<EmailNotificationUser, Long>, JpaSpecificationExecutor<EmailNotificationUser> {
    @Query(
        "select emailNotificationUser from EmailNotificationUser emailNotificationUser where emailNotificationUser.user.login = ?#{principal.username}"
    )
    List<EmailNotificationUser> findByUserIsCurrentUser();
}
