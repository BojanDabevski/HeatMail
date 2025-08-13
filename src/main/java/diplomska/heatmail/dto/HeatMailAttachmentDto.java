package diplomska.heatmail.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeatMailAttachmentDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("mail_attachment_title")
    private String mail_attachment_title;

    @JsonProperty("mail_attachment_body")
    private String mail_attachment_body;
}
