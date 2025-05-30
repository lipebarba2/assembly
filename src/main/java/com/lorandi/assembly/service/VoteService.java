package com.lorandi.assembly.service;


import com.lorandi.assembly.dto.*;
import com.lorandi.assembly.entity.Elector;
import com.lorandi.assembly.entity.Survey;
import com.lorandi.assembly.entity.Vote;
import com.lorandi.assembly.enums.ElectorStatusEnum;
import com.lorandi.assembly.enums.ResultStatusEnum;
import com.lorandi.assembly.enums.SurveyStatusEnum;
import com.lorandi.assembly.event.producer.MessengerPublisherService;
import com.lorandi.assembly.helper.MessageHelper;
import com.lorandi.assembly.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lorandi.assembly.exception.ErrorCodeEnum.*;
import static com.lorandi.assembly.util.mapper.MapperConstants.voteMapper;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final MessageHelper messageHelper;

    private final ElectorService electorService;
    private final SurveyService surveyService;

    private final MessengerPublisherService messengerService;


    public VoteDTO create(final VoteRequestDTO requestDTO) {
        validateVote(requestDTO.surveyId(), requestDTO.electorId());

        return voteMapper.buildVoteDTO(repository.save(voteMapper.buildVote(requestDTO)));
    }

    public VoteDTO update(final VoteUpdateDTO updateDTO) {

        validateVote(updateDTO.surveyId(), updateDTO.electorId());

        Vote vote = findById(updateDTO.id());

        return voteMapper.buildVoteDTO(repository.save(vote));
    }

    public Vote findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_VOTE_NOT_FOUND, id.toString()));
            return new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_VOTE_NOT_FOUND, id.toString()));
        });
    }

    public VoteDTO findDTOById(final Long id) {
        Vote vote = findById(id);
        return voteMapper.buildVoteDTO(vote);
    }

    public void delete(final Long id) {
        Vote vote = findById(id);
        repository.delete(vote);
    }

    public Page<VoteDTO> findAll(final Pageable pageable) {
        return repository.findAll(pageable).map(voteMapper::buildVoteDTO);
    }

    private void validateVote(Long surveyId, Long electorId) {

        Survey survey = surveyService.findById(surveyId);
        Elector elector = electorService.findById(electorId);

        if (elector.getStatus().equals(ElectorStatusEnum.UNABLE_TO_VOTE)) {
            log.error(messageHelper.get(ERROR_ELECTOR_UNABLE_TO_VOTE, electorId));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_ELECTOR_UNABLE_TO_VOTE,
                    electorId));
        }

        if (!repository.findAllBySurveyIdAndElectorId(surveyId, electorId).isEmpty()) {
            log.error(messageHelper.get(ERROR_ELECTOR_ALREADY_VOTED_FOR_THIS_SURVEY, electorId, surveyId));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_ELECTOR_ALREADY_VOTED_FOR_THIS_SURVEY,
                    electorId, surveyId));
        }

        if (survey.getEndTime().isBefore(LocalDateTime.now()) || survey.getStatus().equals(SurveyStatusEnum.CLOSED)) {
            log.error(messageHelper.get(ERROR_THIS_SURVEY_IS_EXPIRED, survey.getId().toString()));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_THIS_SURVEY_IS_EXPIRED,
                    survey.getId().toString()));
        }
    }

    public ResultDTO surveyResult(Long surveyId) {
        SurveyDTO survey = surveyService.findDTOById(surveyId);
        Long approves = repository.countBySurveyIdAndApproval(surveyId, true);
        Long reproves = repository.countBySurveyIdAndApproval(surveyId, false);

        String result;
        if (approves > reproves) {
            result = "Aprovado";
        } else if ((approves < reproves)) {
            result = "Reprovado";
        } else {
            result = "Empate";
        }

        ResultStatusEnum status = survey.status().equals(SurveyStatusEnum.OPEN) ?
                ResultStatusEnum.IN_PROGRESS : ResultStatusEnum.FINISHED;

        return ResultDTO.builder().survey(survey).approves(approves).reproves(reproves).totalVotes(approves + reproves)
                .result(result).status(status).build();
    }

    public List<ResultDTO> assemblyResult() {
        List<Survey> listSurvey = surveyService.findAll();
        List<ResultDTO> listResult = new ArrayList<>();
        for (Survey survey : listSurvey) {
            listResult.add(surveyResult(survey.getId()));
        }
        return listResult;
    }
}
