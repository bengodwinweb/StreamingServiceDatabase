package com.streamingservicebackend.model.person;

import java.time.LocalDate;
import java.util.Objects;

public class Person {

    private String personId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String stageName;
    private LocalDate dateOfBirth;

    public Person(String personId, String firstName, String middleName, String lastName, String stageName, LocalDate dateOfBirth) {
        this.personId = personId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.stageName = stageName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String fullName() {
        return String.format("%s %s %s", getFirstName(), getMiddleName(), getLastName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personId.equals(person.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }


}
