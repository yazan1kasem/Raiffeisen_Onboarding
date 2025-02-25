package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserChecklistRepository extends CrudRepository<User_Checklists, String> {

    Iterable<User_Checklists> findUser_ChecklistsByOriginalChecklist(CheckList checkList);

    List<User_Checklists> findByUser(User user);
}
