package com.lorandi.assembly.service;


import com.lorandi.assembly.dto.SurveyDTO;
import com.lorandi.assembly.dto.SurveyRequestDTO;
import com.lorandi.assembly.dto.SurveyUpdateDTO;
import com.lorandi.assembly.entity.Survey;
import com.lorandi.assembly.enums.SurveyStatusEnum;
import com.lorandi.assembly.helper.MessageHelper;
import com.lorandi.assembly.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static com.lorandi.assembly.exception.ErrorCodeEnum.ERROR_SURVEY_NOT_FOUND;
import static com.lorandi.assembly.util.mapper.MapperConstants.surveyMapper;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository repository;
    private final MessageHelper messageHelper;
    private final static Long DEFAULT_MINUTES = 1L;

    public SurveyDTO create(final SurveyRequestDTO requestDTO) {

        Long minutes = isNull(requestDTO.minutes()) ? DEFAULT_MINUTES : requestDTO.minutes();

        return surveyMapper.buildSurveyDTO(repository.save(surveyMapper.buildSurvey(requestDTO)
                .withEndTime(LocalDateTime.now().plusMinutes(minutes))
                .withStatus(SurveyStatusEnum.OPEN)));
    }

    public SurveyDTO update(final SurveyUpdateDTO updateDTO) {
        var minutes = isNull(updateDTO.minutes()) ? DEFAULT_MINUTES : updateDTO.minutes();

        Survey survey = findById(updateDTO.id());

        return surveyMapper.buildSurveyDTO(repository.save(survey
                .withQuestion(updateDTO.question())
                .withEndTime(LocalDateTime.now().plusMinutes(minutes)))
                .withStatus(updateDTO.status()));
    }

    public Survey findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_SURVEY_NOT_FOUND, id.toString()));
            return new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_SURVEY_NOT_FOUND, id.toString()));
        });
    }

    public SurveyDTO findDTOById(final Long id) {
        Survey survey = findById(id);
        return surveyMapper.buildSurveyDTO(survey);
    }

    public void delete(final Long id) {
        Survey survey = findById(id);
        repository.delete(survey);
    }

    public Page<SurveyDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable).map(surveyMapper::buildSurveyDTO);
    }

    public List<Survey> findAll() {
        return repository.findAll();
    }

    public List<Survey> findAllSurveysToUpdateSurveyStatusToClosed() {
        return repository.findAllSurveysToUpdateSurveyStatusToClosed();
    }

    public Survey save(Survey survey){
        return repository.save(survey);
    };
}
