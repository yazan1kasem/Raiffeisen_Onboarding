package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.User_Checklist_Items;
import org.springframework.data.repository.CrudRepository;

public interface UserChecklistItemsRepository extends CrudRepository<User_Checklist_Items, String> {
}
