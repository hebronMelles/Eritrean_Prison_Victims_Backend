package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
