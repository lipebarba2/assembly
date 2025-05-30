package com.lorandi.assembly.util.creator;


import com.lorandi.assembly.dto.SurveyDTO;
import com.lorandi.assembly.dto.SurveyRequestDTO;
import com.lorandi.assembly.dto.SurveyUpdateDTO;
import com.lorandi.assembly.entity.Survey;
import com.lorandi.assembly.enums.SurveyStatusEnum;

import java.time.LocalDateTime;

import static com.lorandi.assembly.util.PodamFactory.podam;
import static com.lorandi.assembly.util.mapper.MapperConstants.surveyMapper;


public class SurveyCreator {

    public static final Survey survey = podam.manufacturePojo(Survey.class).withEndTime(LocalDateTime.now().plusMinutes(1L)).withStatus(SurveyStatusEnum.CLOSED);
    public static final SurveyDTO surveyDTO = surveyMapper.buildSurveyDTO(survey);

    public static SurveyRequestDTO createSurveyRequestDTO() {
        return SurveyRequestDTO.builder()
                .minutes(2L)
                .question(survey.getQuestion())
                .build();
    }

    public static SurveyUpdateDTO surveyUpdateDTO() {
        return SurveyUpdateDTO.builder()
                .id(survey.getId())
                .minutes(2L)
                .question(survey.getQuestion())
                .status(survey.getStatus())
                .build();
    }
}
