package fr.loicleinot.imdb;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class IMDbService extends IntentService {
    public IMDbService() {
        super("IMDbService");
    }

    public void onCreate() {
        // Création du service
    }
    public void onDestroy() {
        // Destruction du service
    }

    @Deprecated // utile pour les versions antérieures d'Android 2.0
    public void onStart(Intent intent, int startId) {
        onStartCommand(intent, 0 , startId);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(IMDbService.this, "Hello....", Toast.LENGTH_SHORT).show();
                    } });
            }};
        timer.schedule(task, 0, 5000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
