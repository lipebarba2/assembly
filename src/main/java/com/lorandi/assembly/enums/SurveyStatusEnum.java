package com.lorandi.assembly.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lorandi.assembly.enums.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonSerialize(using = EnumSerializer.class)
@AllArgsConstructor
@Getter
public enum SurveyStatusEnum implements EnumDescription {

    OPEN("Votação aberta"),
    CLOSED("Votação encerrada");

    private final String description;

}
