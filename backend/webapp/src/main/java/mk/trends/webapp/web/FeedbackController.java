package mk.trends.webapp.web;

import jakarta.servlet.http.HttpServletRequest;
import mk.trends.webapp.dtos.BiasFeedbackRequestDto;
import mk.trends.webapp.dtos.NewsItemRequestDto;
import mk.trends.webapp.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feedback")
@ResponseBody
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping(value = "/bias", produces = "application/json")
    public Long addBiasFeedback(@RequestBody BiasFeedbackRequestDto dto, HttpServletRequest request){
        dto.setIpAddress(request.getRemoteAddr());
        return this.feedbackService.addFeedback(dto);
    }
}
