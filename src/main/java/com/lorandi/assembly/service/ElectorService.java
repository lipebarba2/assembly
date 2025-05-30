package com.lorandi.assembly.service;

import com.lorandi.assembly.dto.ElectorDTO;
import com.lorandi.assembly.dto.ElectorRequestDTO;
import com.lorandi.assembly.dto.ElectorUpdateDTO;
import com.lorandi.assembly.entity.Elector;
import com.lorandi.assembly.enums.ElectorStatusEnum;
import com.lorandi.assembly.helper.MessageHelper;
import com.lorandi.assembly.repository.ElectorRepository;
import com.lorandi.assembly.repository.spec.ElectorSpecification;
import com.lorandi.assembly.util.validator.CPFValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


import static com.lorandi.assembly.exception.ErrorCodeEnum.*;
import static com.lorandi.assembly.util.mapper.MapperConstants.electorMapper;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectorService {

    private final ElectorRepository repository;
    private final MessageHelper messageHelper;

    public ElectorDTO create(final ElectorRequestDTO requestDTO) {

        if (!CPFValidator.isValidCPF(requestDTO.cpf())) {
            log.error(messageHelper.get(ERROR_INVALID_CPF, requestDTO.cpf()));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_INVALID_CPF, requestDTO.cpf()));
        }

        String cpf = requestDTO.cpf().replaceAll("[^0-9]", "");

        if (!repository.findAllByCpf(cpf).isEmpty()) {
            log.error(messageHelper.get(ERROR_CPF_ALREADY_USED, cpf));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_CPF_ALREADY_USED, cpf));
        }

        ElectorStatusEnum status = isNull(requestDTO.status()) ? ElectorStatusEnum.ABLE_TO_VOTE
                : requestDTO.status();

        return electorMapper.buildElectorDTO(repository.save(electorMapper.buildElector(requestDTO
                .withCpf(cpf)
                .withStatus(status))));
    }

    public ElectorDTO update(final ElectorUpdateDTO updateDTO) {
        if (!CPFValidator.isValidCPF(updateDTO.cpf())) {
            log.error(messageHelper.get(ERROR_INVALID_CPF, updateDTO.cpf()));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_INVALID_CPF, updateDTO.cpf()));
        }

        String cpf = updateDTO.cpf().replaceAll("[^0-9]", "");

        Elector elector = findById(updateDTO.id());

        if (!elector.getCpf().equals(updateDTO.cpf())) {
            if (!repository.findAllByCpf(cpf).isEmpty()) {
                log.error(messageHelper.get(ERROR_CPF_ALREADY_USED, cpf));
                throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_CPF_ALREADY_USED, cpf));
            }
        }

        ElectorStatusEnum electorStatus = isNull(updateDTO.status()) ? ElectorStatusEnum.ABLE_TO_VOTE
                : updateDTO.status();

        return electorMapper.buildElectorDTO(repository.save(elector.withCpf(cpf).withStatus(electorStatus)));
    }

    public Elector findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_ELECTOR_NOT_FOUND, id.toString()));
            return new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_ELECTOR_NOT_FOUND, id.toString()));
        });
    }

    public ElectorDTO findDTOById(final Long id) {
        Elector elector = findById(id);
        return electorMapper.buildElectorDTO(elector);
    }

    public void delete(final Long id) {
        Elector elector = findById(id);
        repository.delete(elector);
    }

    public Page<ElectorDTO> findAll(final Optional<String> cpf, final Pageable pageable) {
        return repository.findAll(ElectorSpecification.builder().cpf(cpf).build(), pageable).map(electorMapper::buildElectorDTO);
    }
}
