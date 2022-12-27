package mk.trends.webapp.dtos;

import lombok.Data;
import mk.trends.webapp.model.Category;

import java.util.List;

@Data
public class NewsItemRequestDto {

    private Long id;

    private String title;

    private String source;

    private String text;

    private String photoUrl;

    private String link;

    private Category category;

    private List<Float> bias;

    public NewsItemRequestDto(Long id, String title, String source, String text, String photoUrl, String link, Category category, List<Float> bias) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.text = text;
        this.photoUrl = photoUrl;
        this.link = link;
        this.category = category;
        this.bias = bias;
    }
}
