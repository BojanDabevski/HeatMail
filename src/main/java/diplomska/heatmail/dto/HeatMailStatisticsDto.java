package diplomska.heatmail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatMailStatisticsDto {
    @JsonProperty("statsType")
    private String name;

    @JsonProperty("count")
    private Long value;
}
