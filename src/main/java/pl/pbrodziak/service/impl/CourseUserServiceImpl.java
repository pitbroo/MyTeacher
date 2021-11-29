package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.CourseUser;
import pl.pbrodziak.repository.CourseUserRepository;
import pl.pbrodziak.service.CourseUserService;

/**
 * Service Implementation for managing {@link CourseUser}.
 */
@Service
@Transactional
public class CourseUserServiceImpl implements CourseUserService {

    private final Logger log = LoggerFactory.getLogger(CourseUserServiceImpl.class);

    private final CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public CourseUser save(CourseUser courseUser) {
        log.debug("Request to save CourseUser : {}", courseUser);
        return courseUserRepository.save(courseUser);
    }

    @Override
    public Optional<CourseUser> partialUpdate(CourseUser courseUser) {
        log.debug("Request to partially update CourseUser : {}", courseUser);

        return courseUserRepository
            .findById(courseUser.getId())
            .map(
                existingCourseUser -> {
                    return existingCourseUser;
                }
            )
            .map(courseUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseUser> findAll() {
        log.debug("Request to get all CourseUsers");
        return courseUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseUser> findOne(Long id) {
        log.debug("Request to get CourseUser : {}", id);
        return courseUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseUser : {}", id);
        courseUserRepository.deleteById(id);
    }
}
