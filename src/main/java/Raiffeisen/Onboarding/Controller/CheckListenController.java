package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/checklisten")
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




}
