package com.selaliadobor.githubeventbrowser.githubapi.responseobjects;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Represents an event that occurs within a GitHub repository
 */
@AutoValue
public abstract class Event {
    /**
     * An non-changing id assigned by GitHub
     */
    @SerializedName("id")
    public abstract double id();

    /**
     * The type of event that occurred
     */
    @SerializedName("type")
    public abstract String type();

    /**
     * The user (or "actor") that caused the event
     */
    @SerializedName("actor")
    public abstract Actor actor();

    /**
     * The repository the event occurred on
     */
    @SerializedName("repo")
    public abstract Repo repo();

    /**
     * The ISO 8601 date the event occurred on
     */
    @SerializedName("created_at")
    public abstract String createdAt();

    /**
     * A generic payload field containing different data depending on the event type
     */
    @SerializedName("payload")
    public abstract Map<String, Object> payload();

    public static TypeAdapter<Event> typeAdapter(Gson gson) {
        return new AutoValue_Event.GsonTypeAdapter(gson);
    }
}
