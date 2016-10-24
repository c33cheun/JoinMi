package com.android.joinmi.app.util;

import com.android.joinmi.app.classes.JoinMiEvent;

import java.util.List;

/**
 * Created by ccheung on 2016-10-23.
 */
public class UserPreferences {
    private List <JoinMiEvent> savedEvents;

    public void addEvent (JoinMiEvent event) {
        savedEvents.add(event);
    }
}
