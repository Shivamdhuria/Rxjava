package elixer.com.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonConcurrency;
    Button buttonMap;
    Button buttonPaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonConcurrency = findViewById(R.id.button);
        buttonMap = findViewById(R.id.button_map);
        buttonPaging= findViewById(R.id.button_fab);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SearchActivityTwo.class));
            }
        });
        buttonConcurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ConcurrencyActivityOne.class));
            }
        });
        buttonPaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), PaginationActivityThree.class));
            }
        });
    }

}
