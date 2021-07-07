package com.example.event_project.model.dto.mapper;

import com.example.event_project.model.Adress;
import com.example.event_project.model.dto.AdressDto;
import org.mapstruct.Mapper;

@Mapper
public interface AdressMapper {
    Adress dtoToAdress(AdressDto dto);

    AdressDto adressToDto(Adress adress);
}
