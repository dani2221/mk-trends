package mk.trends.webapp.web;

import mk.trends.webapp.dtos.NewsItemRequestDto;
import mk.trends.webapp.dtos.SummaryItemRequestDto;
import mk.trends.webapp.service.NewsItemService;
import mk.trends.webapp.service.SummaryItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("internal")
@ResponseBody
public class InternalController {
    private final NewsItemService newsItemService;
    private final SummaryItemService summaryItemService;

    public InternalController(NewsItemService newsItemService, SummaryItemService summaryItemService) {
        this.newsItemService = newsItemService;
        this.summaryItemService = summaryItemService;
    }

    @PostMapping(value = "/news", produces = "application/json")
    public List<Long> addNews(@RequestBody List<NewsItemRequestDto> items){
        return this.newsItemService.addBulkItems(items);
    }

    @PostMapping(value = "/summaries", produces = "application/json")
    public void addSummaries(@RequestBody List<SummaryItemRequestDto> items){
        this.summaryItemService.addBulkSummaryItems(items);
    }

    @GetMapping(value = "/force-gen")
    public void genSummaries(){
        this.summaryItemService.generateSummaries();
    }

}
