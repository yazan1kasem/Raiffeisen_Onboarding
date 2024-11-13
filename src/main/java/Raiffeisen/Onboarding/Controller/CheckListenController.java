package Raiffeisen.Onboarding.Controller;

import Raiffeisen.Onboarding.Entities.CheckList;
import Raiffeisen.Onboarding.Repository.CheckListenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/checklisten/")
@CrossOrigin("*")
public class CheckListenController {

    @Autowired
    private CheckListenRepository checkListenRepository;

    @GetMapping("")
    public @ResponseBody Iterable<CheckList> getAllCheckListen() {
        return checkListenRepository.findAll();
    }

    @GetMapping("/{id}")
    public CheckList getCheckListen(@PathVariable String id) {
        return checkListenRepository.findById(id).orElse(null);
    }

    @PostMapping()
    public CheckList createCheckListen(@RequestBody CheckList checkList) {
        checkListenRepository.save(checkList);
        return checkList;
    }

    @PutMapping("{id}")
    public CheckList updateCheckListen(@PathVariable(value = "id") String checkListenId, @RequestBody CheckList checkListenDetails) {
        return ResponseEntity.ok(checkListenRepository.save(new CheckList().builder()
                .id(checkListenId)
                .ueberschrift(checkListenDetails.getUeberschrift())
                .device(checkListenDetails.getDevice())
                .department(checkListenDetails.getDepartment())
                .position(checkListenDetails.getPosition())
                .users(checkListenDetails.getUsers())
                .saved(checkListenDetails.isSaved())
                .build())).getBody();

    }
}
