package com.example.event_project.service;

import com.example.event_project.exceptions.EventNotFindException;
import com.example.event_project.exceptions.UserNotFindException;
import com.example.event_project.model.*;
import com.example.event_project.model.dto.EventDto;
import com.example.event_project.model.dto.mapper.AdressMapper;
import com.example.event_project.model.dto.mapper.EventMapper;
import com.example.event_project.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AdressRepository adressRepository;
    private final AdressMapper adressMapper;
    private final ParticipantRepository participantRepository;
    private final OrganizerRepository organizerRepository;
    private final UserRepository userRepository;
    private final OrganizerToAddRepository organizerToAddRepository;
    private final MailService mailService;
    private final RoleRepository roleRepository;


    public List<EventDto> getListOfEventsDto() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventDto(Long id) {
        Optional<Event> optEvent = eventRepository.findById(id);
        return eventMapper.eventToDto(optEvent.orElse(new Event()));
    }

    public EventDto addEvent(EventDto eventDto) {
        Adress adress;
        Optional<Adress> optAdress = adressRepository.findByCityAndAndStreetAndNumberOfBuilding(eventDto.getAdress()
                .getCity(), eventDto.getAdress()
                .getStreet(), eventDto.getAdress()
                .getNumberOfBuilding());
        adress = optAdress.orElse(adressMapper.dtoToAdress(eventDto.getAdress()));
        adress = adressRepository.save(adress);
        Event event = eventMapper.dtoToEvent(eventDto);
        event.setAdress(adress);
        return eventMapper.eventToDto(eventRepository.save(event));
    }

    public boolean deleteEvent(EventDto dto) {
        Optional<Event> optEvent = eventRepository.findById(dto.getId());
        if (optEvent.isPresent()) {
            eventRepository.deleteById(dto.getId());
            return true;
        }
        return false;
    }


    public List<EventDto> getListOfEventsDtoByUser(Long id) {
        return participantRepository.findByUserId(id)
                .stream()
                .map(participant -> eventMapper.eventToDto(participant.getEvent()))
                .collect(Collectors.toList());
    }

    public List<EventDto> getListOfEventsDtoOrganized(Long id) {
        return organizerRepository.findByUserId(id)
                .stream()
                .map(Organizer::getEvent)
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList());
    }

    public void joinEvent(Long userId, Long eventId) throws EventNotFindException, UserNotFindException {
        Optional<Event> optEvent = eventRepository.findById(eventId);
        System.out.println(eventId);
        System.out.println(userId);
        Optional<User> optUser = userRepository.findById(userId);
        if (optEvent.isEmpty()) {
            throw new EventNotFindException();
        }
        if (optUser.isEmpty()) {
            throw new UserNotFindException();
        }
        Participant participant = new Participant();
        participant.setEvent(optEvent.get());
        participant.setUser(optUser.get());
        participantRepository.save(participant);
    }

    public void quitEvent(Long userId, Long eventId) throws EventNotFindException, UserNotFindException {
        Optional<Event> optEvent = eventRepository.findById(eventId);
        Optional<User> optUser = userRepository.findById(userId);
        if (optEvent.isEmpty()) {
            throw new EventNotFindException();
        }
        if (optUser.isEmpty()) {
            throw new UserNotFindException();
        }
        Optional<Participant> participantToRemove = participantRepository.findByUserIdAndEventId(optUser.get()
                .getId(), optEvent.get()
                .getId());
        participantToRemove.ifPresent(participant -> participantRepository.deleteById(participant
                .getId()));
    }

    public boolean isParticipiant(Long eventId, String userName) throws UserNotFindException {
        Optional<User> optUser = userRepository.findByLogin(userName);
        if (optUser.isEmpty()) {
            throw new UserNotFindException();
        }
        Optional<Participant> optParticipant = participantRepository.findByUserIdAndEventId(optUser.get()
                .getId(), eventId);
        return optParticipant.isPresent();
    }

    public void setOrganizators(Long eventId, String creatorName, String[] organizators) throws MessagingException {
        Optional<Event> optEvent = eventRepository.findById(eventId);
        Optional<User> optUser = userRepository.findByLogin(creatorName);
        if (optEvent.isPresent() && optUser.isPresent()) {
            Event event = optEvent.get();
            // todo: nieużywane organizer list
            List<Organizer> organizerList = new ArrayList<>();
            organizerRepository.save(new Organizer(optUser.get(), event));
            // todo: wciągnać to w metodę - powinieneś starać się trzymać max 2 zagłebienia
            for (String organizator : organizators) {
                Optional<User> optOrganizator = userRepository.findByEmail(organizator);
                if (optOrganizator.isPresent()) {
                    organizerRepository.save(new Organizer(optOrganizator.get(), event));
                    Role tmpRole = getRole(ERole.ROLE_MODERATOR);
                    optOrganizator.get()
                            .getRoles()
                            .add(tmpRole);
                    userRepository.save(optOrganizator.get());
                } else {
                    organizerToAddRepository.save(new OrganizerToAdd(event, organizator));
                    String encodedString = Base64.getEncoder()
                            .encodeToString(organizator.getBytes());
                    mailService.sendMail(organizator, "Zostań organizatorem", "Wejdz na link:\n" +
                            "http://localhost:4200/register/" + encodedString + "\ni zarejestruj się już dziś jako organizator");
                }
            }
        }
    }

    private Role getRole(ERole role) {
        Optional<Role> tmpRole = roleRepository.findByName(role);
        return tmpRole.orElseThrow(() -> new RuntimeException("Something is wrong with db!"));
    }
}
