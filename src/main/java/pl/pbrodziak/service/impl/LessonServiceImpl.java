package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.Lesson;
import pl.pbrodziak.repository.LessonRepository;
import pl.pbrodziak.service.LessonService;

/**
 * Service Implementation for managing {@link Lesson}.
 */
@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    private final Logger log = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson save(Lesson lesson) {
        log.debug("Request to save Lesson : {}", lesson);
        return lessonRepository.save(lesson);
    }

    @Override
    public Optional<Lesson> partialUpdate(Lesson lesson) {
        log.debug("Request to partially update Lesson : {}", lesson);

        return lessonRepository
            .findById(lesson.getId())
            .map(
                existingLesson -> {
                    if (lesson.getStatus() != null) {
                        existingLesson.setStatus(lesson.getStatus());
                    }
                    if (lesson.getDateStart() != null) {
                        existingLesson.setDateStart(lesson.getDateStart());
                    }
                    if (lesson.getDateEnd() != null) {
                        existingLesson.setDateEnd(lesson.getDateEnd());
                    }
                    if (lesson.getClassroomOrAddres() != null) {
                        existingLesson.setClassroomOrAddres(lesson.getClassroomOrAddres());
                    }

                    return existingLesson;
                }
            )
            .map(lessonRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> findAll() {
        log.debug("Request to get all Lessons");
        return lessonRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lesson> findOne(Long id) {
        log.debug("Request to get Lesson : {}", id);
        return lessonRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lesson : {}", id);
        lessonRepository.deleteById(id);
    }
}
