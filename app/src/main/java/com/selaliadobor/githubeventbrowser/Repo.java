package com.selaliadobor.githubeventbrowser;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

@AutoValue
public abstract class Repo {
    @SerializedName("login")
    public abstract double id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("url")
    public abstract String url();

    public static TypeAdapter<Repo> typeAdapter(Gson gson) {
        return new AutoValue_Repo.GsonTypeAdapter(gson);
    }
}
