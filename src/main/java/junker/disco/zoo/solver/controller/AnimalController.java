package junker.disco.zoo.solver.controller;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Animal;
import junker.disco.zoo.solver.model.animals.Region;
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
    public List<Animal> getAnimalsOfRegion(@RequestParam("region") String region) {
        return Animal.getAnimalListByRegion(Region.byRepr(region), true);
    }
}
