package cz.czechitas.java2webapps.ukol7.controller;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class VizitkaController {

    private final VizitkaRepository repository;

    @Autowired
    public VizitkaController(VizitkaRepository repository) {
        this.repository = repository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public Object seznam() {
        Iterable<Vizitka> vizitka = repository.findAll();
        return new ModelAndView("seznam")
                .addObject("seznam", vizitka);
    }

    @GetMapping("/vizitka")
    public String form(int id) {
        return String.format("redirect:/%s", id);
    }


    @GetMapping("/{id:[0-9]+}")
    public Object detail(@PathVariable int id) {
        Optional<Vizitka> vizitka = repository.findById(id);
        if (vizitka.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ModelAndView("vizitka")
                .addObject("vizitka", vizitka.get());
    }

    @GetMapping("/nova")
    public ModelAndView nova() {
        return new ModelAndView("formular")
                .addObject("vizitka", new Vizitka());
    }

    @PostMapping("/nova")
    public Object pridat(@ModelAttribute("vizitka") @Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }
        vizitka.setId(null);
        repository.save(vizitka);
        return "redirect:/";
    }

}
