package com.lorandi.assembly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record VoteRequestDTO(@NotNull Long surveyId, @NotNull Long electorId, @NotNull Boolean approval) {
    @Builder
    public VoteRequestDTO {};
}
