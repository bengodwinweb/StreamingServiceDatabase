package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.Genre;
import com.streamingservicebackend.model.movie.Movie;
import com.streamingservicebackend.model.movie.MovieDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MoviesController extends BaseMediasController<Movie> {

    public MoviesController() {
        this(new DatabaseHandler());
    }

    public MoviesController(ISqlHandler sqlHandler) {
        super(new MovieDAO(sqlHandler), sqlHandler);
    }

    @GetMapping("/movies")
    public String getMovies(@RequestParam Optional<List<String>> serviceId, @RequestParam Optional<String> personId, @RequestParam Optional<String> genre, @RequestParam Optional<String> text, Model model) {
        model.addAttribute("url", "movies");
        model.addAttribute("genres", Genre.values());
        model.addAttribute("services", this.serviceDAO.getAll());

        MediaFilterParams params = new MediaFilterParams();

        String[] serviceIds;
        if (serviceId.isPresent()) {
            serviceIds = serviceId.get().toArray(new String[0]);
        } else {
            serviceIds = new String[0];
        }

        params.setServiceIds(serviceIds);
        params.setGenre(genre.orElse(Genre.NONE.getDisplayText()));
        params.setText(text.orElse(""));
        model.addAttribute("mediaFilterParams", params);

        List<BaseMedia> media = get(serviceId, personId, genre, text);
        List<Movie> movies = media.stream().map(m -> (Movie)m).collect(Collectors.toList());
        model.addAttribute("movies", movies);
        return "movies";
    }

    @PostMapping("/movies-filter")
    public String handleFilterRequest(@ModelAttribute MediaFilterParams params) {
        StringBuilder sb = new StringBuilder("movies");

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
