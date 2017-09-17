package com.selaliadobor.githubeventbrowser.githubapi.responseobjects;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Represents an user on GitHub
 */
@AutoValue
public abstract class Actor {
    /**
     * An non-changing id assigned by GitHub
     */
    @SerializedName("id")
    public abstract double id();

    /**
     * The modifiable name of the user referenced by the @{@link Actor#id()} field
     */
    @SerializedName("login")
    public abstract String login();

    /**
     * The url to the user's Gravatar
     *
     * @apiNote If the user does not use a Gravatar this value will be null
     */
    @SerializedName("gravatar_id")
    public abstract String gravatarId();

    /**
     * The url to the user's GitHub profile
     */
    @SerializedName("url")
    public abstract String url();

    /**
     * The url to the user's Avatar image
     */
    @SerializedName("avatar_url")
    public abstract String avatarUrl();


    public static TypeAdapter<Actor> typeAdapter(Gson gson) {
        return new AutoValue_Actor.GsonTypeAdapter(gson);
    }
}
