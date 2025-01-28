package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserChecklistRepository extends CrudRepository<user_checklists, String> {
    List<user_checklists> findByUser(User user);
}
