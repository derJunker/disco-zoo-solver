package junker.disco.zoo.solver.controller;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Region;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animals")
@CrossOrigin
public class AnimalController {

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimalsOfRegion(@RequestParam("region") String regionStr) {
        var regionOpt = Region.byRepr(regionStr);
        return regionOpt
                .map(region -> new ResponseEntity<>(Animal.getAnimalListByRegion(region, true), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/byName")
    public ResponseEntity<List<Animal>> getAnimalsByNames(@RequestParam("names") List<String> names) {
        var animals =  Animal.findAnimalsByName(names.toArray(new String[0]));
        if (animals.size() != names.size())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(animals, HttpStatus.OK);
    }
}
