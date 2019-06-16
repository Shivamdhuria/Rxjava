package elixer.com.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class PaginationActivityThree extends AppCompatActivity {

    public static final String TAG = PaginationActivityThree.class.getSimpleName();
    private CompositeDisposable disposables;
    RestClient restClient;
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ProgressBar progressBar;
    private boolean loading = false;
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager layoutManager;
    private PublishProcessor<Integer> mPublishProcessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination_three);
        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        mPublishProcessor = PublishProcessor.create();

        restClient = new RestClient(this);

        disposables = new CompositeDisposable();

        initRecyclerView();
        initObservable();
        setUpLoadMoreListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    /**
     * setting listener to get callback for load more
     */
    private void setUpLoadMoreListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager
                        .findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    pageNumber++;
                    Log.d(TAG, "onScrolled: .....");
                    mPublishProcessor.onNext(pageNumber);
                    loading = true;
                }
            }
        });
        Log.d(TAG, "onScrolled: .........");
        mPublishProcessor.onNext(pageNumber);
    }


    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @SuppressLint("CheckResult")
    private void initObservable() {
        Log.d(TAG, "initObservable: init");
        mPublishProcessor
                .doOnNext(page -> {
                    loading = true;
                    progressBar.setVisibility(View.VISIBLE);
                })
                .concatMapSingle(page -> dataFromNetwork(page)
                        .subscribeOn(Schedulers.io())
                        .doOnError(throwable -> {
                            // handle error
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    mAdapter.addItems(items);
                    mAdapter.notifyDataSetChanged();
                    loading = false;
                    progressBar.setVisibility(View.INVISIBLE);
                });

//                .subscribe(new FlowableSubscriber<List<String>>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<String> strings) {
//                        Log.d(TAG, "onNext: .....");
//                        mAdapter.addItems(strings);
//                        mAdapter.notifyDataSetChanged();
//                        loading = false;
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        disposables.add(disposable);

        mPublishProcessor.onNext(pageNumber);

    }

//    private List<String> dataFromNetwork(final int page) {
//        Log.d(TAG, "dataFromNetwork: data from network");
//        return restClient.getCitiesByPage(page);
//
//    }

    private Single<List<String>> dataFromNetwork(final int page) {
        return Single.just(true)
                .delay(2, TimeUnit.SECONDS)
                .map(value -> {
                    List<String> items = new ArrayList<>();
                    for (int i = 1; i <= 10; i++) {
                        items.add("Item " + (page * 10 + i));
                    }
                    return items;
                });
    }
}
