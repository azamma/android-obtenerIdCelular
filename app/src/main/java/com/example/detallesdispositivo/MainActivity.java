package com.example.detallesdispositivo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TelephonyManager telephonyManager;
    WifiManager wifi;
    TextView serial;
    TextView macWifi;
    Button boton;

    private static final int
            REQUEST_READ_PHONE_STATE = 110,
            REQUEST_WIFI_STATE = 111,
            REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serial = findViewById(R.id.serialID);
        macWifi = findViewById(R.id.macWifi);
        boton = findViewById(R.id.botonPermisos);
        boton.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botonPermisos:
                permisos();
                break;
        }

    }

    @SuppressLint("HardwareIds")
    private void permisos() {

        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //request permission
        boolean hasPermissionPhoneState = (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermissionPhoneState) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            @SuppressLint("HardwareIds") String imeiNumber = telephonyManager.getDeviceId();
            serial.setText(imeiNumber);
        }

        boolean hasPermissionLocation = (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    REQUEST_WIFI_STATE);
        } else {
            WifiInfo info = wifi.getConnectionInfo();
            macWifi.setText(info.getMacAddress());
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    @SuppressLint("HardwareIds") String imeiNumber = telephonyManager.getDeviceId();
                    serial.setText(imeiNumber);
                } else
                {
                    Toast.makeText(this, "The app was not allowed to get your phone state. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }

                break;

          case REQUEST_WIFI_STATE:{
              if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
              {
                  Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                  WifiInfo info = wifi.getConnectionInfo();
                  macWifi.setText(info.getMacAddress());
              } else
              {
                  Toast.makeText(this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
              }
          }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
