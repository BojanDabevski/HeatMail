package diplomska.heatmail.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Heat_Mail_Attachment")
@Entity
public class HeatMailAttachment {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, name = "mail_attachment_title")
    private String mail_attachment_title;

    @Column(nullable = false, name = "mail_attachment_body")
    private String mail_attachment_body;
}
