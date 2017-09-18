package com.selaliadobor.githubeventbrowser.eventsource;


import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;

import io.reactivex.Observable;

public interface GithubEventSource {
    /**
     * Returns an observable that lists events for a given repo
     *
     * @param username   The name of the owner of the repository
     * @param repository The name of the repository
     * @return An observable that will return repository events
     */
    Observable<Event> getEvents(String username, String repository);
}
