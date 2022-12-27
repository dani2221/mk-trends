package mk.trends.webapp.dtos;

import lombok.Data;
import mk.trends.webapp.model.Category;

import java.util.List;

@Data
public class NewsItemResponseDto {
    private Long id;

    private String title;

    private String source;

    private String link;

    private List<Float> bias;

    public NewsItemResponseDto(Long id, String title, String source, String link, List<Float> bias) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.link = link;
        this.bias = bias;
    }
}
