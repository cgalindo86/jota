package com.ubiporspy.keeperprotector;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ubiporspy.keeperprotector.fragments.CountryFragment;
import com.ubiporspy.keeperprotector.util.Data;
import com.ubiporspy.keeperprotector.util.LocationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements
        CountryFragment.CountryListener, LocationManager.OnLocationManagerInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnCountry)
    Button btnCountry;
    @BindView(R.id.edtPhoneCode)
    EditText edtPhoneCode;
    @BindView(R.id.edtPhoneNumber)
    EditText edtPhoneNumber;
    @BindView(R.id.edtIMEI)
    EditText edtIMEI;
    @BindView(R.id.edtIMEI2)
    EditText edtIMEI2;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.fieldEmail)
    EditText fieldEmail;

    @BindView(R.id.edtDni)
    EditText fieldDni;
    @BindView(R.id.edtUsername)
    EditText fieldUsername;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.layoutProgress)
    View layoutProgress;

    private boolean country = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        edtIMEI.setText("1111");
        edtIMEI2.setText("1112");

        edtIMEI.setVisibility(View.GONE);
        edtIMEI2.setVisibility(View.GONE);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.btnCountry, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCountry:

                FragmentManager fm = getSupportFragmentManager();
                CountryFragment moveToDialogFragment = new CountryFragment();
                moveToDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                moveToDialogFragment.show(fm, "");

                break;
            case R.id.btnRegister:

                if (country) {
                    if (!edtPhoneNumber.getText().toString().isEmpty()) {
                        if (!edtIMEI.getText().toString().isEmpty()) {
                            if (!fieldEmail.getText().toString().isEmpty()) {
                                if (!fieldDni.getText().toString().isEmpty()) {
                                    if (!fieldUsername.getText().toString().isEmpty()) {
                                        layoutProgress.setVisibility(View.VISIBLE);

                                        PackageManager p = getPackageManager();
                                        ComponentName componentName = new ComponentName(this, SplashActivity.class);
                                        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                                        LocationManager.getLastLocation(
                                                RegisterActivity.this,
                                                RegisterActivity.this);

                                    } else {
                                        Toast.makeText(this, "Ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, "Ingrese su DNI", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Ingrese su email.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Ingresar código IMEI.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ingresar número de teléfono.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Seleccionar País.", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void setCountry(String name, String code) {
        country = true;
        btnCountry.setText(name);
        edtPhoneCode.setText(code);
    }

    @Override
    public void onLocationResult(Location location) {
        int protection;
        if (radioGroup.getCheckedRadioButtonId() == R.id.optionBasic) {
            protection = Data.PROTECTION_BASIC;
        } else {
            protection = Data.PROTECTION_COMPLETE;
        }

        Data.sendEmailUserRegistered(this,
                location,
                btnCountry.getText().toString(),
                edtPhoneCode.getText().toString(),
                edtPhoneNumber.getText().toString(),
                fieldDni.getText().toString(),
                fieldUsername.getText().toString(),
                fieldEmail.getText().toString(),
                protection);
        Data.sendEmailRegisteredSuccessful(this,
                fieldEmail.getText().toString());

        SharedPreferences sharedPreferences = getSharedPreferences("KeeperProtector", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneNumber", edtPhoneNumber.getText().toString());
        editor.putString("imei", edtIMEI.getText().toString());
        editor.putString("email", fieldEmail.getText().toString());
        editor.putString("password", "JOSE2018Pe");
        editor.apply();

        startActivity(new Intent(this, ControlActivity.class));
        finish();
    }
}