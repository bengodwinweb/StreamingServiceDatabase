package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.Genre;
import com.streamingservicebackend.model.show.Show;
import com.streamingservicebackend.model.show.ShowDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ShowsController extends BaseMediasController<Show> {

    public ShowsController() {
        this(new DatabaseHandler());
    }

    public ShowsController(ISqlHandler sqlHandler) {
        super(new ShowDAO(sqlHandler), sqlHandler);
    }

    @GetMapping("/shows")
    public String getMedia(@RequestParam Optional<List<String>> serviceId, @RequestParam Optional<String> personId, @RequestParam Optional<String> genre, @RequestParam Optional<String> text, Model model) {
        model.addAttribute("url", "shows");
        model.addAttribute("genres", Arrays.stream(Genre.values()).sorted(Comparator.comparing(Genre::getDisplayText)).toArray(Genre[]::new));
        model.addAttribute("services", this.serviceDAO.getAll());
        model.addAttribute("mediaFilterParams", new MediaFilterParams());

        List<BaseMedia> media = get(serviceId, personId, genre, text);
        List<Show> shows = media.stream().map(m -> (Show)m).collect(Collectors.toList());
        model.addAttribute("shows", shows);
        return "shows";
    }

    @PostMapping("/shows-filter")
    public String handleFilterRequest(@ModelAttribute MediaFilterParams params) {
        StringBuilder sb = new StringBuilder("shows");

        boolean needAmpersand = false;

        if (params.getServiceIds() != null && params.getServiceIds().length > 0) {
            sb.append("?");
            sb.append("serviceId=");
            sb.append(String.join(",", params.getServiceIds()));
            needAmpersand = true;
        }

        if (params.getPersonId() != null && params.getPersonId().length() > 0) {
            sb.append(needAmpersand ? "&" : "?");
            sb.append("personId=");
            sb.append(params.getPersonId());
            needAmpersand = true;
        }

        if (params.getGenre() != null && params.getGenre().length() > 0) {
            sb.append(needAmpersand ? "&" : "?");
            sb.append("genre=");
            sb.append(params.getGenre().toLowerCase());
            needAmpersand = true;
        }

        if (params.getText() != null && params.getText().length() > 0) {
            sb.append(needAmpersand ? "&" : "?");
            sb.append("text=");
            sb.append(params.getText().replaceAll(" ", "+").toLowerCase());
        }

        return "redirect:/" + sb.toString();
    }
}
