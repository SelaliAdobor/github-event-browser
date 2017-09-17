package com.selaliadobor.githubeventbrowser.githubapi.responseobjects;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a GitHub repository
 */
@AutoValue
public abstract class Repo {
    /**
     * An non-changing id assigned by GitHub
     */
    @SerializedName("id")
    public abstract double id();

    /**
     * The name of the repository referenced by the @{@link Repo#id()} field
     */
    @SerializedName("name")
    public abstract String name();

    /**
     * The url to the repository referenced by the @{@link Repo#id()} field
     */
    @SerializedName("url")
    public abstract String url();

    public static TypeAdapter<Repo> typeAdapter(Gson gson) {
        return new AutoValue_Repo.GsonTypeAdapter(gson);
    }
}
