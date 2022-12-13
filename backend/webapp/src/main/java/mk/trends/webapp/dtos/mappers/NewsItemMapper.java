package mk.trends.webapp.dtos.mappers;

import mk.trends.webapp.dtos.NewsItemDto;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.repository.NewsItemRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NewsItemMapper implements Mapper<NewsItem, NewsItemDto, NewsItemDto>{
    private final NewsItemRepository newsItemRepository;

    public NewsItemMapper(NewsItemRepository newsItemRepository) {
        this.newsItemRepository = newsItemRepository;
    }

    @Override
    public NewsItem toEntity(NewsItemDto dto){
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
                    dto.getLink());
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
    public NewsItemDto toDto(NewsItem entity) {
        NewsItemDto dto = new NewsItemDto(
                entity.getId(),
                entity.getTitle(),
                entity.getSource(),
                entity.getText(),
                entity.getPhotoUrl(),
                entity.getLink());
        return dto;
    }

}
