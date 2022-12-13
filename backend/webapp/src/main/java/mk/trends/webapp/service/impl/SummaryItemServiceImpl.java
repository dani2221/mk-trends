package mk.trends.webapp.service.impl;

import mk.trends.webapp.dtos.NewsItemDto;
import mk.trends.webapp.dtos.SummaryItemRequestDto;
import mk.trends.webapp.dtos.SummaryItemResponseDto;
import mk.trends.webapp.dtos.SummaryPage;
import mk.trends.webapp.dtos.mappers.Mapper;
import mk.trends.webapp.model.SummaryItem;
import mk.trends.webapp.repository.SummaryItemRepository;
import mk.trends.webapp.service.SummaryItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

@Service
public class SummaryItemServiceImpl implements SummaryItemService {
    private final SummaryItemRepository summaryItemRepository;
    private final Mapper<SummaryItem, SummaryItemRequestDto, SummaryItemResponseDto> summaryItemMapper;

    @Value("${vars.algorithms_url}")
    private String algorithmsUrl;

    public SummaryItemServiceImpl(SummaryItemRepository summaryItemRepository, Mapper<SummaryItem, SummaryItemRequestDto, SummaryItemResponseDto> summaryItemMapper) {
        this.summaryItemRepository = summaryItemRepository;
        this.summaryItemMapper = summaryItemMapper;
    }

    @Override
    public void addBulkSummaryItems(List<SummaryItemRequestDto> items) {
        List<SummaryItem> entities = items.stream()
                .map(this.summaryItemMapper::toEntity)
                .toList();
        this.summaryItemRepository.saveAll(entities);
    }

    @Override
    public SummaryPage getNews(int page, int size) {
        SummaryItem latest = this.summaryItemRepository.findFirstByOrderByLastIndexedDesc().get(0);
        Pageable query = PageRequest.of(page, size, Sort.by("popularity").descending());
        Page fin = this.summaryItemRepository.findAllByLastIndexedAfter(latest.getLastIndexed().minusSeconds(10), query)
                .map(this.summaryItemMapper::toDto);
        return new SummaryPage(
                fin.toList(),
                fin.hasNext()
        );
    }

    @Override
    @Scheduled(cron = "0 */30 * * * *")
    public void generateSummaries() {
        try {
            URL url = new URL(algorithmsUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if(status != 200){
                throw new RuntimeException("Request to fetch new data failed - "+ status);
            }
        } catch (MalformedURLException e){
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
