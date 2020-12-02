package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.model.MediaInfo;
import com.streamingservicebackend.model.show.Show;
import com.streamingservicebackend.model.show.ShowDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/show")
public class ShowController extends BaseMediaController<Show> {

    public ShowController() {
        this(new DatabaseHandler());
    }

    public ShowController(DatabaseHandler db) {
        super(new ShowDAO(db), db);
    }

    @GetMapping
    @ResponseBody
    public MediaInfo getInfo(@RequestParam Optional<String> id, @RequestParam Optional<String> name) {
        return this.get(id, name);
    }
}
