package diplomska.heatmail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatMailDto {

    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private String year;

    @JsonProperty("mail_body")
    private String mail_body;

    @JsonProperty("mail_title")
    private String mail_title;

    @JsonProperty("mail_receiver")
    private String mail_receiver;

    @JsonProperty("mail_body_variables")
    private String mail_body_variables;
}
