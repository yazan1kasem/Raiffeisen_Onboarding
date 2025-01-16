package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {

    Item findItemBySuchbegriff(String suchbegriff);
}
