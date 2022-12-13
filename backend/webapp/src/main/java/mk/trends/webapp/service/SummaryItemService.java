package mk.trends.webapp.service;

import mk.trends.webapp.dtos.SummaryItemRequestDto;
import mk.trends.webapp.dtos.SummaryItemResponseDto;
import mk.trends.webapp.dtos.SummaryPage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SummaryItemService {
    void addBulkSummaryItems(List<SummaryItemRequestDto> items);
    SummaryPage getNews(int page, int size);
    void generateSummaries();
}
