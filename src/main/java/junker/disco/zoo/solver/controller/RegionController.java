package junker.disco.zoo.solver.controller;

import java.util.List;

import junker.disco.zoo.solver.model.animals.Region;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
@CrossOrigin
public class RegionController {
    @GetMapping
    public List<String> getRegions() {
        return Region.getRegionReprs();
    }
}
