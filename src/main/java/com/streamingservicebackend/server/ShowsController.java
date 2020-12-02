package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.show.Show;
import com.streamingservicebackend.model.show.ShowDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shows")
public class ShowsController extends BaseMediasController<Show> {

    public ShowsController() {
        this(new DatabaseHandler());
    }

    public ShowsController(ISqlHandler sqlHandler) {
        super(new ShowDAO(sqlHandler), sqlHandler);
    }

    @GetMapping
    public String getMedia(@RequestParam Optional<List<String>> serviceId, @RequestParam Optional<String> personId, @RequestParam Optional<String> genre, @RequestParam Optional<String> text, Model model) {

        List<BaseMedia> media = get(serviceId, personId, genre, text);
        List<Show> shows = media.stream().map(m -> (Show)m).collect(Collectors.toList());

        model.addAttribute("shows", shows);
        return "shows";
    }

}
