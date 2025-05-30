package com.lorandi.assembly.util.creator;


import com.lorandi.assembly.dto.VoteDTO;
import com.lorandi.assembly.dto.VoteRequestDTO;
import com.lorandi.assembly.dto.VoteUpdateDTO;
import com.lorandi.assembly.entity.Vote;

import static com.lorandi.assembly.util.PodamFactory.podam;
import static com.lorandi.assembly.util.creator.ElectorCreator.elector;
import static com.lorandi.assembly.util.creator.SurveyCreator.survey;
import static com.lorandi.assembly.util.mapper.MapperConstants.voteMapper;

public class VoteCreator {

    public final static String VALID_CPF = "679.530.080-33";
    public final static String INVALID_CPF = "679.530.080-37";


    public static final Vote vote = podam.manufacturePojo(Vote.class);
    public static final VoteDTO voteDTO = voteMapper.buildVoteDTO(vote);

    public static VoteRequestDTO createVoteRequestDTO() {
        return VoteRequestDTO.builder()
                .electorId(elector.getId())
                .surveyId(survey.getId())
                .approval(vote.getApproval())
                .build();
    }


    public static VoteUpdateDTO voteUpdateDTO() {
        return VoteUpdateDTO.builder()
                .electorId(elector.getId())
                .surveyId(survey.getId())
                .approval(vote.getApproval())
                .build();
    }
}
