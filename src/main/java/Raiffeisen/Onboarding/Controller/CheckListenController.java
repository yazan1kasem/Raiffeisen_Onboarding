package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/checklisten")
@CrossOrigin("*")
public class CheckListenController {

    @Autowired
    private CheckListenRepository checkListenRepository;

    @GetMapping("")
    public @ResponseBody Iterable<CheckList> getAllCheckListen() {
        return checkListenRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckList> getCheckListen(@PathVariable String id) {
        return checkListenRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<CheckList> createCheckListen(@RequestBody CheckList checkList) {
        CheckList savedCheckList = checkListenRepository.save(checkList);
        return ResponseEntity.ok(savedCheckList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckList> updateCheckListen(
            @PathVariable String id,
            @RequestBody CheckList checkListenDetails) {
        return checkListenRepository.findById(id).map(existingCheckList -> {
            existingCheckList.setUeberschrift(checkListenDetails.getUeberschrift());
            existingCheckList.setItems(checkListenDetails.getItems());
            CheckList updatedCheckList = checkListenRepository.save(existingCheckList);
            return ResponseEntity.ok(updatedCheckList);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckListen(@PathVariable String id) {
        if (checkListenRepository.existsById(id)) {
            checkListenRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
