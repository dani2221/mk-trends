package mk.trends.webapp.dtos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SummaryItemResponseDto {

    private String title;

    private Set<String> links;

    private List<String> summary;

    private String photoUrl;

    private int popularity;

    public SummaryItemResponseDto(String title, Set<String> links, List<String> summary, String photoUrl, int popularity) {
        this.title = title;
        this.links = links;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.popularity = popularity;
    }
}
