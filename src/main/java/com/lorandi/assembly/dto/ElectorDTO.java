package com.lorandi.assembly.dto;

import com.lorandi.assembly.enums.ElectorStatusEnum;
import jakarta.validation.constraints.NotNull;

public record ElectorDTO (Long id, @NotNull String cpf, ElectorStatusEnum status) {}


