package com.lorandi.assembly.dto;


import com.lorandi.assembly.enums.ElectorStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;


@Builder
@With
public record ElectorRequestDTO( @NotNull String cpf, ElectorStatusEnum status) {
    @Builder public ElectorRequestDTO {};
}


