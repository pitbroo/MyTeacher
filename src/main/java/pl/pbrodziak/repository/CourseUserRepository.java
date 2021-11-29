package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.CourseUser;

/**
 * Spring Data SQL repository for the CourseUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long>, JpaSpecificationExecutor<CourseUser> {
    @Query("select courseUser from CourseUser courseUser where courseUser.user.login = ?#{principal.username}")
    List<CourseUser> findByUserIsCurrentUser();
}
