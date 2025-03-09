package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.UserForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFormRepository extends JpaRepository<UserForm, Long> {
}
