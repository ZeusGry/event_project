package com.example.event_project.service;

import com.example.event_project.model.Adress;
import com.example.event_project.model.Event;
import com.example.event_project.model.dto.EventDto;
import com.example.event_project.model.dto.mapper.AdressMapper;
import com.example.event_project.model.dto.mapper.EventMapper;
import com.example.event_project.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("tests")
public class EventServiceTest {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final MailService mailService;
    private final AdressRepository adressRepository;

    @Autowired
    public EventServiceTest(EventService eventService, EventRepository eventRepository, EventMapper eventMapper, AdressRepository adressRepository, AdressMapper adressMapper, ParticipantRepository participantRepository, OrganizerRepository organizerRepository, UserRepository userRepository, OrganizerToAddRepository organizerToAddRepository, RoleRepository roleRepository, JavaMailSender javaMailSender) {
        this.mailService = new MailService(javaMailSender);
        this.eventService = new EventService(eventRepository, eventMapper, adressRepository, adressMapper, participantRepository, organizerRepository, userRepository, organizerToAddRepository, mailService, roleRepository);
        this.eventRepository = eventRepository;
        this.adressRepository = adressRepository;
    }

    @Test
    void eventListIsInitializedEmptyTest() {
        assert_initiallyEmpty();
    }

    private void assert_initiallyEmpty() {
        List<EventDto> list = eventService.getListOfEventsDto();
        Assertions.assertEquals(0, list.size());
    }

    @Nested
    class EventManagmentTests {

        @BeforeEach
        void setupTest() {
            clearDatabase();
            Adress adress = new Adress();
            adress.setCity("Gdańsk");
            adress.setStreet("Słowackiego");
            adress.setNumberOfBuilding("14");
            adress = adressRepository.save(adress);
            assert_initiallyEmpty();
            Event event = eventRepository.save(Event.builder()
                    .adress(adress)
                    .email("hello")
                    .name("Dawid")
                    .phoneNumber("500202020")
                    .startTime(LocalDateTime.now())
                    .build());
            eventRepository.save(event);
        }

        private void clearDatabase() {
            eventRepository.deleteAll();
        }

        @Test
        void eVentCanBeAddedTest() {
            List<EventDto> list = eventService.getListOfEventsDto();
            Assertions.assertEquals(1, list.size());
        }

    }
}
