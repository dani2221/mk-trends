package mk.trends.webapp.service.impl;

import mk.trends.webapp.dtos.BiasFeedbackRequestDto;
import mk.trends.webapp.dtos.mappers.Mapper;
import mk.trends.webapp.model.BiasFeedback;
import mk.trends.webapp.repository.BiasFeedbackRepository;
import mk.trends.webapp.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final BiasFeedbackRepository biasFeedbackRepository;
    private final Mapper<BiasFeedback, BiasFeedbackRequestDto, BiasFeedbackRequestDto> biasFeedbackMapper;

    public FeedbackServiceImpl(BiasFeedbackRepository biasFeedbackRepository, Mapper<BiasFeedback, BiasFeedbackRequestDto, BiasFeedbackRequestDto> biasFeedbackMapper) {
        this.biasFeedbackRepository = biasFeedbackRepository;
        this.biasFeedbackMapper = biasFeedbackMapper;
    }

    @Override
    public Long addFeedback(BiasFeedbackRequestDto request) {
        BiasFeedback bf = this.biasFeedbackRepository.save(this.biasFeedbackMapper.toEntity(request));
        return bf.getId();
    }
}
