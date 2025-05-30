package com.lorandi.assembly.repository;

import com.lorandi.assembly.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>, JpaSpecificationExecutor<Vote> {
    List<Vote> findAllBySurveyIdAndElectorId(Long surveyId, Long electorId);
    Long countBySurveyIdAndApproval(Long surveyId, Boolean approval);

}
