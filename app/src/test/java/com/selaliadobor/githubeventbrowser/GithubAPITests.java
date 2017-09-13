package com.selaliadobor.githubeventbrowser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GithubAPITests {
    @Test
    public void parsesEventsCorrectly() throws Exception {
        InputStream mockResponseStream = this.getClass().getClassLoader().getResourceAsStream("mockApiResponse.json");
        byte[] mockResponse = IOUtils.readFully(mockResponseStream, mockResponseStream.available());

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonTypeAdaptorFactory.create())
                .create();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(chain ->
            new okhttp3.Response.Builder()
                    .code(200)
                    .message("Returning mock data")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), mockResponse))
                    .addHeader("content-type", "application/json")
                    .build()
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GithubAPI api = retrofit.create(GithubAPI.class);
        Response<List<Event>> response = api.listEvents("any", "any", 1).execute();
        List<Event> events = response.body();
        assertEquals(3, events.size());
        assertEquals("WatchEvent",events.get(0).type());
        assertEquals("ForkEvent",events.get(1).type());
        assertEquals("IssueCommentEvent",events.get(2).type());
    }
}