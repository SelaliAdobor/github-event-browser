package com.selaliadobor.githubeventbrowser;

import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;

/**
 * Created by selaliadobor on 9/17/17.
 */

public interface EventFilter {
    boolean isValidEvent(Event event);
}
