package com.lorandi.assembly.util.mapper;


import com.lorandi.assembly.dto.ElectorDTO;
import com.lorandi.assembly.dto.ElectorRequestDTO;
import com.lorandi.assembly.entity.Elector;
import org.mapstruct.Mapper;

@Mapper
public interface ElectorMapper {
    Elector buildElector(ElectorRequestDTO requestDTO);

    ElectorDTO buildElectorDTO(Elector elector);
}
