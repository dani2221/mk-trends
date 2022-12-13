package mk.trends.webapp.dtos;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsItemDto {

    private Long id;

    private String title;

    private String source;

    private String text;

    private String photoUrl;

    private String link;

    public NewsItemDto(Long id, String title, String source, String text, String photoUrl, String link) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.text = text;
        this.photoUrl = photoUrl;
        this.link = link;
    }
}
