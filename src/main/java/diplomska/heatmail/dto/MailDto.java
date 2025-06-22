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
public class MailDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("body")
    private String body;

    @JsonProperty("title")
    private String title;

    @JsonProperty("to")
    private String to;

    @JsonProperty("attachment_title")
    private String attachment_title;

    @JsonProperty("attachment")
    private String attachment;

}
