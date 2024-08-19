package org.survy.domain.core.valueObject;

import java.util.Date;


public class CloseDate {

    private Date date ;

    public CloseDate() {
    }

    public CloseDate(Date date) {
        this.date = date;
    }


    public boolean inFutureTime(){
        return date.after(new Date());
    }

    public boolean inPastTime(){
        return !inFutureTime();
    }


    public Date getDate() {
        return date;
    }
}
