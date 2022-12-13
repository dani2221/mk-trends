package mk.trends.webapp.service.impl;

import mk.trends.webapp.dtos.NewsItemDto;
import mk.trends.webapp.dtos.mappers.Mapper;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.repository.NewsItemRepository;
import mk.trends.webapp.service.NewsItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsItemServiceImpl implements NewsItemService {
    private final NewsItemRepository newsItemRepository;
    private final Mapper<NewsItem, NewsItemDto, NewsItemDto> newsItemMapper;

    public NewsItemServiceImpl(NewsItemRepository newsItemRepository, Mapper<NewsItem, NewsItemDto, NewsItemDto> newsItemMapper) {
        this.newsItemRepository = newsItemRepository;
        this.newsItemMapper = newsItemMapper;
    }

    @Override
    public List<Long> addBulkItems(List<NewsItemDto> items) {
        List<NewsItem> newsItems = items.stream()
                .map(this.newsItemMapper::toEntity)
                .toList();
        this.newsItemRepository.saveAll(newsItems);
        return newsItems.stream().map(x -> x.getId()).toList();
    }
}
