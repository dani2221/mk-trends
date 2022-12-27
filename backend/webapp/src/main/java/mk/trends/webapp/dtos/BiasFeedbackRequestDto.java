package mk.trends.webapp.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import mk.trends.webapp.model.Bias;
import mk.trends.webapp.model.NewsItem;

@Data
public class BiasFeedbackRequestDto {
    private String ipAddress;

    private String comment;

    private long newsItemId;

    private Bias suggestedBias;
}
