package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.Point;
import pl.pbrodziak.repository.PointRepository;
import pl.pbrodziak.service.PointService;

/**
 * Service Implementation for managing {@link Point}.
 */
@Service
@Transactional
public class PointServiceImpl implements PointService {

    private final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    private final PointRepository pointRepository;

    public PointServiceImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public Point save(Point point) {
        log.debug("Request to save Point : {}", point);
        return pointRepository.save(point);
    }

    @Override
    public Optional<Point> partialUpdate(Point point) {
        log.debug("Request to partially update Point : {}", point);

        return pointRepository
            .findById(point.getId())
            .map(
                existingPoint -> {
                    if (point.getDate() != null) {
                        existingPoint.setDate(point.getDate());
                    }
                    if (point.getValue() != null) {
                        existingPoint.setValue(point.getValue());
                    }

                    return existingPoint;
                }
            )
            .map(pointRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Point> findAll() {
        log.debug("Request to get all Points");
        return pointRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Point> findOne(Long id) {
        log.debug("Request to get Point : {}", id);
        return pointRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Point : {}", id);
        pointRepository.deleteById(id);
    }
}
