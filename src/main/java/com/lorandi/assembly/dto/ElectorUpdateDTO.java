package com.lorandi.assembly.dto;


import com.lorandi.assembly.enums.ElectorStatusEnum;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@With
public record ElectorUpdateDTO( @NotNull Long id, @NotNull String cpf, ElectorStatusEnum status) {
    @Builder public ElectorUpdateDTO {};
}
