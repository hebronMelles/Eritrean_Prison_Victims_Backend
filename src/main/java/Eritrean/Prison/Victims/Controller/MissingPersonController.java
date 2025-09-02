package Eritrean.Prison.Victims.Controller;

import Eritrean.Prison.Victims.DTOMapper.DtoMapper;
import Eritrean.Prison.Victims.Dtos.MissingPersonDto;
import Eritrean.Prison.Victims.Entity.MissingPerson;
import Eritrean.Prison.Victims.Service.MissingPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/missing-person")
@RestController
public class MissingPersonController {
    private final MissingPersonService missingPersonService;
    private final DtoMapper dtoMapper;

    @Autowired
    public MissingPersonController(MissingPersonService missingPersonService, DtoMapper dtoMapper) {
        this.missingPersonService = missingPersonService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<MissingPersonDto>> getAll() {
        List<MissingPerson> people = missingPersonService.getAll();
        return ResponseEntity.ok(dtoMapper.missingPersonToDtoList(people));
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<MissingPersonDto> getById(@PathVariable Long id) {
        MissingPerson person = missingPersonService.getById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtoMapper.missingPersonToDto(person));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<MissingPersonDto> create(@RequestBody MissingPerson person) {
        MissingPerson savedPerson = missingPersonService.create(person);
        return ResponseEntity.ok(dtoMapper.missingPersonToDto(person));
    }

    @DeleteMapping("/delete-by-id/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        missingPersonService.deleteById(id);
        return ResponseEntity.ok("Successfully deleted missing person data");
    }

    @DeleteMapping("/delete-all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> deleteAll() {
        missingPersonService.deleteAll();
        return ResponseEntity.ok("Successfully deleted all missing persons data");
    }
}
