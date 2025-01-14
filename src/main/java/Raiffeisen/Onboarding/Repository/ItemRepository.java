package Raiffeisen.Onboarding.Repository;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {
    Item findItemByGeraet(String geraet);
    Item findItemByAdministration(String administration);
    Item findItemBySoftware(String software);
    Item findItemBySuchbegriff(String suchbegriff);
}
