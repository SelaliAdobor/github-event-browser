package com.selaliadobor.githubeventbrowser;

import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.selaliadobor.githubeventbrowser.eventsource.GithubEventSource;
import com.selaliadobor.githubeventbrowser.eventsource.RetrofitGithubEventSource;
import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;
import com.selaliadobor.githubeventbrowser.layout.EventListItemLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.ganfra.materialspinner.MaterialSpinner;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.event_loading_progress_bar)
    ProgressBar eventLoadingProgressBar;
    @BindView(R.id.event_listing_lithoView)
    LithoView eventLithoView;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.error_textView)
    TextView errorTextView;
    @BindView(R.id.owner_edit_text)
    EditText ownerEditText;
    @BindView(R.id.repository_edit_text)
    EditText repositoryEditText;
    @BindView(R.id.event_type_spinner)
    MaterialSpinner filterSpinner;

    CompositeDisposable lifecycleDisposables = new CompositeDisposable();

    private Unbinder unbinder;

    private RecyclerBinder recyclerBinder;
    private Component<Recycler> recyclerComponent;
    private ComponentContext componentContext;
    private GithubEventSource githubEventSource;
    private Disposable listEventsDisposable;
    @Override
    protected void onStop() {
        super.onStop();
        lifecycleDisposables.clear();
        if(unbinder != null){
            unbinder.unbind();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        eventLoadingProgressBar.setVisibility(View.GONE);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonTypeAdapterFactory.create())
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(new OkHttpClient.Builder()
                        .addInterceptor(chain ->
                                chain.proceed(chain.request().newBuilder()
                                        .header("Authorization", Credentials.basic("SelaliAdobor", "tW2VUdHsAM8RXZ.96Tkr7VXj3PgwN4aoUTb2vJCTfP8gc6qhTa"))
                                        .build()))
                        .build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        githubEventSource = new RetrofitGithubEventSource(retrofit);

        setupViews();
    }

    private void setupViews() {
        setViewVisibility(ContentStatus.NONE);

        componentContext = new ComponentContext(this);

        recyclerBinder = new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(this, OrientationHelper.VERTICAL, false))
                .build(componentContext);


        recyclerComponent = Recycler.create(componentContext)
                .binder(recyclerBinder)
                .build();

        eventLithoView.setComponent(recyclerComponent);

        lifecycleDisposables.add(
            RxView
                .clicks(searchButton)
                .subscribe(o -> updateEventListing(), Throwable::printStackTrace)
        );
    }

    private void updateEventListing() {
        ArrayList<Event> eventList = new ArrayList<>();

        if(listEventsDisposable != null && !listEventsDisposable.isDisposed()){
            listEventsDisposable.dispose();
        }

        listEventsDisposable = githubEventSource
                .getEvents(ownerEditText.getText().toString(), repositoryEditText.getText().toString())
                .doOnSubscribe(disposable -> {
                    setViewVisibility(ContentStatus.LOADING);
                })
                .subscribe(
                        eventList::add,
                        this::showError,
                        () -> showEvents(eventList)
                );
        lifecycleDisposables.add(listEventsDisposable);
    }

    private void showEvents(ArrayList<Event> eventList) {
        setViewVisibility(ContentStatus.CONTENT);

        updateRecyclerContent(eventList,null);

        List<String> uniqueEventTypes = StreamSupport
                .stream(eventList)
                .map(Event::type)
                .distinct()
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, uniqueEventTypes);
        filterSpinner.setAdapter(adapter);

        lifecycleDisposables.add(
                RxAdapterView.itemSelections(filterSpinner)
                        .filter(index -> index > 0) //An unselected spinner has an index of -1
                        .map(uniqueEventTypes::get)
                        .subscribe(eventType ->
                                updateRecyclerContent(eventList, event -> event.type().equals(eventType)))
        );
    }

    private void setViewVisibility(ContentStatus status) {
        int loadingVisibility = status == ContentStatus.LOADING ? View.VISIBLE : View.INVISIBLE;
        eventLoadingProgressBar.setVisibility(loadingVisibility);

        int errorTextVisibility = status == ContentStatus.ERROR ? View.VISIBLE : View.INVISIBLE;
        errorTextView.setVisibility(errorTextVisibility);

        int contentVisibility = status == ContentStatus.CONTENT ? View.VISIBLE : View.INVISIBLE;
        eventLithoView.setVisibility(contentVisibility);
        filterSpinner.setVisibility(contentVisibility);
    }

    private void showError(Throwable throwable) {
        setViewVisibility(ContentStatus.ERROR);
        errorTextView.setText("Error: " + throwable.getMessage());
    }

    private void updateRecyclerContent(List<Event> events, EventFilter eventFilter) {
        List<Component<EventListItemLayout>> listItems = new ArrayList<>();
        for(Event event : events){

            if(eventFilter != null && !eventFilter.isValidEvent(event)){
                 continue;
            }

            Component<EventListItemLayout> layoutComponent = EventListItemLayout
                    .create(componentContext)

                    .event(event)
                    .build();
            listItems.add(layoutComponent);
        }
        recyclerBinder.removeRangeAt(0, recyclerBinder.getItemCount());
        for(int i = 0; i < listItems.size(); i++){
            recyclerBinder.insertItemAt(i,listItems.get(i));
        }
    }

    enum ContentStatus {
        NONE,
        LOADING,
        CONTENT,
        ERROR,
    }


}
