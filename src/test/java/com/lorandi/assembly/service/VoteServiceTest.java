package com.lorandi.assembly.service;


import com.lorandi.assembly.dto.ResultDTO;
import com.lorandi.assembly.enums.ElectorStatusEnum;
import com.lorandi.assembly.enums.ResultStatusEnum;
import com.lorandi.assembly.enums.SurveyStatusEnum;
import com.lorandi.assembly.helper.MessageHelper;
import com.lorandi.assembly.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;


import static com.lorandi.assembly.util.creator.ElectorCreator.elector;
import static com.lorandi.assembly.util.creator.SurveyCreator.survey;
import static com.lorandi.assembly.util.creator.SurveyCreator.surveyDTO;
import static com.lorandi.assembly.util.creator.VoteCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class VoteServiceTest {
    @InjectMocks
    private VoteService service;
    @Mock
    private VoteRepository repository;
    @Mock
    private MessageHelper messageHelper;
    @Mock
    private SurveyService surveyService;
    @Mock
    private ElectorService electorService;

    @Test
    void create_returnsVoteDTO_WhenSuccessful() {
        when(surveyService.findById(any())).thenReturn(survey.withStatus(SurveyStatusEnum.OPEN));
        when(electorService.findById(any())).thenReturn(elector);
        when(repository.save(any())).thenReturn(vote);
        assertEquals(voteDTO, service.create(createVoteRequestDTO()));
    }

    @Test
    void create_returns_BAD_REQUEST_When_ERROR_ELECTOR_UNABLE_TO_VOTE() {
        when(repository.findById(any())).thenReturn(Optional.of(vote));
        when(surveyService.findById(any())).thenReturn(survey);
        when(electorService.findById(any())).thenReturn(elector.withStatus(ElectorStatusEnum.UNABLE_TO_VOTE));
        when(repository.save(any())).thenReturn(vote);
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.create(createVoteRequestDTO())).getStatusCode());
    }

    @Test
    void create_returns_BAD_REQUEST_When_ERROR_ELECTOR_ALREADY_VOTED_FOR_THIS_SURVEY() {
        when(repository.findById(any())).thenReturn(Optional.of(vote));
        when(surveyService.findById(any())).thenReturn(survey);
        when(electorService.findById(any())).thenReturn(elector);
        when(repository.findAllBySurveyIdAndElectorId(any(), any())).thenReturn(List.of(vote));
        when(repository.save(any())).thenReturn(vote);
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.create(createVoteRequestDTO())).getStatusCode());
    }

    @Test
    void create_returns_BAD_REQUEST_When_ERROR_THIS_SURVEY_IS_EXPIRED() {
        when(repository.findById(any())).thenReturn(Optional.of(vote));
        when(surveyService.findById(any())).thenReturn(survey.withEndTime(LocalDateTime.now().minusMinutes(2L)));
        when(electorService.findById(any())).thenReturn(elector);
        when(repository.save(any())).thenReturn(vote);
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.create(createVoteRequestDTO())).getStatusCode());
    }

    @Test
    void update_returnsVoteDTO_WhenSuccessful() {
        when(repository.findById(any())).thenReturn(Optional.of(vote));
        when(surveyService.findById(any())).thenReturn(survey.withStatus(SurveyStatusEnum.OPEN));
        when(electorService.findById(any())).thenReturn(elector);
        when(repository.save(any())).thenReturn(vote);
        assertEquals(voteDTO, service.update(voteUpdateDTO()));
    }


    @Test
    void findById_throws404_whenVoteNotFound() {
        when(repository.findById(vote.getId())).thenReturn(Optional.empty());
        final var exception = assertThrows(ResponseStatusException.class, () ->
                service.findById(vote.getId())).getStatusCode();
        assertEquals(NOT_FOUND, exception);
    }

    @Test
    void findDtoById_whenSuccessful() {
        when(repository.findById(vote.getId())).thenReturn(Optional.of(vote));
        assertEquals(voteDTO, service.findDTOById(vote.getId()));
    }

    @Test
    void delete_removesEntity_whenSuccessful() {
        when(repository.findById(vote.getId())).thenReturn(Optional.of(vote));
        service.delete(vote.getId());
        verify(repository, times(1)).delete(vote);
    }

    @Test
    void findAll_returnsPageOfDTOs_whenSuccessful() {
        final var pageable = PageRequest.of(0, 10, Sort.by(ASC, "id"));
        final var assertion = new PageImpl<>(List.of(voteDTO));
        final var votees = new PageImpl<>(List.of(vote));
        when(repository.findAll(any(Pageable.class))).thenReturn(votees);
        assertEquals(assertion, service.findAll(pageable));
    }

    @Test
    void result_returns_Aprovado() {
        {
            when(surveyService.findDTOById(any())).thenReturn(surveyDTO);
            when(repository.countBySurveyIdAndApproval(surveyDTO.id(), true)).thenReturn(5L);
            when(repository.countBySurveyIdAndApproval(surveyDTO.id(), false)).thenReturn(3L);
            var result = ResultDTO.builder().survey(surveyDTO).approves(5L).reproves(3L).result("Aprovado")
                    .totalVotes(8L).status(ResultStatusEnum.FINISHED).build();
            assertEquals(result, service.surveyResult(surveyDTO.id()));
        }
    }
    @Test
    void result_returns_Reprovado() {
        {
            when(surveyService.findDTOById(any())).thenReturn(surveyDTO);
            when(repository.countBySurveyIdAndApproval(surveyDTO.id(), true)).thenReturn(3L);
            when(repository.countBySurveyIdAndApproval(surveyDTO.id(), false)).thenReturn(5L);
            var result = ResultDTO.builder().survey(surveyDTO).approves(3L).reproves(5L).result("Reprovado")
                    .totalVotes(8L).status(ResultStatusEnum.FINISHED).build();
            assertEquals(result, service.surveyResult(surveyDTO.id()));
        }
    }
    @Test
    void result_returns_Empate() {
        {
            when(surveyService.findDTOById(any())).thenReturn(surveyDTO);
            when(repository.countBySurveyIdAndApproval(surveyDTO.id(), true)).thenReturn(3L);
            when(repository.countBySurveyIdAndApproval(surveyDTO.id(), false)).thenReturn(3L);
            var result = ResultDTO.builder().survey(surveyDTO).approves(3L).reproves(3L).result("Empate")
                    .totalVotes(6L).status(ResultStatusEnum.FINISHED).build();
            assertEquals(result, service.surveyResult(surveyDTO.id()));
        }
    }


}


