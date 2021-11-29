package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.PaymentUser;
import pl.pbrodziak.repository.PaymentUserRepository;
import pl.pbrodziak.service.PaymentUserService;

/**
 * Service Implementation for managing {@link PaymentUser}.
 */
@Service
@Transactional
public class PaymentUserServiceImpl implements PaymentUserService {

    private final Logger log = LoggerFactory.getLogger(PaymentUserServiceImpl.class);

    private final PaymentUserRepository paymentUserRepository;

    public PaymentUserServiceImpl(PaymentUserRepository paymentUserRepository) {
        this.paymentUserRepository = paymentUserRepository;
    }

    @Override
    public PaymentUser save(PaymentUser paymentUser) {
        log.debug("Request to save PaymentUser : {}", paymentUser);
        return paymentUserRepository.save(paymentUser);
    }

    @Override
    public Optional<PaymentUser> partialUpdate(PaymentUser paymentUser) {
        log.debug("Request to partially update PaymentUser : {}", paymentUser);

        return paymentUserRepository
            .findById(paymentUser.getId())
            .map(
                existingPaymentUser -> {
                    return existingPaymentUser;
                }
            )
            .map(paymentUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentUser> findAll() {
        log.debug("Request to get all PaymentUsers");
        return paymentUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentUser> findOne(Long id) {
        log.debug("Request to get PaymentUser : {}", id);
        return paymentUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentUser : {}", id);
        paymentUserRepository.deleteById(id);
    }
}
