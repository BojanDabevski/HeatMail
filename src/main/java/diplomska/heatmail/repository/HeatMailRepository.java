package diplomska.heatmail.repository;

import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.User;
import diplomska.heatmail.model.enums.HeatMailStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeatMailRepository extends JpaRepository<HeatMail, String> {

    @Query(value = "select * from heat_mail " +
            "where user_id =:userId " +
            "and `month` =:month " +
            "and `year` =:year " +
            "and status ='0'", nativeQuery = true)
    List<HeatMail> findUsersImportedHeatMailForMonthAndYear(String userId, String month, String year);

    @Transactional
    @Modifying
    @Query("update HeatMail h set h.status = ?1 where h.id = ?2")
    int updateStatusById(HeatMailStatusEnum status, String id);




}
