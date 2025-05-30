package com.lorandi.assembly.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.lorandi.assembly.enums.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum ElectorStatusEnum implements EnumDescription {

    ABLE_TO_VOTE("Pode votar"),
    UNABLE_TO_VOTE("Não pode votar");

    private final String description;

}
