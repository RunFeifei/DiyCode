package com.example.root.okfit.CrRxbus;

/**
 * Created by PengFeifei on 17-5-12.
 */

public final class CrBusEvent {

    private static int EVENT_ID = 0;
    private int eventId;
    private String content;


    public CrBusEvent(@EventId int eventId, String content) {
        this.eventId=eventId;
        this.content=content;
    }

    public @interface EventId {
        int EVENT_LOGGED_IN = getAutoEeventId();
        int EVENT_LOGGED_OUT = getAutoEeventId();

        int EVENT_MAINPAGE_CREDIT = getAutoEeventId();
        int EVENT_MAINPAGE_DETECTIVE = getAutoEeventId();
        int EVENT_MAINPAGE_TOOL = getAutoEeventId();
    }

    private static int getAutoEeventId() {
        return EVENT_ID--;
    }

    public int getEventId() {
        return eventId;
    }

    public String getContent() {
        return content;
    }
}
