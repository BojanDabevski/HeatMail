package diplomska.heatmail.repository;

import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.User;
import diplomska.heatmail.model.enums.HeatMailStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Query(value = "select * from heat_mail " +
            "where user_id =:userId " +
            "and `month` =:month " +
            "and `year` =:year " +
            "and status ='3'", nativeQuery = true)
    List<HeatMail> findUsersFailedHeatMailForMonthAndYear(String userId, String month, String year);

    @Query(value = "select * from heat_mail " +
            "where user_id =:userId " +
            "and `month` =:month " +
            "and `year` =:year ", nativeQuery = true)
    List<HeatMail> findUsersHeatMailForMonthAndYear(String userId, String month, String year);

    @Query(value = "select count(*) from heat_mail " +
            "where user_id =:userId " +
            "and `month` =:month " +
            "and `year` =:year " +
            "and status =:status", nativeQuery = true)
    List<HeatMail> countUsersHeatMailForStatus(String userId, String month, String year, String status);

    long countByUser_IdAndMonthAndYearAndStatus(String id, String month, String year, HeatMailStatusEnum status);

    long countByUser_IdAndMonthAndYear(String id, String month, String year);
    @Transactional
    @Modifying
    @Query("update HeatMail h set h.status = ?1 where h.id = ?2")
    int updateStatusById(HeatMailStatusEnum status, String id);

    @Transactional
    @Modifying
    @Query("update HeatMail h set h.status = ?1, h.sent_at = ?2 where h.id = ?3")
    int updateStatusAndSent_atById(HeatMailStatusEnum status, Date sent_at, String id);







}
