package com.onushi.sampleapp;

import com.onushi.testrecording.aspect.RecordMockForTest;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;


@RecordMockForTest
@Repository
public class PersonRepositoryImpl implements PersonRepository {
    @Override
    public Person getPersonFromDB(int id) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (id == 1) {
            return Person.builder()
                    .firstName("Chuck")
                    .lastName("Norris")
                    .dateOfBirth(simpleDateFormat.parse("1940-03-10"))
                    .build();

        } else if (id == 2) {
            return Person.builder()
                    .firstName("Bruce")
                    .lastName("Lee")
                    .dateOfBirth(simpleDateFormat.parse("1940-11-27"))
                    .build();
        } else {
            return null;
        }
    }
}
