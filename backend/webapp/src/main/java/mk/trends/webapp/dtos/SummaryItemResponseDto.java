package mk.trends.webapp.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import mk.trends.webapp.model.Category;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class SummaryItemResponseDto {

    private String title;

    private Set<String> links;

    private List<String> summary;

    private String photoUrl;

    private int popularity;

    private LocalDateTime firstIndexed;

    private LocalDateTime lastIndexed;

    private Category category;

    private List<Float> averageBias;

    private List<NewsItemResponseDto> linkedNews;

    private int timesIndexed;

    public SummaryItemResponseDto(String title, Set<String> links, List<String> summary, String photoUrl, int popularity, LocalDateTime firstIndexed, LocalDateTime lastIndexed, Category category, List<Float> averageBias, List<NewsItemResponseDto> linkedNews, int timesIndexed) {
        this.title = title;
        this.links = links;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.popularity = popularity;
        this.firstIndexed = firstIndexed;
        this.lastIndexed = lastIndexed;
        this.category = category;
        this.averageBias = averageBias;
        this.linkedNews = linkedNews;
        this.timesIndexed = timesIndexed;
    }
}
