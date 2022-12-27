package mk.trends.webapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "summaryItem")
    private List<LinkedNewsItems> linkedNewsItems;

    @Enumerated(EnumType.STRING)
    private Category category;

    public SummaryItem(String title, LocalDateTime firstIndexed, LocalDateTime lastIndexed, String summary, String photoUrl, int popularity, Category category) {
        this.title = title;
        this.firstIndexed = firstIndexed;
        this.lastIndexed = lastIndexed;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.popularity = popularity;
        this.linkedNewsItems = new ArrayList<>();
        this.category = category;
    }

    public SummaryItem() {

    }
    public void addLinkedNewsItems(LinkedNewsItems linkedNewsItems){
        this.linkedNewsItems.add(linkedNewsItems);
    }
}
