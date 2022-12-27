package mk.trends.webapp.service.impl;

import mk.trends.webapp.dtos.NewsItemRequestDto;
import mk.trends.webapp.dtos.NewsItemResponseDto;
import mk.trends.webapp.dtos.mappers.Mapper;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.repository.NewsItemRepository;
import mk.trends.webapp.service.NewsItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsItemServiceImpl implements NewsItemService {
    private final NewsItemRepository newsItemRepository;
    private final Mapper<NewsItem, NewsItemRequestDto, NewsItemResponseDto> newsItemMapper;

    public NewsItemServiceImpl(NewsItemRepository newsItemRepository, Mapper<NewsItem, NewsItemRequestDto, NewsItemResponseDto> newsItemMapper) {
        this.newsItemRepository = newsItemRepository;
        this.newsItemMapper = newsItemMapper;
    }

    @Override
    public List<Long> addBulkItems(List<NewsItemRequestDto> items) {
        List<NewsItem> newsItems = items.stream()
                .map(this.newsItemMapper::toEntity)
                .toList();
        this.newsItemRepository.saveAll(newsItems);
        return newsItems.stream().map(x -> x.getId()).toList();
    }
}
