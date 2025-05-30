package com.lorandi.assembly.service;


import com.lorandi.assembly.helper.MessageHelper;
import com.lorandi.assembly.repository.ElectorRepository;
import com.lorandi.assembly.repository.spec.ElectorSpecification;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static com.lorandi.assembly.util.creator.ElectorCreator.*;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class ElectorServiceTest {
    @InjectMocks
    private ElectorService service;
    @Mock
    private ElectorRepository repository;
    @Mock
    private MessageHelper messageHelper;

    @Test
    void create_returnsElectorDTO_WhenSuccessful() {
        when(repository.save(any())).thenReturn(elector);
        assertEquals(electorDTO, service.create(createElectorRequestDTO()));
    }

    @Test
    void create_returns_BAD_REQUEST_When_INVALID_CPF() {
        when(repository.save(any())).thenReturn(elector);
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.create(createElectorRequestDTO().withCpf(INVALID_CPF))).getStatusCode());
    }
    @Test
    void create_returns_BAD_REQUEST_When_CPF_ALREADY_USED() {
        when(repository.save(any())).thenReturn(elector);
        when(repository.findAllByCpf(any())).thenReturn(List.of(elector));
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.create(createElectorRequestDTO())).getStatusCode());
    }

    @Test
    void update_returnsElectorDTO_WhenSuccessful() {
        when(repository.findById(any())).thenReturn(Optional.of(elector));
        when(repository.save(any())).thenReturn(elector);
        assertEquals(electorDTO, service.update(electorUpdateDTO()));
    }

    @Test
    void update_returns_BAD_REQUEST_When_INVALID_CPF() {
        when(repository.findById(any())).thenReturn(Optional.of(elector));
        when(repository.save(any())).thenReturn(elector);
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.update(electorUpdateDTO().withCpf(INVALID_CPF))).getStatusCode());
    }

    @Test
    void update_returns_BAD_REQUEST_When_CPF_ALREADY_USED() {
        when(repository.findById(any())).thenReturn(Optional.of(elector));
        when(repository.save(any())).thenReturn(elector);
        when(repository.findAllByCpf(any())).thenReturn(List.of(elector.withCpf("950.575.060-99")));
        assertEquals(BAD_REQUEST, assertThrows(ResponseStatusException.class, () ->
                service.update(electorUpdateDTO().withCpf("950.575.060-99"))).getStatusCode());
    }

    @Test
    void findById_throws404_whenElectorNotFound() {
        when(repository.findById(elector.getId())).thenReturn(Optional.empty());
        final var exception = assertThrows(ResponseStatusException.class, () ->
                service.findById(elector.getId())).getStatusCode();
        assertEquals(NOT_FOUND, exception);
    }

    @Test
    void findDtoById_whenSuccessful() {
        when(repository.findById(elector.getId())).thenReturn(Optional.of(elector));
        assertEquals(electorDTO, service.findDTOById(elector.getId()));
    }

    @Test
    void delete_removesEntity_whenSuccessful() {
        when(repository.findById(elector.getId())).thenReturn(Optional.of(elector));
        service.delete(elector.getId());
        verify(repository, times(1)).delete(elector);
    }

    @Test
    void findAll_returnsPageOfDTOs_whenSuccessful() {
        final var pageable = PageRequest.of(0, 10, Sort.by(ASC, "id"));
        final var assertion = new PageImpl<>(List.of(electorDTO));
        final var electors = new PageImpl<>(List.of(elector));
        when(repository.findAll(any(ElectorSpecification.class), any(Pageable.class))).thenReturn(electors);
        assertEquals(assertion, service.findAll(empty(),pageable));
    }

}


