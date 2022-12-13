package mk.trends.webapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class SummaryItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 512)
    private String title;

    private LocalDateTime firstIndexed;

    private LocalDateTime lastIndexed;

    @Column(columnDefinition="TEXT", unique = true)
    private String summary;

    @Column(columnDefinition="TEXT")
    private String photoUrl;

    private int popularity;

    @ManyToMany()
    private List<NewsItem> linkedNewsItems;

    public SummaryItem(String title, LocalDateTime firstIndexed, LocalDateTime lastIndexed, String summary, String photoUrl, int popularity, List<NewsItem> linkedNewsItems) {
        this.title = title;
        this.firstIndexed = firstIndexed;
        this.lastIndexed = lastIndexed;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.popularity = popularity;
        this.linkedNewsItems = linkedNewsItems;
    }

    public SummaryItem() {

    }
}
