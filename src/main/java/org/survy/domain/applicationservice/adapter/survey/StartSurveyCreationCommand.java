package org.survy.domain.applicationservice.adapter.survey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.dto.create.SurveyCreationStartRequest;
import org.survy.domain.applicationservice.dto.create.SurveyCreationStartResponse;
import org.survy.domain.applicationservice.mapper.SurveyDataMapper;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.applicationservice.ports.output.repository.UserRepository;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.entity.MyUser;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.SurveyDomainException;
import org.survy.domain.core.valueObject.UserId;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class StartSurveyCreationCommand {

    private final SurveyDomainService surveyDomainService;
    private final SurveyRepository surveyRepository;
    private final SurveyDataMapper surveyDataMapper;
    private final UserRepository userRepository;

    public StartSurveyCreationCommand(SurveyDomainService surveyDomainService,
                                      SurveyRepository surveyRepository,
                                      SurveyDataMapper surveyDataMapper,
                                      UserRepository userRepository) {
        this.surveyDomainService = surveyDomainService;
        this.surveyRepository = surveyRepository;
        this.surveyDataMapper = surveyDataMapper;
        this.userRepository = userRepository;
    }


    @Transactional
    public SurveyCreationStartResponse startSurveyCreationProcess(SurveyCreationStartRequest request) {
        checkUser(request.getCreatorId());
        Survey surveyToBeCreated = mapToDomainEntity(request);

        Survey surveyCreated = surveyDomainService.startSurveyCreationProcess(surveyToBeCreated);

        saveSurvey(surveyCreated);
        return surveyDataMapper.surveyToSurveyCreationStartResponse(surveyCreated);

    }


    private void checkUser(UUID userId) {
        if (!userRepository.existsByUserId(userId))
            throw new SurveyDomainException("No such user exists: " + userId);
    }

    private void saveSurvey(Survey surveyToBeSaved) {
        boolean isSaved = surveyRepository.save(surveyToBeSaved);
        if (!isSaved)
            throw new SurveyDomainException("Could not save survey!");

    }

    private Survey mapToDomainEntity(SurveyCreationStartRequest request) {
        return surveyDataMapper.surveyCreationStartRequestToSurvey(request);
    }


}
