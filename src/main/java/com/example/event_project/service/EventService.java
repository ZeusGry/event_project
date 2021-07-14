package com.example.event_project.service;

import com.example.event_project.model.Adress;
import com.example.event_project.model.Event;
import com.example.event_project.model.dto.AdressDto;
import com.example.event_project.model.dto.EventDto;
import com.example.event_project.model.dto.mapper.AdressMapper;
import com.example.event_project.model.dto.mapper.EventMapper;
import com.example.event_project.repository.AdressRepository;
import com.example.event_project.repository.EventRepository;
import com.example.event_project.repository.OrganizerRepository;
import com.example.event_project.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    public List<EventDto> getListOfEventsDto() {
        return eventRepository.findAll()
                .stream()
                .map(event -> eventMapper.eventToDto(event))
                .collect(Collectors.toList());
    }

    public EventDto getEventDto(Long id) {
        Optional<Event> optEvent = eventRepository.findById(id);
        return eventMapper.eventToDto(optEvent.orElse(new Event()));
    }

    public Event addEvent(EventDto eventDto, AdressDto adressDto) {
        Adress adress;
        if (adressDto.getId() == null) {
            adress = adressMapper.dtoToAdress(adressDto);
            adress = adressRepository.save(adress);
        } else {
            adress = adressRepository.findById(adressDto.getId())
                    .get();
        }
        Event event = eventMapper.dtoToEvent(eventDto);
        event.setAdress(adress);
        return eventRepository.save(event);
    }

    public void updateEvent(EventDto dto) {
        Optional<Event> optEvent = eventRepository.findById(dto.getId());
        if (optEvent.isPresent()) {
            optEvent.get()
                    .setStartTime(dto.getStartTime());
            eventRepository.save(optEvent.get());
        }
    }

    public void deleteEvent(EventDto dto) {
        Optional<Event> optEvent = eventRepository.findById(dto.getId());
        if (optEvent.isPresent()) {
            eventRepository.deleteById(dto.getId());
        }
    }


    public List<EventDto> getListOfEventsDtoAccepted(Long id) {
        return participantRepository.findByUserIdAndAccepted(id, true)
                .stream()
                .map(event -> eventMapper.eventToDto(event))
                .collect(Collectors.toList());
    }

    public List<EventDto> getListOfEventsDtoNotAccepted(Long id) {
        return participantRepository.findByUserIdAndAccepted(id, false)
                .stream()
                .map(event -> eventMapper.eventToDto(event))
                .collect(Collectors.toList());
    }

    public List<EventDto> getListOfEventsDtoOrganized(Long id) {
        return organizerRepository.findByUserId(id)
                .stream()
                .map(event -> eventMapper.eventToDto(event))
                .collect(Collectors.toList());
    }
}
