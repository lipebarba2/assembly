package com.lorandi.assembly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record SurveyRequestDTO( Long minutes, @NotNull String question) {}
