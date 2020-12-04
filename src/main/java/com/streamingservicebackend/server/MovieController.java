package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.model.MediaInfo;
import com.streamingservicebackend.model.movie.Movie;
import com.streamingservicebackend.model.movie.MovieDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/movie")
public class MovieController extends BaseMediaController<Movie> {

    public MovieController() {
        this(new DatabaseHandler());
    }

    public MovieController(DatabaseHandler db) {
        super(new MovieDAO(db), db);
    }

//    @GetMapping
//    @ResponseBody
//    public MediaInfo getInfo(@RequestParam Optional<String> id, @RequestParam Optional<String> name) {
//        return this.get(id, name);
//    }

    @GetMapping
    public String getInfo(@RequestParam Optional<String> id, @RequestParam Optional<String> name, Model model) {
        model.addAttribute("movie",this.get(id, name));
        return "movie";
    }

}
