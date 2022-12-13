package mk.trends.webapp.web;

import mk.trends.webapp.dtos.SummaryItemResponseDto;
import mk.trends.webapp.dtos.SummaryPage;
import mk.trends.webapp.service.NewsItemService;
import mk.trends.webapp.service.SummaryItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/")
@ResponseBody
public class SummariesController {
    private final NewsItemService newsItemService;
    private final SummaryItemService summaryItemService;

    public SummariesController(NewsItemService newsItemService, SummaryItemService summaryItemService) {
        this.newsItemService = newsItemService;
        this.summaryItemService = summaryItemService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public SummaryPage getSummaries(@RequestParam int page, @RequestParam int size){
        return this.summaryItemService.getNews(page, size);
    }
}
