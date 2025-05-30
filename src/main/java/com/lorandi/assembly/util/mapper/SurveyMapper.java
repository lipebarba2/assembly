package com.lorandi.assembly.util.mapper;

import com.lorandi.assembly.dto.SurveyDTO;
import com.lorandi.assembly.dto.SurveyRequestDTO;
import com.lorandi.assembly.entity.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SurveyMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    Survey buildSurvey(SurveyRequestDTO requestDTO);

    SurveyDTO buildSurveyDTO(Survey survey);
}
