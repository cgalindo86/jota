package com.ubiporspy.keeperprotector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                while (jumpTime < 100) {
                    try {
                        sleep(100);
                        jumpTime += 5;
                        if (jumpTime == 100) {
                            SharedPreferences sharedPreferences = getSharedPreferences("KeeperProtector", Context.MODE_PRIVATE);

                            if (!sharedPreferences.getString("imei", "").isEmpty()) {
                                startActivity(new Intent(SplashActivity.this, ControlActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
}
