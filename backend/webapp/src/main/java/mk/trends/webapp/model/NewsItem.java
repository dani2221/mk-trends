package mk.trends.webapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
public class NewsItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 512)
    private String title;

    private String source;

    @Column(columnDefinition="TEXT")
    private String text;

    private LocalDateTime firstIndexed;

    private LocalDateTime lastIndexed;

    @Column(columnDefinition="TEXT")
    private String photoUrl;

    @Column(columnDefinition="TEXT", unique = true)
    private String link;

    @ManyToMany(mappedBy = "newsItems")
    private List<LinkedNewsItems> linkedNewsItems;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String bias;

    public NewsItem(String title, String source, String text, LocalDateTime firstIndexed, LocalDateTime lastIndexed, String photoUrl, String link, Category category, String bias) {
        this.title = title;
        this.source = source;
        this.text = text;
        this.firstIndexed = firstIndexed;
        this.lastIndexed = lastIndexed;
        this.photoUrl = photoUrl;
        this.linkedNewsItems = new LinkedList<>();
        this.link = link;
        this.category = category;
        this.bias = bias;
    }

    public NewsItem() {

    }
}
