package com.selaliadobor.githubeventbrowser.eventsource;

import com.selaliadobor.githubeventbrowser.githubapi.GithubAPI;
import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * An implementation of @{@link GithubEventSource} based on API responses from the GitHub API
 */

public class RetrofitGithubEventSource implements GithubEventSource {

    private static final int API_SUCCESS_RESPONSE_CODE = 200;

    private static final int PAGINATION_STARTING_PAGE = 1;

    private static final String LINK_HEADER = "Link";

    private static final String NEXT_HATEOS_URL = "rel=\"next\"";

    private final GithubAPI githubAPI;

    public RetrofitGithubEventSource(Retrofit retrofit) {
        githubAPI = retrofit.create(GithubAPI.class);
    }

    /**
     * @implNote Call immediately starts fetching results
     */
    @Override
    public Observable<Event> getEvents(String username, String repository) {
        PublishSubject<Event> eventPublishSubject = PublishSubject.create();

        listRepoEvents(username, repository, eventPublishSubject);

        return eventPublishSubject;
    }


    private void listRepoEvents(String name, String repo, final PublishSubject<Event> eventPublishSubject) {
        listRepoEvents(name, repo, eventPublishSubject, PAGINATION_STARTING_PAGE);
    }

    /**
     *  Recursively calls GitHub API with consecutive page numbers, adding events found to a PublishSubject
     */
    private void listRepoEvents(String name, String repo, final PublishSubject<Event> eventPublishSubject, final int startingPage) {
        githubAPI.listEvents(name, repo, startingPage)
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.code() != API_SUCCESS_RESPONSE_CODE || response.body() == null) {
                            eventPublishSubject.onError(new Exception("Failed to get response: " + response.message()));
                            return;
                        }

                        for (Event event : response.body()) {
                            eventPublishSubject.onNext(event);
                        }

                        List<String> linkHeaders = response.headers().values(LINK_HEADER);
                        if (linkHeaders.size() > 0 && linkHeaders.get(0).contains(NEXT_HATEOS_URL)) {
                            listRepoEvents(name, repo, eventPublishSubject, startingPage + 1);
                        } else {
                            eventPublishSubject.onComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        eventPublishSubject.onError(t);
                    }
                });
    }
}
