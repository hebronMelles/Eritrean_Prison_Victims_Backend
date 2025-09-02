package Eritrean.Prison.Victims.Repository;

import Eritrean.Prison.Victims.Entity.MissingPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissingPersonRepository extends JpaRepository<MissingPerson, Long> {
}
