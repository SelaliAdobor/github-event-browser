package com.selaliadobor.githubeventbrowser;

import com.google.auto.value.AutoValue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubAPI {
    @GET("repos/{owner}/{repo}/events")
    Call<List<Event>> listEvents(@Path("owner") String owner, @Path("repo") String repo, @Query("page") int pageIndex);
}
