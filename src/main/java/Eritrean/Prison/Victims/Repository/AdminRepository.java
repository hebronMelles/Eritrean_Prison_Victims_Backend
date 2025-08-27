package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
}
