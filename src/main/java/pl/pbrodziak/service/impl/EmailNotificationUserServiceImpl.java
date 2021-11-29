package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.EmailNotificationUser;
import pl.pbrodziak.repository.EmailNotificationUserRepository;
import pl.pbrodziak.service.EmailNotificationUserService;

/**
 * Service Implementation for managing {@link EmailNotificationUser}.
 */
@Service
@Transactional
public class EmailNotificationUserServiceImpl implements EmailNotificationUserService {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationUserServiceImpl.class);

    private final EmailNotificationUserRepository emailNotificationUserRepository;

    public EmailNotificationUserServiceImpl(EmailNotificationUserRepository emailNotificationUserRepository) {
        this.emailNotificationUserRepository = emailNotificationUserRepository;
    }

    @Override
    public EmailNotificationUser save(EmailNotificationUser emailNotificationUser) {
        log.debug("Request to save EmailNotificationUser : {}", emailNotificationUser);
        return emailNotificationUserRepository.save(emailNotificationUser);
    }

    @Override
    public Optional<EmailNotificationUser> partialUpdate(EmailNotificationUser emailNotificationUser) {
        log.debug("Request to partially update EmailNotificationUser : {}", emailNotificationUser);

        return emailNotificationUserRepository
            .findById(emailNotificationUser.getId())
            .map(
                existingEmailNotificationUser -> {
                    return existingEmailNotificationUser;
                }
            )
            .map(emailNotificationUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotificationUser> findAll() {
        log.debug("Request to get all EmailNotificationUsers");
        return emailNotificationUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailNotificationUser> findOne(Long id) {
        log.debug("Request to get EmailNotificationUser : {}", id);
        return emailNotificationUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmailNotificationUser : {}", id);
        emailNotificationUserRepository.deleteById(id);
    }
}
