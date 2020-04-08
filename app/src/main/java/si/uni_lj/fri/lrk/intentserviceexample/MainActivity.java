package si.uni_lj.fri.lrk.intentserviceexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private TextView mTextView;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            if (b != null) {
                String str = b.getString(DownloadService.FILEPATH);
                int resultCode = b.getInt(DownloadService.RESULT);
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this,
                            "Download complete at: "+str, Toast.LENGTH_LONG).show();
                    mTextView.setText("Download done!");
                } else {
                    Toast.makeText(MainActivity.this,
                            "Download failed!", Toast.LENGTH_LONG).show();
                    mTextView.setText("Failed!");
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.status);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(DownloadService.REPORT));
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.FILENAME, "index.html");
        intent.putExtra(DownloadService.URL,"https://developer.android.com/" );
        startService(intent);
        mTextView.setText("Service started...");
    }
}
