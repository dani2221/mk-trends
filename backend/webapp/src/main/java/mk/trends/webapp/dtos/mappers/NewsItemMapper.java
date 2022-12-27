package mk.trends.webapp.dtos.mappers;

import mk.trends.webapp.dtos.NewsItemRequestDto;
import mk.trends.webapp.dtos.NewsItemResponseDto;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.repository.NewsItemRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class NewsItemMapper implements Mapper<NewsItem, NewsItemRequestDto, NewsItemResponseDto>{
    private final NewsItemRepository newsItemRepository;

    public NewsItemMapper(NewsItemRepository newsItemRepository) {
        this.newsItemRepository = newsItemRepository;
    }

    @Override
    public NewsItem toEntity(NewsItemRequestDto dto){
        NewsItem oldItem = newsItemRepository
                .findByLink(dto.getLink());

        if(oldItem == null) {
            NewsItem item = new NewsItem(
                    dto.getTitle(),
                    dto.getSource(),
                    dto.getText(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    dto.getPhotoUrl(),
                    dto.getLink(),
                    dto.getCategory(),
                    String.join("%%", dto.getBias().stream().map(x -> Float.toString(x)).toList()));
            return item;
        }
        else{
            oldItem.setText(dto.getText());
            oldItem.setLastIndexed(LocalDateTime.now());
            oldItem.setPhotoUrl(dto.getPhotoUrl());
            return oldItem;
        }
    }

    @Override
    public NewsItemResponseDto toDto(NewsItem entity) {
        NewsItemResponseDto dto = new NewsItemResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getSource(),
                entity.getLink(),
                Arrays.stream(entity.getBias().split("%%")).map(x -> Float.parseFloat(x)).toList());
        return dto;
    }

}
