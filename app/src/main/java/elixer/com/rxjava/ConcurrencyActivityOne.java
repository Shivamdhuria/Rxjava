package elixer.com.rxjava;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConcurrencyActivityOne extends AppCompatActivity {
    public static final String TAG = "Main Activity";
    Button buttonStart;
    ProgressBar progressBar;
    private CompositeDisposable disposables;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concurrency_one);
        buttonStart = findViewById(R.id.button_start);
        progressBar = findViewById(R.id.progressBar);
        disposables = new CompositeDisposable();
        layout = findViewById(R.id.layout);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTask();
            }
        });

    }

    private void startTask() {
        Observable<String> colorObservable = Observable
                .fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return fetchRandomColor();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        colorObservable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(String s) {
                layout.setBackgroundColor(Color.parseColor(s));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }


    static String fetchRandomColor() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#FFA07A");
        colors.add("#FFD700");
        colors.add("#7FFF00");
        colors.add("#00FFFF");
        colors.add("#6A5ACD");
        return colors.get((int) (Math.random() * 5));
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
