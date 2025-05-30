package com.lorandi.assembly.repository;


import com.lorandi.assembly.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long>, JpaSpecificationExecutor<Survey> {

    @Query(value = "SELECT * FROM survey  WHERE status = 'OPEN' AND end_time < CURRENT_TIMESTAMP", nativeQuery = true)
    List<Survey>  findAllSurveysToUpdateSurveyStatusToClosed();
}
