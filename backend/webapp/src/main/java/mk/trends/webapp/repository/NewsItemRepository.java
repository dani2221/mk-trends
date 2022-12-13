package mk.trends.webapp.repository;

import mk.trends.webapp.model.NewsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
    NewsItem findByLink(String link);
    Set<NewsItem> findAllByIdIn(Set<Long> ids);
}
