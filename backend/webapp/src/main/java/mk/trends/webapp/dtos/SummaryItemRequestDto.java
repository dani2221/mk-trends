package mk.trends.webapp.dtos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SummaryItemRequestDto {

    private String title;

    private List<String> summary;

    private String photoUrl;

    private Set<Long> linkedNewsItemIds;

}
