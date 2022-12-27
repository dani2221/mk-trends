package mk.trends.webapp.dtos.mappers;

import mk.trends.webapp.dtos.BiasFeedbackRequestDto;
import mk.trends.webapp.model.BiasFeedback;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.repository.NewsItemRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BiasFeedbackMapper implements Mapper<BiasFeedback, BiasFeedbackRequestDto, BiasFeedbackRequestDto>{

    private final NewsItemRepository newsItemRepository;

    public BiasFeedbackMapper(NewsItemRepository newsItemRepository) {
        this.newsItemRepository = newsItemRepository;
    }

    @Override
    public BiasFeedback toEntity(BiasFeedbackRequestDto dto) {
        NewsItem item = newsItemRepository.findById(dto.getNewsItemId()).orElseThrow();
        BiasFeedback bf = new BiasFeedback(
                dto.getIpAddress(),
                item,
                dto.getSuggestedBias(),
                dto.getComment());
        return bf;
    }

    @Override
    public BiasFeedbackRequestDto toDto(BiasFeedback entity) {
        return null;
    }
}
