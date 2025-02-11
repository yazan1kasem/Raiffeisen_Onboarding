package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.*;
import org.springframework.data.repository.CrudRepository;

public interface UserChecklistRepository extends CrudRepository<User_Checklists, String> {
    public Iterable<User_Checklists> findByUser(User user);

    Iterable<User_Checklists> findUser_ChecklistsByOriginalChecklist(CheckList checkList);
}
