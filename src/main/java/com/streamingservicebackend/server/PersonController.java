package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.Dao;
import com.streamingservicebackend.model.PersonInfo;
import com.streamingservicebackend.model.mediaconnection.MediaConnectionDao;
import com.streamingservicebackend.model.person.Person;
import com.streamingservicebackend.model.person.PersonDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final Dao<Person> dao;
    private final MediaConnectionDao mediaConnectionDao;

    public PersonController() {
        this(new DatabaseHandler());
    }

    public PersonController(ISqlHandler sqlHandler) {
        this.dao = new PersonDAO(sqlHandler);
        mediaConnectionDao = new MediaConnectionDao(sqlHandler);
    }

    @GetMapping
    @ResponseBody
    public PersonInfo get(@RequestParam String id) {
        Optional<Person> p = dao.get(id);
        if (p.isEmpty()) {
            return null;
        }

        Person found = p.get();
        PersonInfo info = new PersonInfo(found.getPersonId(), found);
        info.setMovies(mediaConnectionDao.getMoviesForPerson(found));
        info.setShows(mediaConnectionDao.getShowsForPerson(found));
        return info;
    }
}
