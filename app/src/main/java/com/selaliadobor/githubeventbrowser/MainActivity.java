package com.selaliadobor.githubeventbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.event_loading_progress_bar)
    ProgressBar eventLoadingProgressBar;
    
    CompositeDisposable lifecycleDisposables = new CompositeDisposable();
    private Unbinder unbinder;


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
    }
}
