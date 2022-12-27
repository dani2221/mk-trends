package mk.trends.webapp.service;

import mk.trends.webapp.dtos.NewsItemRequestDto;

import java.util.List;

public interface NewsItemService {
    List<Long> addBulkItems(List<NewsItemRequestDto> items);
}
