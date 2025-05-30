package com.lorandi.assembly.service;

import com.lorandi.assembly.helper.MessageHelper;
import com.lorandi.assembly.repository.SurveyRepository;
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

import java.util.List;
import java.util.Optional;

import static com.lorandi.assembly.util.creator.SurveyCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class SurveyServiceTest {
    @InjectMocks
    private SurveyService service;
    @Mock
    private SurveyRepository repository;
    @Mock
    private MessageHelper messageHelper;

    @Test
    void create_returnsSurveyDTO_WhenSuccessful() {
        when(repository.save(any())).thenReturn(survey);
        assertEquals(surveyDTO, service.create(createSurveyRequestDTO()));
    }

    @Test
    void update_returnsSurveyDTO_WhenSuccessful() {
        when(repository.findById(any())).thenReturn(Optional.of(survey));
        when(repository.save(any())).thenReturn(survey);
        assertEquals(surveyDTO, service.update(surveyUpdateDTO()));
    }

    @Test
    void findById_throws404_whenSurveyNotFound() {
        when(repository.findById(survey.getId())).thenReturn(Optional.empty());
        final var exception = assertThrows(ResponseStatusException.class, () ->
                service.findById(survey.getId())).getStatusCode();
        assertEquals(NOT_FOUND, exception);
    }

    @Test
    void findDtoById_whenSuccessful() {
        when(repository.findById(survey.getId())).thenReturn(Optional.of(survey));
        assertEquals(surveyDTO, service.findDTOById(survey.getId()));
    }

    @Test
    void delete_removesEntity_whenSuccessful() {
        when(repository.findById(survey.getId())).thenReturn(Optional.of(survey));
        service.delete(survey.getId());
        verify(repository, times(1)).delete(survey);
    }

    @Test
    void findAll_returnsPageOfDTOs_whenSuccessful() {
        final var pageable = PageRequest.of(0, 10, Sort.by(ASC, "id"));
        final var assertion = new PageImpl<>(List.of(surveyDTO));
        final var surveyes = new PageImpl<>(List.of(survey));
        when(repository.findAll(any(Pageable.class))).thenReturn(surveyes);
        assertEquals(assertion, service.findAll(pageable));
    }

}


