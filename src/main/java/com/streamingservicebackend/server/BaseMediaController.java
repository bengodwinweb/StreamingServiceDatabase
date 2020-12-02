package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.Dao;
import com.streamingservicebackend.model.MediaInfo;
import com.streamingservicebackend.model.movie.Movie;
import com.streamingservicebackend.model.person.PersonDAO;
import com.streamingservicebackend.model.show.Show;
import com.streamingservicebackend.model.streamingserviceconnection.StreamingServiceConnectionDao;
import com.streamingservicebackend.util.StringUtil;

import java.util.Optional;

public class BaseMediaController<T> {

    private final Dao<T> dao;
    private final DatabaseHandler db;
    private final PersonDAO personDAO;
    private final StreamingServiceConnectionDao connectionDao;

    public BaseMediaController(Dao<T> dao, DatabaseHandler db) {
        this.dao = dao;
        this.db = db;
        this.personDAO = new PersonDAO(db);
        this.connectionDao = new StreamingServiceConnectionDao(db);
    }

    public MediaInfo get(Optional<String> id, Optional<String> name) {
        Optional<BaseMedia> media = Optional.empty();

        if (id.isPresent()) {
            media = (Optional<BaseMedia>) dao.get(id.get());
        } else if (name.isPresent()) {
            media = (Optional<BaseMedia>) dao.getAll().stream().filter(x -> StringUtil.normalizeStringForQuery(((BaseMedia) x).getName()).contains(StringUtil.normalizeStringForQuery(name.get()))).findFirst();
        }

        if (media.isPresent()) {
            BaseMedia found = media.get();
            MediaInfo info = new MediaInfo(found.getId(), found);
            info.setDirectors(personDAO.getDirectorsFromMedia(found));
            info.setActors(personDAO.getActorsFromMedia(found));
            info.setStreamingServices(found instanceof Movie ? connectionDao.getServicesForMovie((Movie) found) : connectionDao.getServicesForShow((Show) found));
            return info;
        }

        return null;
    }
}
