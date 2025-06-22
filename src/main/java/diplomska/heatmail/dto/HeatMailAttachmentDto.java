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
public class HeatMailAttachmentDto {

    @JsonProperty("mail_attachment_title")
    private String mail_attachment_title;

    @JsonProperty("mail_attachment_body")
    private String mail_attachment_body;
}
