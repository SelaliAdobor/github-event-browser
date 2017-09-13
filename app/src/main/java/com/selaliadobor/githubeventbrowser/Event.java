package com.selaliadobor.githubeventbrowser;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

@AutoValue
public abstract class Event {
    @SerializedName("id")
    public abstract double id();

    @SerializedName("type")
    public abstract String type();

    @SerializedName("actor")
    public abstract Actor actor();

    @SerializedName("repo")
    public abstract Repo repo();

    @SerializedName("created_at")
    public abstract String createdAt();

    @SerializedName("payload")
    public abstract Map<String,Object> payload();

    public static TypeAdapter<Event> typeAdapter(Gson gson) {
        return new AutoValue_Event.GsonTypeAdapter(gson);
    }
}
