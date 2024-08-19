package org.survy.domain.core.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.question.MultipleChoiceQuestion;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.exception.QuestionInitializationException;
import org.survy.domain.core.exception.QuestionNotAddedException;
import org.survy.domain.core.exception.SurveyInitializationException;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.exception.messages.InputRules;
import org.survy.domain.core.valueObject.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SurveyTest {

    private UUID surveyUUID;
    private Survey survey;
    private SurveyTitle surveyTitle;
    private SurveyDescription surveyDescription;
    private CloseDate closeDate;
    private SurveyId surveyId;
    private SurveyStatus surveyStatus;
    private UserId userId;
    private List<UserId> creators;


    private String questionText = "question 1";
    private int questionPage = 1;
    private int questionOrder = 1;
    private String optionLabel1 = "option label1";
    private String optionLabel2 = "option label2";
    private int option1Order = 1;

    private List<Question> questions;
    private List<ParticipationLogic> participationLogics;


    @BeforeEach
    public void init() {
        surveyUUID = UUID.randomUUID();
        surveyId = new SurveyId(surveyUUID);
        surveyTitle = new SurveyTitle("Survey title ");
        surveyDescription = new SurveyDescription("Survey description");
        closeDate = new CloseDate(Date.from(Instant.now().plusSeconds(10000)));
        surveyStatus = SurveyStatus.OPEN;
        userId = new UserId();
        creators = List.of(userId);


    }


    @Test
    public void initializeSurvey_AssignsIdAndStatus_SUCCESS() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();

        survey.initializeSurvey();

        assertNotNull(survey.getId());
        assertEquals(survey.getSurveyStatus(), SurveyStatus.UNCONFIGURED);
        assertNotNull(survey.getQuestions());
        assertNotNull(survey.getParticipationLogics());
    }

    @Test
    public void initializeSurvey_hasNoTitle_throwsException() {
        survey = Survey.builder()
//                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();

        SurveyInitializationException surveyInitializationException =
                assertThrows(SurveyInitializationException.class,
                        () -> survey.initializeSurvey());

        assertEquals(InputRules.SURVEY_TITLE, surveyInitializationException.getMessage());
    }

    @Test
    public void initializeSurvey_pastTimeCloseDate_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(new CloseDate(Date.from(Instant.now().minusMillis(100000L))))
                .creators(creators)
                .build();

        SurveyInitializationException surveyInitializationException =
                assertThrows(SurveyInitializationException.class,
                        () -> survey.initializeSurvey());

        assertEquals(InputRules.SURVEY_CLOSE_DATE, surveyInitializationException.getMessage());
    }


    @Test
    public void initializeSurvey_hasNoCreator_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
//                .creators() // you can try  empty array also.
                .build();

        SurveyInitializationException surveyInitializationException =
                assertThrows(SurveyInitializationException.class,
                        () -> survey.initializeSurvey());

        assertEquals(InputRules.SURVEY_CREATOR, surveyInitializationException.getMessage());
    }

    @Test
    public void initializeSurvey_hasNoDescription_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
//                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();

        SurveyInitializationException surveyInitializationException =
                assertThrows(SurveyInitializationException.class,
                        () -> survey.initializeSurvey());

        assertEquals(InputRules.SURVEY_DESCRIPTION, surveyInitializationException.getMessage());
    }


    @Test
    public void initializeSurvey_alreadyInitialized_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .surveyStatus(SurveyStatus.UNCONFIGURED) // OPEN , UNCONFIGURED , CLOSED
                .build();

        SurveyInitializationException surveyInitializationException =
                assertThrows(SurveyInitializationException.class,
                        () -> survey.initializeSurvey());

        assertEquals(FailureMessages.SURVEY_INITIAL_STATUS, surveyInitializationException.getMessage());
    }


    @Test
    public void addQuestion_SUCCESS() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();
        survey.initializeSurvey();
        Option option = new Option(optionLabel1);
        Option option1 = new Option(optionLabel2);
        Question question = MultipleChoiceQuestion.builder()
                .options(List.of(option , option1))
                .questionText(questionText)
                .page(questionPage)
                .order(questionOrder)
                .build();

        survey.addQuestion(question);
        assertEquals(1 , survey.getQuestions().size());
        assertEquals(survey.getId() , survey.getQuestions().get(0).getSurveyId());
        assertEquals(question , survey.getQuestions().get(0));

    }


    @Test
    public void addQuestion_invalidOption_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();
        survey.initializeSurvey();
        optionLabel1 = "";
        Option option = new Option(optionLabel1);
        Option option1 = new Option(optionLabel2);
        Question question = MultipleChoiceQuestion.builder()
                .options(List.of(option , option1))
                .questionText(questionText)
                .page(questionPage)
                .order(questionOrder)
                .build();

        QuestionNotAddedException exception =
                assertThrows(QuestionNotAddedException.class ,
                        () -> survey.addQuestion(question)  );

        assertTrue(exception.getCause() instanceof QuestionInitializationException);
        assertEquals(InputRules.INVALID_OPTION , exception.getMessage());

    }


    @Test
    public void addQuestion_uninitializedSurvey_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();
//        survey.initializeSurvey();
        optionLabel1 = "";
        Option option = new Option(optionLabel1);
        Option option1 = new Option(optionLabel2);
        Question question = MultipleChoiceQuestion.builder()
                .options(List.of(option , option1))
                .questionText(questionText)
                .page(questionPage)
                .order(questionOrder)
                .build();

        QuestionNotAddedException exception =
                assertThrows(QuestionNotAddedException.class ,
                        () -> survey.addQuestion(question)  );

        assertTrue(exception.getCause() instanceof SurveyInitializationException);
        assertEquals(FailureMessages.SURVEY_INITIAL_STATUS , exception.getMessage());

    }

    @Test
    public void addQuestion_onlyOneOptionInQuestion_throwsException() {
        survey = Survey.builder()
                .title(surveyTitle)
                .description(surveyDescription)
                .closeDate(closeDate)
                .creators(creators)
                .build();
        survey.initializeSurvey();
        optionLabel1 = "uu";
        Option option = new Option(optionLabel1);
//        Option option1 = new Option(optionLabel2);
        Question question = MultipleChoiceQuestion.builder()
                .options(List.of(option ))
                .questionText(questionText)
                .page(questionPage)
                .order(questionOrder)
                .build();

        QuestionNotAddedException exception =
                assertThrows(QuestionNotAddedException.class ,
                        () -> survey.addQuestion(question)  );

        assertTrue(exception.getCause() instanceof QuestionInitializationException);
        assertEquals(InputRules.MULTIPLE_CHOICE_OPTION_NUMBER , exception.getMessage());

    }








}
