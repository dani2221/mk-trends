package mk.trends.webapp.service;

import mk.trends.webapp.dtos.NewsItemDto;

import java.util.List;

public interface NewsItemService {
    List<Long> addBulkItems(List<NewsItemDto> items);
}
