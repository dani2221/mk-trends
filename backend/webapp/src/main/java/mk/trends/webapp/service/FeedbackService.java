package mk.trends.webapp.service;

import mk.trends.webapp.dtos.BiasFeedbackRequestDto;

import java.util.List;

public interface FeedbackService {
    Long addFeedback(BiasFeedbackRequestDto request);
}
