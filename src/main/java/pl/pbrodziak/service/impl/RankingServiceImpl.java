package pl.pbrodziak.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pbrodziak.domain.Ranking;
import pl.pbrodziak.repository.RankingRepository;
import pl.pbrodziak.service.RankingService;

/**
 * Service Implementation for managing {@link Ranking}.
 */
@Service
@Transactional
public class RankingServiceImpl implements RankingService {

    private final Logger log = LoggerFactory.getLogger(RankingServiceImpl.class);

    private final RankingRepository rankingRepository;

    public RankingServiceImpl(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    @Override
    public Ranking save(Ranking ranking) {
        log.debug("Request to save Ranking : {}", ranking);
        return rankingRepository.save(ranking);
    }

    @Override
    public Optional<Ranking> partialUpdate(Ranking ranking) {
        log.debug("Request to partially update Ranking : {}", ranking);

        return rankingRepository
            .findById(ranking.getId())
            .map(
                existingRanking -> {
                    if (ranking.getPoints() != null) {
                        existingRanking.setPoints(ranking.getPoints());
                    }

                    return existingRanking;
                }
            )
            .map(rankingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ranking> findAll() {
        log.debug("Request to get all Rankings");
        return rankingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ranking> findOne(Long id) {
        log.debug("Request to get Ranking : {}", id);
        return rankingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ranking : {}", id);
        rankingRepository.deleteById(id);
    }
}
