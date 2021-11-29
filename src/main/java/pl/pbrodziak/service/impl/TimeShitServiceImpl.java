package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.TimeShit;
import pl.pbrodziak.repository.TimeShitRepository;
import pl.pbrodziak.service.TimeShitService;

/**
 * Service Implementation for managing {@link TimeShit}.
 */
@Service
@Transactional
public class TimeShitServiceImpl implements TimeShitService {

    private final Logger log = LoggerFactory.getLogger(TimeShitServiceImpl.class);

    private final TimeShitRepository timeShitRepository;

    public TimeShitServiceImpl(TimeShitRepository timeShitRepository) {
        this.timeShitRepository = timeShitRepository;
    }

    @Override
    public TimeShit save(TimeShit timeShit) {
        log.debug("Request to save TimeShit : {}", timeShit);
        return timeShitRepository.save(timeShit);
    }

    @Override
    public Optional<TimeShit> partialUpdate(TimeShit timeShit) {
        log.debug("Request to partially update TimeShit : {}", timeShit);

        return timeShitRepository
            .findById(timeShit.getId())
            .map(
                existingTimeShit -> {
                    if (timeShit.getPresent() != null) {
                        existingTimeShit.setPresent(timeShit.getPresent());
                    }
                    if (timeShit.getDate() != null) {
                        existingTimeShit.setDate(timeShit.getDate());
                    }

                    return existingTimeShit;
                }
            )
            .map(timeShitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeShit> findAll() {
        log.debug("Request to get all TimeShits");
        return timeShitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeShit> findOne(Long id) {
        log.debug("Request to get TimeShit : {}", id);
        return timeShitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeShit : {}", id);
        timeShitRepository.deleteById(id);
    }
}
