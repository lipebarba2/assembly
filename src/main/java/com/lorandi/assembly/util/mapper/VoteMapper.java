package com.lorandi.assembly.util.mapper;

import com.lorandi.assembly.dto.VoteDTO;
import com.lorandi.assembly.dto.VoteRequestDTO;
import com.lorandi.assembly.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VoteMapper {
    @Mapping(target = "id", ignore = true)
    Vote buildVote(VoteRequestDTO requestDTO);

    VoteDTO buildVoteDTO(Vote vote);
}
