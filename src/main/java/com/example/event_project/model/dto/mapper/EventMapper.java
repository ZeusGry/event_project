package com.example.event_project.model.dto.mapper;

import com.example.event_project.model.Event;
import com.example.event_project.model.dto.EventDto;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    Event dtoToEvent(EventDto dto);

    EventDto eventToDto(Event event);
}
