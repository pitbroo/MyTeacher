package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.TaskSolved;

/**
 * Spring Data SQL repository for the TaskSolved entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskSolvedRepository extends JpaRepository<TaskSolved, Long>, JpaSpecificationExecutor<TaskSolved> {
    @Query("select taskSolved from TaskSolved taskSolved where taskSolved.user.login = ?#{principal.username}")
    List<TaskSolved> findByUserIsCurrentUser();
}
