package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findBySub(String sub);

    User findUserByEmail(String email);

    @Query("select count(u) from User u")
    long getTotalUsers();

    User findByEmail(String email);

}
