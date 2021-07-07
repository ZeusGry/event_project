package com.example.event_project.controller.rest;

import com.example.event_project.model.dto.AdressDto;
import com.example.event_project.model.dto.EventDto;
import com.example.event_project.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/Events")
@RequiredArgsConstructor
public class RestEventController {

    private final EventService eventService;


    @GetMapping
    public List<EventDto> getList() {
        return eventService.getListOfEventsDto();
    }

    @GetMapping("{Id}")
    public EventDto get(@PathVariable Long Id) {
        return eventService.getEventDto(Id);
    }

    @PostMapping()
    public void add(@RequestBody EventDto eventDto, AdressDto adressDto) {
        eventService.addEvent(eventDto, adressDto);
    }

    @PatchMapping()
    public void update(@RequestBody EventDto dto) {
        eventService.updateEvent(dto);
    }

    @DeleteMapping()
    public void delete(@RequestBody EventDto dto) {
        eventService.deleteEvent(dto);
    }
}
