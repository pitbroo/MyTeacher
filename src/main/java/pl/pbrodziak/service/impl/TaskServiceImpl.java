package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.Task;
import pl.pbrodziak.repository.TaskRepository;
import pl.pbrodziak.service.TaskService;

/**
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        log.debug("Request to save Task : {}", task);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> partialUpdate(Task task) {
        log.debug("Request to partially update Task : {}", task);

        return taskRepository
            .findById(task.getId())
            .map(
                existingTask -> {
                    if (task.getTitle() != null) {
                        existingTask.setTitle(task.getTitle());
                    }
                    if (task.getPointGrade() != null) {
                        existingTask.setPointGrade(task.getPointGrade());
                    }
                    if (task.getContent() != null) {
                        existingTask.setContent(task.getContent());
                    }
                    if (task.getDeadline() != null) {
                        existingTask.setDeadline(task.getDeadline());
                    }
                    if (task.getAttachment() != null) {
                        existingTask.setAttachment(task.getAttachment());
                    }
                    if (task.getAttachmentContentType() != null) {
                        existingTask.setAttachmentContentType(task.getAttachmentContentType());
                    }

                    return existingTask;
                }
            )
            .map(taskRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }
}
