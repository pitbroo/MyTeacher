package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.Point;

/**
 * Spring Data SQL repository for the Point entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointRepository extends JpaRepository<Point, Long>, JpaSpecificationExecutor<Point> {
    @Query("select point from Point point where point.user.login = ?#{principal.username}")
    List<Point> findByUserIsCurrentUser();
}
