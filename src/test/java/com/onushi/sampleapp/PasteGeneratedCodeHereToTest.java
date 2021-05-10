package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.onushi.sampleapp.services.PersonRepositoryImpl;
import com.onushi.sampleapp.model.Person;

class PersonServiceTest {
    @Test
    void getPersonFirstName() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();
        PersonRepositoryImpl personRepositoryImpl = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl.getPersonsCountFromDB()).thenReturn(2);
        when(personRepositoryImpl.getPersonFromDB(2)).thenReturn(person1);
        PersonService personService = new PersonService(personRepositoryImpl);

        // Act
        String result = personService.getPersonFirstName(2);

        // Assert
        assertEquals("Bruce", result);
    }
}
