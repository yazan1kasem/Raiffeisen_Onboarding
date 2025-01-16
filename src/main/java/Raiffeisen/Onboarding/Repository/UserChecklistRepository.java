package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.*;
import org.springframework.data.repository.CrudRepository;

public interface UserChecklistRepository extends CrudRepository<user_checklists, String> {
}
