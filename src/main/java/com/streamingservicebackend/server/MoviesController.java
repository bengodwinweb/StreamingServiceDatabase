package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.movie.Movie;
import com.streamingservicebackend.model.movie.MovieDAO;
import com.streamingservicebackend.model.show.Show;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MoviesController extends BaseMediasController<Movie> {

    public MoviesController() {
        this(new DatabaseHandler());
    }

    public MoviesController(ISqlHandler sqlHandler) {
        super(new MovieDAO(sqlHandler), sqlHandler);
    }

    @GetMapping
    public String getMovies(@RequestParam Optional<List<String>> serviceId, @RequestParam Optional<String> personId, @RequestParam Optional<String> genre, @RequestParam Optional<String> text, Model model) {
        List<BaseMedia> media = get(serviceId, personId, genre, text);
        List<Movie> movies = media.stream().map(m -> (Movie)m).collect(Collectors.toList());
        model.addAttribute("movies", movies);

        return "movies";
    }
}
