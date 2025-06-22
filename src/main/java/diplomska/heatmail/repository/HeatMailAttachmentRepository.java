package diplomska.heatmail.repository;

import diplomska.heatmail.model.HeatMailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HeatMailAttachmentRepository extends JpaRepository<HeatMailAttachment, String> {

    @Query(value = "SELECT * FROM heatmail.heat_mail_attachment " +
            "where user_id=:userId " +
            "and mail_attachment_title=:attachmentTitle", nativeQuery = true)
    List<HeatMailAttachment> findByUser_IdAndMail_attachment_title(String userId, String attachmentTitle);

}
