package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.TaskSolved;
import pl.pbrodziak.repository.TaskSolvedRepository;
import pl.pbrodziak.service.TaskSolvedService;

/**
 * Service Implementation for managing {@link TaskSolved}.
 */
@Service
@Transactional
public class TaskSolvedServiceImpl implements TaskSolvedService {

    private final Logger log = LoggerFactory.getLogger(TaskSolvedServiceImpl.class);

    private final TaskSolvedRepository taskSolvedRepository;

    public TaskSolvedServiceImpl(TaskSolvedRepository taskSolvedRepository) {
        this.taskSolvedRepository = taskSolvedRepository;
    }

    @Override
    public TaskSolved save(TaskSolved taskSolved) {
        log.debug("Request to save TaskSolved : {}", taskSolved);
        return taskSolvedRepository.save(taskSolved);
    }

    @Override
    public Optional<TaskSolved> partialUpdate(TaskSolved taskSolved) {
        log.debug("Request to partially update TaskSolved : {}", taskSolved);

        return taskSolvedRepository
            .findById(taskSolved.getId())
            .map(
                existingTaskSolved -> {
                    if (taskSolved.getPointGrade() != null) {
                        existingTaskSolved.setPointGrade(taskSolved.getPointGrade());
                    }
                    if (taskSolved.getContent() != null) {
                        existingTaskSolved.setContent(taskSolved.getContent());
                    }
                    if (taskSolved.getDeadline() != null) {
                        existingTaskSolved.setDeadline(taskSolved.getDeadline());
                    }
                    if (taskSolved.getSendDay() != null) {
                        existingTaskSolved.setSendDay(taskSolved.getSendDay());
                    }
                    if (taskSolved.getAnswer() != null) {
                        existingTaskSolved.setAnswer(taskSolved.getAnswer());
                    }
                    if (taskSolved.getAttachment() != null) {
                        existingTaskSolved.setAttachment(taskSolved.getAttachment());
                    }
                    if (taskSolved.getAttachmentContentType() != null) {
                        existingTaskSolved.setAttachmentContentType(taskSolved.getAttachmentContentType());
                    }

                    return existingTaskSolved;
                }
            )
            .map(taskSolvedRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskSolved> findAll() {
        log.debug("Request to get all TaskSolveds");
        return taskSolvedRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskSolved> findOne(Long id) {
        log.debug("Request to get TaskSolved : {}", id);
        return taskSolvedRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskSolved : {}", id);
        taskSolvedRepository.deleteById(id);
    }
}
