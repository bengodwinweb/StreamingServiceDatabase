package com.streamingservicebackend.model;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    /**
     * Returns an optional containing the matching object from the db with the given ID if found
     * @param id id string to search for
     * @return optional containing matching object if found
     */
    Optional<T> get(String id);

    /**
     * Returns a list of all objects in the db
     * @return complete list of objects of this type in the db
     */
    List<T> getAll();

    /**
     * Adds the object to the db
     * @param t object to add
     * @return true if object successfully added to the db
     */
    boolean add(T t);

    /**
     * Updates all fields in the db for the record matching the id of the given object
     * @param t object to update in the db
     * @return true if object successfully updated
     */
    boolean update(T t);

    /**
     * Removes the object from the db
     * @param t object to delete
     * @return true if object successfully deleted
     */
    boolean delete(T t);
}
