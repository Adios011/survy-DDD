package org.survy.domain.core.valueObject;


import org.survy.domain.core.exception.QuotaEnabledException;

public class QuotaAction {

    private QuotaActionTypes actionType;
    private String url;
    private String customMessage;


    public QuotaAction() {
    }

    public QuotaAction(QuotaActionTypes actionType , String url, String customMessage) {
        this.actionType = actionType;
        this.url = url;
        this.customMessage = customMessage;
    }


    public void act() {
        if (actionType == null)
            return;

        if (actionType.equals(QuotaActionTypes.DIRECT_URL))
            throw new QuotaEnabledException("DIRECT_URL#" + url);
        else if(actionType.equals(QuotaActionTypes.CLOSE_BROWSER))
            throw new QuotaEnabledException("CLOSE_BROWSER#");
        else if(actionType.equals(QuotaActionTypes.DISPLAY_CUSTOM_MESSAGE))
            throw new QuotaEnabledException("DISPLAY_CUSTOM_MESSAGE#" + customMessage);
        else
            return;

    }


    public QuotaActionTypes getActionType() {
        return actionType;
    }

    public void setActionType(QuotaActionTypes actionType) {
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
}
