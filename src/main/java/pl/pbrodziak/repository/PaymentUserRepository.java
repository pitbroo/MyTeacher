package pl.pbrodziak.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.pbrodziak.domain.PaymentUser;

/**
 * Spring Data SQL repository for the PaymentUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentUserRepository extends JpaRepository<PaymentUser, Long>, JpaSpecificationExecutor<PaymentUser> {
    @Query("select paymentUser from PaymentUser paymentUser where paymentUser.user.login = ?#{principal.username}")
    List<PaymentUser> findByUserIsCurrentUser();
}
