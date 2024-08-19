package org.survy.domain.applicationservice.dto.create;

import org.survy.domain.core.valueObject.QuotaActionTypes;

public class QuotaActionRequest {

    private String actionType;
    private String url;
    private String customMessage;

    public QuotaActionRequest() {
    }

    public QuotaActionRequest(String actionType, String url, String customMessage) {
        this.actionType = actionType;
        this.url = url;
        this.customMessage = customMessage;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }


    @Override
    public String toString() {
        return "QuotaActionRequest{" +
                "actionType='" + actionType + '\'' +
                ", url='" + url + '\'' +
                ", customMessage='" + customMessage + '\'' +
                '}';
    }
}
