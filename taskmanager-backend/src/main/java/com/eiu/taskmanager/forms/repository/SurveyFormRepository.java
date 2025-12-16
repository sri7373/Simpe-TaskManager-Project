package com.eiu.taskmanager.forms.repository;

import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, UUID> {

    List<SurveyForm> findByActiveTrue();

    List<SurveyForm> findByOwner(User owner);

    List<SurveyForm> findByOwnerAndActiveTrue(User owner);

    List<SurveyForm> findByActiveTrueAndExpiryDateAfter(LocalDateTime dateTime);

    List<SurveyForm> findByActiveTrueAndExpiryDateIsNull();
}
