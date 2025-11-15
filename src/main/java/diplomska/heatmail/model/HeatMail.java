package diplomska.heatmail.model;

import diplomska.heatmail.model.enums.HeatMailStatusEnum;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Heat_Mail")
@Entity
public class HeatMail {

    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false, name = "month")
    private String month;
    @Column(nullable = false, name = "year")
    private String year;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false, name = "status")
    private HeatMailStatusEnum status;
    @Column(nullable = false, name = "mail_body")
    private String mail_body;
    @Column(nullable = false, name = "mail_title")
    private String mail_title;
    @Column(nullable = false, name = "mail_receiver")
    private String mail_receiver;
    @CreationTimestamp
    @Column(nullable = false, name="inserted_at")
    private Date inserted_at;
    @Column(name="sent_at")
    private Date sent_at;
    @Column(name = "mail_body_variables")
    private String mail_body_variables;
    @Column(name = "mail_attachment_title")
    private String mail_attachment_title;

}
