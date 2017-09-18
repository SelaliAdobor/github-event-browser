package com.selaliadobor.githubeventbrowser.githubapi;

import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface representing Github API
 */
public interface GithubAPI {

    /**
     * Returns a list of events for the given repo and page
     *
     * @param owner     The user who owns the repository
     * @param repo      The name of the repository
     * @param pageIndex A page index used with pagination. Should not be greater than 10 (See notes)
     * @return A list of events for the repository
     * @implNote Github's API limits pagination for events to 10 pages for performance reasons
     */
    @GET("repos/{owner}/{repo}/events")
    Call<List<Event>> listEvents(@Path("owner") String owner, @Path("repo") String repo, @Query("page") int pageIndex);
}
