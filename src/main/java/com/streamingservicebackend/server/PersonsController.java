package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.person.Person;
import com.streamingservicebackend.model.person.PersonDAO;
import com.streamingservicebackend.model.show.Show;
import com.streamingservicebackend.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PersonsController {

    private final PersonDAO dao;

    public PersonsController() {
        this(new DatabaseHandler());
    }

    public PersonsController(ISqlHandler sqlHandler) {
        dao = new PersonDAO(sqlHandler);
    }

//    @GetMapping
//    @ResponseBody
//    public List<Person> get(@RequestParam Optional<String> mediaId, Optional<String> search) {
//        List<Person> list = mediaId.isPresent() ? dao.getAllFromMedia(mediaId.get()) : dao.getAll();
//
//        if (search.isPresent()) {
//            String[] queryStrings = StringUtil.normalizeStringForQuery(search.get()).split(" ");
//            list = list.stream().filter(x -> Arrays.stream(queryStrings).anyMatch(y -> x.fullName().contains(y))).collect(Collectors.toList());
//        }
//
//        return list;
//    }

    @GetMapping("/persons")
    public String get(@RequestParam Optional<String> mediaId, Optional<String> search, Model model) {
        PersonFilterParams params = new PersonFilterParams();
        params.setSearch(search.orElse(""));
        model.addAttribute("personFilterParams", params);

        List<Person> list = mediaId.isPresent() ? dao.getAllFromMedia(mediaId.get()) : dao.getAll();
        if (search.isPresent()) {
            String[] queryStrings = StringUtil.normalizeStringForQuery(search.get()).split(" ");
            list = list.stream().filter(x -> Arrays.stream(queryStrings).allMatch(y -> x.fullName().toLowerCase().contains(y) || x.getStageName().toLowerCase().contains(y))).collect(Collectors.toList());
        }

        model.addAttribute("persons", list);
        return "persons";
    }

    @PostMapping("/persons-filter")
    public String handleFilterRequest(@ModelAttribute PersonFilterParams params) {
        StringBuilder sb = new StringBuilder("persons");

        if (params.getSearch() != null && params.getSearch().length() > 0) {
            sb.append("?search=");
            sb.append(params.getSearch());
        }

        return "redirect:/" + sb.toString();
    }

}
