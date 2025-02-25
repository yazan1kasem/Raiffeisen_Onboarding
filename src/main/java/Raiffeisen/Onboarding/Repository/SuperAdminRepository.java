package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface SuperAdminRepository extends CrudRepository<User, String> {
}
