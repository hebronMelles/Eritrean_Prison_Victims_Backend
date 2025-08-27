package Eritrean.Prison.Victims.Service;

import Eritrean.Prison.Victims.Entity.UserForm;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserFormSpecification {
    public static Specification<UserForm> hasLocation(String location) {
        return (root, querry, builder) ->
                location == null ? null : builder.equal(root.get("location"), location);
    }

    public static Specification<UserForm> hasStartDate(LocalDate startDate) {
        return (root, query, builder) ->
                startDate == null ? null : builder.equal(root.get("startDate"), startDate);
    }

    public static Specification<UserForm> hasEndDate(LocalDate endDate) {
        return (root, query, builder) ->
                endDate == null ? null : builder.equal(root.get("endDate"), endDate);
    }

}
