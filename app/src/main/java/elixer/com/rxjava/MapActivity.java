package elixer.com.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MapActivity extends AppCompatActivity {

    private RestClient mRestClient;
    private EditText mSearchInput;
    private TextView mNoResultsIndicator;
    private RecyclerView mSearchResults;
    private RecyclerViewAdapter mSearchResultsAdapter;

    private PublishSubject<String> mSearchResultsSubject;
    private Subscription mTextWatchSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient(this);
        configureLayout();
        createObservables();
        listenToSearchInput();
    }


    private void createObservables() {
        mSearchResultsSubject = PublishSubject.create();
        mTextWatchSubscription = mSearchResultsSubject
//                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        return mRestClient.searchForCity(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Object o) {
                        {
                            handleSearchResults(cities);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }




                });
    }
                );
    }


    private void handleSearchResults(List<String> cities) {
        if (cities.isEmpty()) {
            showNoSearchResults();
        } else {
            showSearchResults(cities);
        }
    }

    private void showNoSearchResults() {
        mNoResultsIndicator.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(View.GONE);
    }

    private void showSearchResults(List<String> cities) {
        mNoResultsIndicator.setVisibility(View.GONE);
        mSearchResults.setVisibility(View.VISIBLE);
        mSearchResultsAdapter.setStrings(cities);
    }

    private void listenToSearchInput() {
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchResultsSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void configureLayout() {
        setContentView(R.layout.activity_map);
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mNoResultsIndicator = (TextView) findViewById(R.id.no_results_indicator);
        mSearchResults = (RecyclerView) findViewById(R.id.search_results);
        mSearchResults.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsAdapter = new RecyclerViewAdapter(this);
        mSearchResults.setAdapter(mSearchResultsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
