package com.streamingservicebackend.server;

import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.Dao;
import com.streamingservicebackend.model.movie.MovieDAO;
import com.streamingservicebackend.model.person.PersonDAO;
import com.streamingservicebackend.model.show.ShowDAO;
import com.streamingservicebackend.model.streamingserviceconnection.StreamingServiceConnectionDao;
import com.streamingservicebackend.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class BaseMediasController<T extends BaseMedia> {

    private final Dao<T> dao;
    private final ISqlHandler _sqlHandler;
    private final PersonDAO personDAO;
    private final StreamingServiceConnectionDao connectionDao;

    public BaseMediasController(Dao<T> dao, ISqlHandler sqlHandler) {
        this.dao = dao;
        this._sqlHandler = sqlHandler;
        this.personDAO = new PersonDAO(sqlHandler);
        this.connectionDao = new StreamingServiceConnectionDao(sqlHandler);
    }

    public List<BaseMedia> get(Optional<List<String>> serviceId, Optional<String> personId, Optional<String> genre, Optional<String> text) {
        boolean show = dao instanceof ShowDAO;
        List<BaseMedia> list;

        if (serviceId.isPresent()) {
            list = show ? new ArrayList<BaseMedia>(((ShowDAO) dao).getAllFromStreamingService(serviceId.get().toArray(String[]::new))) : new ArrayList<BaseMedia>(((MovieDAO) dao).getAllFromStreamingService(serviceId.get().toArray(String[]::new)));
        } else {
            list = (List<BaseMedia>) dao.getAll();
        }

        if (personId.isPresent()) {
            List<BaseMedia> temp = show ? new ArrayList<BaseMedia>(((ShowDAO) dao).getAllFromPerson(personId.get())) : new ArrayList<BaseMedia>(((MovieDAO) dao).getAllFromPerson(personId.get()));
            list = list.stream().filter(temp::contains).collect(Collectors.toList());
        }

        if (genre.isPresent()) {
            list = list.stream().filter(x -> x.getGenre().toLowerCase().contains(genre.get().toLowerCase())).collect(Collectors.toList());
        }

        if (text.isPresent()) {
            String queryText = StringUtil.normalizeStringForQuery(text.get());
            Map<BaseMedia, Integer> temp = new HashMap<>();

            for (BaseMedia m : list) {
                String textToSearch = StringUtil.normalizeStringForQuery(m.getName() + " " + m.getDescription());
                int index = textToSearch.indexOf(queryText);
                if (index >= 0) {
                    temp.put(m, index);
                }
            }

            list = temp.entrySet().stream().sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).map(x -> x.getKey()).collect(Collectors.toList());
        }

        return list;
    }
}
