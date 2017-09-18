package com.selaliadobor.githubeventbrowser;

import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;

/**
 * Defines a predicate that is used to filter Events shown to the user
 */

public interface EventFilter {
    /**
     * @implSpec Implementations should return true if the event should be shown to the user, false otherwise
     */
    boolean isValidEvent(Event event);
}
