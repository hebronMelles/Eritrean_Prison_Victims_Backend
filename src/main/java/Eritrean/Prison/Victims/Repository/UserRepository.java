package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.User;
import Eritrean.Prison.Victims.Report.UsersCount;
import Eritrean.Prison.Victims.Service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findBySub(String sub);
    User  findUserByEmail(String email);
    @Query("select count(u) from User u ")
    long getTotalUsers();
}
