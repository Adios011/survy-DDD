package org.survy.domain.core.valueObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("valid")
public class SurveyTitle {

    private String title;

    public SurveyTitle() {
    }

    public SurveyTitle(String title) {
        this.title = title;
    }

    public boolean valid(){
        return title != null && title.length() < 201 ;
    }

    public String getTitle() {
        return title;
    }
}
