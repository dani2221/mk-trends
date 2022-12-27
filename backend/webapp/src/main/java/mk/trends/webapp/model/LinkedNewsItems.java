package mk.trends.webapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class LinkedNewsItems {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany
    private List<NewsItem> newsItems;

    @ManyToOne(cascade = CascadeType.ALL)
    private SummaryItem summaryItem;

    @Column(columnDefinition="TEXT")
    private String summary;

    private LocalDateTime date;

    public LinkedNewsItems() {
    }

    public LinkedNewsItems(List<NewsItem> linkedNewsItems, String summary, SummaryItem summaryItem) {
        this.newsItems = linkedNewsItems;
        this.date = LocalDateTime.now();
        this.summary = summary;
        this.summaryItem = summaryItem;
    }
}
