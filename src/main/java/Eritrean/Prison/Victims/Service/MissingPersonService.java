package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.MissingPerson;
import Eritrean.Prison.Victims.Repository.MissingPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissingPersonService {
    private final MissingPersonRepository missingPersonRepository;

    @Autowired
    public MissingPersonService(MissingPersonRepository missingPersonRepository) {
        this.missingPersonRepository = missingPersonRepository;
    }

    public List<MissingPerson> getAll() {
        return missingPersonRepository.findAll();
    }

    public MissingPerson getById(Long id) {
        return missingPersonRepository.findById(id).orElse(null);
    }

    public MissingPerson create(MissingPerson person) {
        return missingPersonRepository.save(person);
    }

    public void deleteById(Long id) {
        missingPersonRepository.deleteById(id);
    }

    public void deleteAll() {
        missingPersonRepository.deleteAll();
    }
}
