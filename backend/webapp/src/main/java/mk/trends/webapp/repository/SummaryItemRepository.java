package mk.trends.webapp.repository;

import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.model.SummaryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SummaryItemRepository extends JpaRepository<SummaryItem, Long> {
    Page<SummaryItem> findAllByLastIndexedAfter(LocalDateTime dateCreated, Pageable pageable);
    List<SummaryItem> findFirstByOrderByLastIndexedDesc();
    SummaryItem findBySummary(String summary);
}
