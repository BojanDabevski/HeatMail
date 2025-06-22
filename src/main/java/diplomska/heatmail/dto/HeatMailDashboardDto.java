package diplomska.heatmail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatMailDashboardDto {
    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private String year;

    @JsonProperty("mail_receiver")
    private String mail_receiver;

    @JsonProperty("mail_title")
    private String mail_title;

    @JsonProperty("mail_body")
    private String mail_body;

    @JsonProperty("inserted_at")
    private Date inserted_at;

    @JsonProperty("sent_at")
    private Date sent_at;

    @JsonProperty("mail_status")
    private String mail_status;

    @JsonProperty("mail_attachment_title")
    private String mail_attachment_title;


}
