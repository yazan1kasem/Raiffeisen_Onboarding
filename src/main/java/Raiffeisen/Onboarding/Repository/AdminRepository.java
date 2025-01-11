package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<User, String> {

}
