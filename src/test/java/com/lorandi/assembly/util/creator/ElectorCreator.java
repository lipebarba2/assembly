package com.lorandi.assembly.util.creator;

import com.lorandi.assembly.dto.ElectorDTO;
import com.lorandi.assembly.dto.ElectorRequestDTO;
import com.lorandi.assembly.dto.ElectorUpdateDTO;
import com.lorandi.assembly.entity.Elector;
import com.lorandi.assembly.enums.ElectorStatusEnum;

import static com.lorandi.assembly.util.PodamFactory.podam;
import static com.lorandi.assembly.util.mapper.MapperConstants.electorMapper;

public class ElectorCreator {

    public final static String VALID_CPF = "679.530.080-33";
    public final static String INVALID_CPF = "679.530.080-37";


    public static final Elector elector = podam.manufacturePojo(Elector.class).withCpf(VALID_CPF)
            .withStatus(ElectorStatusEnum.ABLE_TO_VOTE);
    public static final ElectorDTO electorDTO = electorMapper.buildElectorDTO(elector);

    public static ElectorRequestDTO createElectorRequestDTO() {
        return ElectorRequestDTO.builder()
                .cpf(elector.getCpf())
                .status(elector.getStatus())
                .build();
    }

    public static ElectorUpdateDTO electorUpdateDTO() {
        return ElectorUpdateDTO.builder()
                .id(elector.getId())
                .cpf(elector.getCpf())
                .status(elector.getStatus())
                .build();
    }
}
