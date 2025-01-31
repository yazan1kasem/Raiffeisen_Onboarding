package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.CheckList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CheckListenRepository extends CrudRepository<CheckList, String> {
}