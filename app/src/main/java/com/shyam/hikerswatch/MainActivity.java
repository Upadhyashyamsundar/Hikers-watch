package com.shyam.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        }

    }

    public  void locationupdateinfo(Location location){



        TextView latitude=(TextView)findViewById(R.id.lattitude);

        TextView longitude=(TextView)findViewById(R.id.longitude);

        TextView altitude=(TextView)findViewById(R.id.altitude);

        TextView accurecy=(TextView)findViewById(R.id.accurecy);

        latitude.setText("Latitude:"+location.getLatitude());
        longitude.setText("Longitude:"+location.getLongitude());
        altitude.setText("Altitude:"+location.getAltitude());
        accurecy.setText("Accurecy:"+location.getAccuracy());

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            String result="Could not find Address";

            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if ((addressList!=null && addressList.size()>0)){

                result="";


                if(addressList.get(0).getCountryName()!=null){

                    result+=addressList.get(0).getCountryName()+"\n";
                }
                if(addressList.get(0).getAdminArea()!=null){

                    result+=addressList.get(0).getAdminArea()+"\n";
                }
                if(addressList.get(0).getLocality()!=null){

                    result+=addressList.get(0).getLocality()+"\n";
                }
                if(addressList.get(0).getSubLocality()!=null){

                    result+=addressList.get(0).getSubLocality()+"\n";
                }
                if(addressList.get(0).getPostalCode()!=null){

                    result+=addressList.get(0).getPostalCode()+"\n";
                }
                if(addressList.get(0).getThoroughfare()!=null){

                    result+=addressList.get(0).getThoroughfare()+" ";
                }
                if(addressList.get(0).getFeatureName()!=null){

                    result+=addressList.get(0).getFeatureName()+" ";
                }

            }

            TextView addresstextview=(TextView)findViewById(R.id.address);
            addresstextview.setText("\nAddress: \n"+result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                locationupdateinfo(location);
            }
        };

        if(Build.VERSION.SDK_INT<23){

           startListening();
        }else{

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{

                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

                Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location!=null){

                    locationupdateinfo(location);
                }

            }


        }





    }
}