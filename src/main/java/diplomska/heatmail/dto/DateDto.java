package diplomska.heatmail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateDto {

    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private String year;

}