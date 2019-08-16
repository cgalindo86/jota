package com.ubiporspy.keeperprotector;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cbTermsAndConditions)
    CheckBox cbTermsAndConditions;
    @BindView(R.id.btnNext)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setChecked(false);
    }

    @OnClick({R.id.btnNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
        }
    }

    @OnCheckedChanged(R.id.cbTermsAndConditions)
    public void OnCheckedChanged(CompoundButton button, boolean checked) {
        setChecked(button.isChecked());
    }

    public void setChecked(boolean checked) {
        if (checked) {
            /*
             * Pedimos los permisos por primera y única vez.
             *
             * Si el usuario lo ha permitido, enviareamos su ubicación APROXIMADA.
             * En caso contrario, se envía un mensaje "NO SE DIERON LOS PERMISOS".
             */
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    100);

            btnNext.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_blue, null));
        } else {
            btnNext.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_gray, null));
        }
        btnNext.setEnabled(checked);
    }
}