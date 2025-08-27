package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.UserForm;
import Eritrean.Prison.Victims.Report.LocationCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFormRepository extends JpaRepository<UserForm, Long>, JpaSpecificationExecutor<UserForm> {
    @Query("select u.location as location ,COUNT(u) as count from UserForm u group by u.location")
    List<LocationCount> getUserFormsByLocation();

    @Query("SELECT COUNT(DISTINCT u.user) FROM UserForm u")
    Long getAllPrisoners();
}
