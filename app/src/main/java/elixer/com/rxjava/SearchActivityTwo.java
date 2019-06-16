package elixer.com.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchActivityTwo extends AppCompatActivity {
    public static final String TAG = "MapActivity";
    RestClient restClient;
    private EditText mEditTextView;
    private TextView mNoResultsTextview;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private PublishSubject<String> mPublishSubject;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_two);
        restClient = new RestClient(this);
        mEditTextView = findViewById(R.id.imput_edittext);
        mNoResultsTextview = findViewById(R.id.no_results_textview);
        mRecyclerView = findViewById(R.id.recycler_view);
        disposables = new CompositeDisposable();
        initRecyclerView();
        initObservable();
        listenToSearchInput();
    }

    @SuppressLint("CheckResult")
    private void initObservable() {
        mPublishSubject = PublishSubject.create();
        mPublishSubject
//                .debounce(400, TimeUnit.MILLISECONDS)
                .map(searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        handleSearchResults(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void handleSearchResults(List<String> strings) {
        if (strings.isEmpty()) {
            showNoSearchResults();
        } else {
            showSearchResults(strings);
        }
    }

    private void showNoSearchResults() {
        mNoResultsTextview.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showSearchResults(List<String> cities) {
        mNoResultsTextview.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.setStrings(cities);
    }


    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    Function<String, List<String>> searchString = new Function<String, List<String>>() {
        @Override
        public List<String> apply(String s) throws Exception {
            return restClient.searchForCity(s);
        }

    };

    private void listenToSearchInput() {
        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPublishSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        mRecyclerView.setAdapter(null);
        super.onDestroy();
    }
}
