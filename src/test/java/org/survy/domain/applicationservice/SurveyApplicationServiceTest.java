package org.survy.domain.applicationservice;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.survy.domain.applicationservice.dto.create.*;
import org.survy.domain.applicationservice.mapper.QuestionDataMapper;
import org.survy.domain.applicationservice.ports.input.service.SurveyApplicationService;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.logic.SimpleQuotaLogic;
import org.survy.domain.core.entity.question.MultipleChoiceQuestion;
import org.survy.domain.core.entity.question.OpenEndedQuestion;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.exception.SurveyDomainException;
import org.survy.domain.core.valueObject.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = SurveyTestConfig.class)
public class SurveyApplicationServiceTest {

//    @Autowired
//    private SurveyRepository surveyRepository;
//    @Autowired
//    private SurveyDomainService surveyDomainService;
//    @Autowired
//    private QuestionDataMapper questionDataMapper;
//    @Autowired
//    private SurveyApplicationService surveyApplicationService;
//    @Autowired
//    private CacheManager cacheManager;
//
//
//    private final String option1Label = "option1";
//    private final int option1Order = 1;
//    private final int quota = 100;
//    private final String option2label = "option2";
//    private final int option2order = 2;
//
//    private QuestionId questionId;
//    private QuestionId question2Id;
//    private LogicAddRequest request;
//    private Survey survey;
//    private SurveyId surveyId;
//
//
//    @BeforeEach
//    public void init() {
//        survey = Survey.builder()
//                .closeDate(new CloseDate(Date.from(Instant.now().plusSeconds(1000))))
//                .title(new SurveyTitle("title"))
//                .description(new SurveyDescription("alksjda"))
//                .creators(Arrays.asList(new UserId(UUID.randomUUID())))
//                .build();
//
//        survey.initializeSurvey();
//        surveyId = survey.getId();
//        Question addedQuestion = survey.addQuestion(new MultipleChoiceQuestion("question-1", 1, 1,
//                Arrays.asList(new Option(option1Label, option1Order), new Option(option2label, option2order))));
//        questionId = addedQuestion.getId();
//        Question addedQuestion2 = survey.addQuestion(new OpenEndedQuestion("question2", 1, 2));
//        question2Id = addedQuestion2.getId();
//
//        request = LogicAddRequest.builder()
//                .surveyId(surveyId.getValue())
//                .type(ParticipationLogicTypes.SIMPLE_QUOTA.name())
//                .options(List.of(option1Label))
//                .questionId(questionId.getValue())
//                .quota(quota)
//                .build();
//
//
//        when(surveyRepository.save(any())).thenReturn(survey);
//        when(surveyRepository.findWithQuestionsAndParticipationLogicsById(surveyId)).thenReturn(Optional.of(survey));
//
//
//        Cache cache = cacheManager.getCache("surveysWithQuestions");
//        if(cache != null){
//            System.out.println(cache.get(surveyId.getValue()));
//            cache.clear();
//        }
//
//        System.out.println(cache == null);
//
//    }
//
//    @Test
//    public void addParticipationLogicToSurvey_SurveyNotExists_throwsException() {
//        when(surveyRepository.findWithQuestionsAndParticipationLogicsById(surveyId)).thenReturn(Optional.empty());
//
//        SurveyDomainException exception = assertThrows(SurveyDomainException.class,
//                () -> surveyApplicationService.addParticipationLogicToSurvey(request));
//
//        assertEquals("No such survey exists: " + surveyId.getValue(), exception.getMessage());
//
//    }
//
//
//    @Test
//    public void addParticipationLogicToSurvey_success() {
//
//
//        LogicAddResponse logic = surveyApplicationService.addParticipationLogicToSurvey(request);
//
//        assertNotNull(logic.getParticipationLogicId());
//        System.out.println(logic.getParticipationLogicId());
//        assertEquals(survey.getParticipationLogics().size(), 1);
//        assertEquals(survey.getParticipationLogics().get(0).getClass(), SimpleQuotaLogic.class);
//    }
//
//    @Test
//    public void addParticipationLogicToSurvey_noSuchQuestion_throwsException() {
//        UUID randomUUID = UUID.randomUUID();
//        request.setQuestionId(randomUUID);
//
//        SurveyDomainException exception = assertThrows(SurveyDomainException.class,
//                () -> surveyApplicationService.addParticipationLogicToSurvey(request));
//
//        assertEquals("Survey does not contain such question " + randomUUID, exception.getMessage());
//    }
//
//    @Test
//    public void addParticipationLogicToSurvey_noSuchOption_throwsException() {
//        request.setOptions(List.of("optasdasd"));
//        QuestionDomainException exception = assertThrows(QuestionDomainException.class,
//                () -> surveyApplicationService.addParticipationLogicToSurvey(request));
//        assertEquals("question does not have such option optasdasd", exception.getMessage());
//    }
//
//    @Test
//    public void addParticipationLogicToSurvey_noApplicableQuestion_throwsException() {
//        request.setQuestionId(question2Id.getValue());
//        QuestionDomainException exception = assertThrows(QuestionDomainException.class,
//                () -> surveyApplicationService.addParticipationLogicToSurvey(request));
//        assertEquals("quota cannot be applied to this question " + question2Id.getValue(), exception.getMessage());
//
//    }
//
//
//    @Test
//    public void completeSurveyCreationProcess_SUCCESS() {
//        SurveyCreationCompleteRequest completeRequest = new SurveyCreationCompleteRequest(surveyId.getValue());
//        when(surveyRepository.findWithQuestionsById(surveyId)).thenReturn(Optional.of(survey));
//        when(surveyRepository.updateStatus(survey)).thenReturn(true);
//        SurveyCreationCompleteResponse response = surveyApplicationService.completeSurveyCreationProcess(completeRequest);
//
//        assertEquals("Survey completed successfully", response.getMessage());
//        assertEquals(surveyId.getValue(), response.getSurveyId());
//        assertEquals(SurveyStatus.OPEN, survey.getSurveyStatus());
//    }
//
//    @Test
//    public void completeSurveyCreationProcess_NoQuestion_throwsException() {
//
//        Survey survey = Survey.builder()
//                .closeDate(new CloseDate(Date.from(Instant.now().plusSeconds(1000))))
//                .title(new SurveyTitle("title"))
//                .description(new SurveyDescription("alksjda"))
//                .creators(Arrays.asList(new UserId(UUID.randomUUID())))
//                .build();
//
//        survey.initializeSurvey();
//        SurveyId surveyId = survey.getId();
//
//
//        SurveyCreationCompleteRequest completeRequest = new SurveyCreationCompleteRequest(surveyId.getValue());
//        when(surveyRepository.findWithQuestionsById(surveyId)).thenReturn(Optional.of(survey));
//        when(surveyRepository.updateStatus(survey)).thenReturn(true);
//
//        SurveyDomainException exception = assertThrows(SurveyDomainException.class,
//                () -> surveyApplicationService.completeSurveyCreationProcess(completeRequest));
//
//        assertEquals("survey must contains at least one question", exception.getMessage());
//
//    }
//
//
//
//    @Test
//    public void addQuestionToSurveyTest_cacheTest(){
//        QuestionAddRequest questionAddRequest = QuestionAddRequest.builder()
//                .type(QuestionTypes.OPEN_ENDED.name())
//                .questionText("open-ended question")
//                .page(1)
//                .order(3)
//                .surveyId(surveyId.getValue())
//                .build();
//
//        when(surveyRepository.findWithQuestionsById(surveyId)).thenReturn(Optional.of(survey));
//        surveyApplicationService.addQuestionToSurvey(questionAddRequest);
//
//        // Again add the same question.
//        surveyApplicationService.addQuestionToSurvey(questionAddRequest);
//
//
//
//    }



}
