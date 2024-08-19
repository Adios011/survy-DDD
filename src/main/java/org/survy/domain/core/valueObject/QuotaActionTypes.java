package org.survy.domain.core.valueObject;

public enum QuotaActionTypes {

    DIRECT_URL , CLOSE_BROWSER , DISPLAY_CUSTOM_MESSAGE;


    public static  QuotaActionTypes from(String name){
        if(name.equalsIgnoreCase(DIRECT_URL.name()))
            return DIRECT_URL;
        else if(name.equalsIgnoreCase(CLOSE_BROWSER.name()))
            return CLOSE_BROWSER;
        else if(name.equalsIgnoreCase(DISPLAY_CUSTOM_MESSAGE.name()))
            return DISPLAY_CUSTOM_MESSAGE;
        else
            return null;
    }
}
