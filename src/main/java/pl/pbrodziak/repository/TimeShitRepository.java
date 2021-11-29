package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.TimeShit;

/**
 * Spring Data SQL repository for the TimeShit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeShitRepository extends JpaRepository<TimeShit, Long>, JpaSpecificationExecutor<TimeShit> {
    @Query("select timeShit from TimeShit timeShit where timeShit.user.login = ?#{principal.username}")
    List<TimeShit> findByUserIsCurrentUser();
}
