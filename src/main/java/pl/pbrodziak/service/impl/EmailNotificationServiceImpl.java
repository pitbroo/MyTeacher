package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.EmailNotification;
import pl.pbrodziak.repository.EmailNotificationRepository;
import pl.pbrodziak.service.EmailNotificationService;

/**
 * Service Implementation for managing {@link EmailNotification}.
 */
@Service
@Transactional
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    private final EmailNotificationRepository emailNotificationRepository;

    public EmailNotificationServiceImpl(EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }

    @Override
    public EmailNotification save(EmailNotification emailNotification) {
        log.debug("Request to save EmailNotification : {}", emailNotification);
        return emailNotificationRepository.save(emailNotification);
    }

    @Override
    public Optional<EmailNotification> partialUpdate(EmailNotification emailNotification) {
        log.debug("Request to partially update EmailNotification : {}", emailNotification);

        return emailNotificationRepository
            .findById(emailNotification.getId())
            .map(
                existingEmailNotification -> {
                    if (emailNotification.getContent() != null) {
                        existingEmailNotification.setContent(emailNotification.getContent());
                    }
                    if (emailNotification.getTime() != null) {
                        existingEmailNotification.setTime(emailNotification.getTime());
                    }
                    if (emailNotification.getTeacher() != null) {
                        existingEmailNotification.setTeacher(emailNotification.getTeacher());
                    }

                    return existingEmailNotification;
                }
            )
            .map(emailNotificationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailNotification> findAll() {
        log.debug("Request to get all EmailNotifications");
        return emailNotificationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailNotification> findOne(Long id) {
        log.debug("Request to get EmailNotification : {}", id);
        return emailNotificationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmailNotification : {}", id);
        emailNotificationRepository.deleteById(id);
    }
}
