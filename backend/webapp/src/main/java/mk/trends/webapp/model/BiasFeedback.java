package mk.trends.webapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BiasFeedback {

    @Id
    @GeneratedValue()
    private long id;

    private String ipAddress;

    private String comment;

    @ManyToOne
    private NewsItem newsItem;

    @Enumerated(EnumType.STRING)
    private Bias suggestedBias;

    public BiasFeedback() {
    }

    public BiasFeedback(String ipAddress, NewsItem newsItem, Bias suggestedBias, String comment) {
        this.ipAddress = ipAddress;
        this.newsItem = newsItem;
        this.suggestedBias = suggestedBias;
        this.comment = comment;
    }
}
