package org.survy.domain.core.valueObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("valid")
public class SurveyDescription {

    public SurveyDescription() {
    }

    private String description;

    public SurveyDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean valid(){
        return description != null && description.length() <= 1000;
    }
}
