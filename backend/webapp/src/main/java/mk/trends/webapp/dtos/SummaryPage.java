package mk.trends.webapp.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SummaryPage {
    private List<SummaryItemResponseDto> items;
    private boolean hasMore;

    public SummaryPage(List<SummaryItemResponseDto> items, boolean hasMore) {
        this.items = items;
        this.hasMore = hasMore;
    }
}
