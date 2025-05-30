package com.lorandi.assembly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record VoteUpdateDTO (@NotNull Long id, @NotNull Long surveyId, @NotNull Long electorId, @NotNull Boolean approval) {
    @Builder public VoteUpdateDTO {};
}
