package mk.trends.webapp.dtos.mappers;

import mk.trends.webapp.dtos.NewsItemRequestDto;
import mk.trends.webapp.dtos.NewsItemResponseDto;
import mk.trends.webapp.dtos.SummaryItemRequestDto;
import mk.trends.webapp.dtos.SummaryItemResponseDto;
import mk.trends.webapp.model.Category;
import mk.trends.webapp.model.LinkedNewsItems;
import mk.trends.webapp.model.NewsItem;
import mk.trends.webapp.model.SummaryItem;
import mk.trends.webapp.repository.NewsItemRepository;
import mk.trends.webapp.repository.SummaryItemRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SummaryItemMapper implements Mapper<SummaryItem, SummaryItemRequestDto, SummaryItemResponseDto>{

    private final NewsItemRepository newsItemRepository;
    private final SummaryItemRepository summaryItemRepository;
    private final Mapper<NewsItem, NewsItemRequestDto, NewsItemResponseDto> newsItemMapper;

    public SummaryItemMapper(NewsItemRepository newsItemRepository, SummaryItemRepository summaryItemRepository, Mapper<NewsItem, NewsItemRequestDto, NewsItemResponseDto> newsItemMapper) {
        this.newsItemRepository = newsItemRepository;
        this.summaryItemRepository = summaryItemRepository;
        this.newsItemMapper = newsItemMapper;
    }

    @Override
    public SummaryItem toEntity(SummaryItemRequestDto dto) {

        String dtoSummary = String.join("%%", dto.getSummary());
        List<SummaryItem> summaryItems = this.summaryItemRepository
                .findAllByFirstIndexedAfter(LocalDateTime.now().minusWeeks(1));
        HashMap<Long, Long> sameNewsItemsPerSummary = summaryItems.stream()
                .collect(Collectors.toMap(
                        x -> x.getId(),
                        x -> x.getLinkedNewsItems().stream()
                                .flatMap(p -> p.getNewsItems().stream().map(t -> t.getId()))
                                .filter(q -> dto.getLinkedNewsItemIds().contains(q))
                                .count(),
                        Math::addExact,
                        HashMap::new
                ));

        SummaryItem item = null;

        if(sameNewsItemsPerSummary.size() > 0) {
            Long closestMatch = sameNewsItemsPerSummary
                    .entrySet().stream()
                    .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                    .get().getKey();
            if (sameNewsItemsPerSummary.get(closestMatch) > dto.getLinkedNewsItemIds().size() / 2) {
                item = summaryItemRepository.findById(closestMatch).get();
            }
            else{
                SummaryItem possibleItem = summaryItemRepository.findBySummary(dtoSummary);
                if(possibleItem != null){
                    item = possibleItem;
                }
            }
        }

        Set<NewsItem> items = newsItemRepository.findAllByIdIn(dto.getLinkedNewsItemIds());

        Map<Category, Long> majorityCategory = items.stream()
                .collect(Collectors.groupingBy(
                        x -> x.getCategory(),
                        Collectors.counting()
                ));
        Category mostCommonCategory = majorityCategory
                .entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                .get().getKey();
        if(item == null){
            SummaryItem entity = new SummaryItem(
                    dto.getTitle(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    dtoSummary,
                    dto.getPhotoUrl(),
                    items.size(),
                    mostCommonCategory);
            entity.addLinkedNewsItems(new LinkedNewsItems(items.stream().toList(), dtoSummary, entity));
            return entity;
        }
        else{
            item.setTitle(dto.getTitle());
            item.setLastIndexed(LocalDateTime.now());
            item.setPhotoUrl(dto.getPhotoUrl());
            item.setPopularity(items.size());
            item.setCategory(mostCommonCategory);

            List<Long> lastItems = item.getLinkedNewsItems()
                    .get(item.getLinkedNewsItems().size()-1)
                    .getNewsItems().stream().map(x -> x.getId()).toList();
            long sizeSame = items.stream().map(x -> lastItems.contains(x.getId())).filter(x -> x==true).count();
            if(sizeSame != items.size()) {
                item.addLinkedNewsItems(new LinkedNewsItems(items.stream().toList(), dtoSummary, item));
            }
            return item;
        }
    }

    @Override
    public SummaryItemResponseDto toDto(SummaryItem entity) {
        List<NewsItem> newsItems = entity.getLinkedNewsItems()
                .get(entity.getLinkedNewsItems().size()-1)
                .getNewsItems();
        List<List<Float>> biases = newsItems.stream().map(x -> x.getBias())
                .map(x -> x.split("%%")).map(x -> Arrays.stream(x).map(t -> Float.parseFloat(t)).toList()).toList();
        double sumLeft = biases.stream().mapToDouble(x -> x.get(0)).sum();
        double sumNeutral = biases.stream().mapToDouble(x -> x.get(1)).sum();
        double sumRight = biases.stream().mapToDouble(x -> x.get(2)).sum();

        List<Float> averageBiases = new LinkedList<>();
        averageBiases.add((float) (sumLeft/(sumLeft+sumNeutral+sumRight)));
        averageBiases.add((float) (sumNeutral/(sumLeft+sumNeutral+sumRight)));
        averageBiases.add((float) (sumRight/(sumLeft+sumNeutral+sumRight)));

        return new SummaryItemResponseDto(
                entity.getTitle(),
                newsItems
                        .stream().map(NewsItem::getLink)
                        .collect(Collectors.toSet()),
                Arrays.stream(entity.getSummary().split("%%")).toList(),
                entity.getPhotoUrl(),
                entity.getPopularity(),
                entity.getFirstIndexed(),
                entity.getLastIndexed(),
                entity.getCategory(),
                averageBiases,
                newsItems.stream()
                        .map(x -> this.newsItemMapper.toDto(x)).toList(),
                entity.getLinkedNewsItems().size());
    }
}
