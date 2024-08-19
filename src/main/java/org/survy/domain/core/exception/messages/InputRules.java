package org.survy.domain.core.exception.messages;

public class InputRules {

    public static final String SURVEY_TITLE = "survey must have one title shorter than 200 characters.";
    public static final String SURVEY_CLOSE_DATE = "survey close date must be in future time";
    public static final String SURVEY_DESCRIPTION = "survey must have one description shorter than 1000 characters";
    public static final String SURVEY_CREATOR = "survey must include at least one creator";

    public static  final String QUESTION_TEXT = "question must have one question text shorter than 200 characters.";
    public static final String QUESTION_PAGE = "page must be in the range [1,10]";
    public static final String QUESTION_ORDER = "Order must be positive or zero";

    public static final String MULTIPLE_CHOICE_OPTION_NUMBER = "multiple-choice question must include at least two options";
    public static final String INVALID_OPTION = "invalid option";

    public static final String QUOTA_MUST_CONTAIN_QUESTION = "simple quota must contain one question";
    public static final String QUOTA_VALUE = "simple quota value must be greater than 0";
    public static final String QUOTA_OPTION = "simple quota must contain at least one option";

}
