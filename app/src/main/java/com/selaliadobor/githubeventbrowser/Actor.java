package com.selaliadobor.githubeventbrowser;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Actor {

    @SerializedName("id")
    public abstract double id();

    @SerializedName("login")
    public abstract String login();

    @SerializedName("gravatar_id")
    public abstract String gravatarId();

    @SerializedName("url")
    public abstract String url();

    @SerializedName("avatar_url")
    public abstract String avatarUrl();


    public static TypeAdapter<Actor> typeAdapter(Gson gson) {
        return new AutoValue_Actor.GsonTypeAdapter(gson);
    }
}
