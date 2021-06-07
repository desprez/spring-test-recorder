package com.onushi.sample.services;

import com.onushi.sample.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person loadPerson(int id) throws Exception {
        return personRepository.getPersonFromDB(id);
    }

    public String getPersonFirstName(int id) {
//        personRepository.getPersonsCountFromDB("a", null);
        try {
            Person person = personRepository.getPersonFromDB(id);
            return person.getFirstName();
        } catch (Exception ex) {
            return null;
        }
    }
}