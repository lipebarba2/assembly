package com.lorandi.assembly.dto;

import com.lorandi.assembly.enums.ResultStatusEnum;
import lombok.Builder;

public record ResultDTO(SurveyDTO survey, Long approves, Long reproves, Long totalVotes, String result, ResultStatusEnum status) {
    @Builder
    public ResultDTO {};
}
