package com.lorandi.assembly.dto;


import com.lorandi.assembly.enums.SurveyStatusEnum;

import java.time.LocalDateTime;
public record SurveyDTO(Long id, LocalDateTime endTime, String question, SurveyStatusEnum status){}
