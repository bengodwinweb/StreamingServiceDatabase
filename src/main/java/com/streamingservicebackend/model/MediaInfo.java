package com.streamingservicebackend.model;

import com.streamingservicebackend.model.person.Person;
import com.streamingservicebackend.model.streamingserviceconnection.StreamingServiceConnection;

import java.util.*;

public class MediaInfo {

    private final String id;
    private final BaseMedia media;
    private final Map<String, Person> directors;
    private final Map<String, Person> actors;
    private final Map<String, StreamingServiceConnection> streamingServices;

    public MediaInfo(String id, BaseMedia media) {
        this.id = id;
        this.media = media;
        directors = new HashMap<>();
        actors = new HashMap<>();
        streamingServices = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public BaseMedia getMedia() {
        return media;
    }

    public List<Person> getDirectors() {
        return new ArrayList<>(directors.values());
    }

    public List<Person> getActors() {
        return new ArrayList<>(actors.values());
    }

    public List<StreamingServiceConnection> getStreamingServices() {
        return new ArrayList<>(streamingServices.values());
    }

    public void setDirectors(Collection<Person> directors) {
        directors.forEach(x -> this.directors.put(x.getPersonId(), x));
    }

    public void setActors(Collection<Person> actors) {
        actors.forEach(x -> this.actors.put(x.getPersonId(), x));
    }

    public void setStreamingServices(Collection<StreamingServiceConnection> services) {
        services.forEach(x -> streamingServices.put(x.getId(), x));
    }
}
