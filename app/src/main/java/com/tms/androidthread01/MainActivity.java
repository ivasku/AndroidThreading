package com.tms.androidthread01;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PERO";
    private Button buttonStart;

    //to change UI elements from we must use the Handler
    private Handler mainHandler = new Handler();

    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.button);
    }

    public void startThread(View view) {
        stopThread = false;

        /*TestThread thread = new TestThread(10);
        thread.start();**/

        TestRunnable runnable = new TestRunnable(10);
        //runnable.run(); //run on UI (main) thread
        new Thread(runnable).start();

    }

    public void stopThread(View view) {
        stopThread = true;
    }

    class TestThread extends Thread {
        private int seconds;

        public TestThread (int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                try {
                    Log.d(TAG, "startThread: " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class TestRunnable implements Runnable {
        private int seconds;

        public TestRunnable (int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                if (stopThread) {
                    return;
                }

                if (i == 5) {
                    //example 1
                    //buttonStart.setText("50%"); // if we change it here it will crash because only the theard that was the view created can be changed (main - UI thread)
                    //this is why we post this to the handler
                   /* mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStart.setText("50%");
                        }
                    });*/

                   //example 2
                    //we can also create the handler here but we must associate it with the main looper (thread)
                    /*Handler otherHander = new Handler(Looper.getMainLooper());

                    otherHander.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStart.setText("50%");
                        }
                    });*/

                    //example3
                    //we can make this without the Handler too, directly via UI element
                    /*buttonStart.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStart.setText("50%");
                        }
                    });*/

                    //example 4
                    //we can also just call runOnUiThread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonStart.setText("50%");
                        }
                    });

                }

                try {
                    Log.d(TAG, "startThread: " + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
