package si.uni_lj.fri.lrk.intentserviceexample;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class DownloadService extends IntentService {

    public static final String TAG = "DownloadService";

    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";

    public static final String REPORT = "si.uni_lj.fri.lrk.intentservicetest.REPORT";

    private int result = Activity.RESULT_CANCELED;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        File output = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName);

        InputStream stream = null;
        FileOutputStream fos = null;

        int result = Activity.RESULT_CANCELED;

        try {
            java.net.URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos=new FileOutputStream(output);

            int next = -1;
            while ((next=reader.read())!=-1){
                fos.write(next);
            }
            result = Activity.RESULT_OK;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();

                    DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
                    downloadManager.addCompletedDownload(output.getName(), output.getName(), true, "text/html",output.getAbsolutePath(),output.length(),true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        publishResults(output.getAbsolutePath(), result);
    }

    void publishResults(String outputPath, int result) {
        Intent intent = new Intent(REPORT);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }


}