package com.lorandi.assembly.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lorandi.assembly.enums.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum ResultStatusEnum implements EnumDescription {

    IN_PROGRESS("Votação em andamento"),
    FINISHED("Votação encerrada");

    private final String description;

}
