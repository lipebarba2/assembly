package com.lorandi.assembly.entity;

import com.lorandi.assembly.enums.SurveyStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime endTime;
    private String question;
    @Enumerated(STRING)
    private SurveyStatusEnum status;
}
