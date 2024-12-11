package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.CheckList;
import org.springframework.data.repository.CrudRepository;

public interface CheckListenRepository extends CrudRepository<CheckList, String> {
}