package mk.trends.webapp.dtos.mappers;

import mk.trends.webapp.dtos.SummaryItemRequestDto;
import mk.trends.webapp.dtos.SummaryItemResponseDto;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.model.SummaryItem;
import mk.trends.webapp.repository.NewsItemRepository;
import mk.trends.webapp.repository.SummaryItemRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SummaryItemMapper implements Mapper<SummaryItem, SummaryItemRequestDto, SummaryItemResponseDto>{

    private final NewsItemRepository newsItemRepository;
    private final SummaryItemRepository summaryItemRepository;

    public SummaryItemMapper(NewsItemRepository newsItemRepository, SummaryItemRepository summaryItemRepository) {
        this.newsItemRepository = newsItemRepository;
        this.summaryItemRepository = summaryItemRepository;
    }

    @Override
    public SummaryItem toEntity(SummaryItemRequestDto dto) {

        String dtoSummary = dto.getSummary().stream().collect(Collectors.joining("%%"));
        SummaryItem item = this.summaryItemRepository.findBySummary(dtoSummary);

        Set<NewsItem> items = newsItemRepository.findAllByIdIn(dto.getLinkedNewsItemIds());
        if(item == null){
            SummaryItem entity = new SummaryItem(
                    dto.getTitle(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    dtoSummary,
                    dto.getPhotoUrl(),
                    items.size(),
                    items.stream().toList());
            return entity;
        }
        else{
            item.setTitle(dto.getTitle());
            item.setLastIndexed(LocalDateTime.now());
            item.setPhotoUrl(dto.getPhotoUrl());
            item.setPopularity(items.size());

            return item;
        }
    }

    @Override
    public SummaryItemResponseDto toDto(SummaryItem entity) {
        SummaryItemResponseDto item = new SummaryItemResponseDto(
                entity.getTitle(),
                entity.getLinkedNewsItems().stream().map(x -> x.getLink()).collect(Collectors.toSet()),
                Arrays.stream(entity.getSummary().split("%%")).toList(),
                entity.getPhotoUrl(),
                entity.getPopularity());
        return item;
    }
}
