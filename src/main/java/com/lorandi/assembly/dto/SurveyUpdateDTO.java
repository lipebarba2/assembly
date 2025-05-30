package com.lorandi.assembly.dto;

import com.lorandi.assembly.enums.SurveyStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record SurveyUpdateDTO(@NotNull Long id, Long minutes, @NotNull String question, SurveyStatusEnum status) {
    @Builder public SurveyUpdateDTO {};
}
