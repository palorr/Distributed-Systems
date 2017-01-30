package com.example.archo.finalproject;
//

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

//https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap#developer-guide
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //
    private GoogleMap mMap;
    //
    public String fd, sd, ip1, ip2, ip3;
    boolean flagp = false;
    boolean flagd = false;
    boolean flagi = false;
    LatLng southwest, northeast, southeast, northwest;
    ArrayList<String> resultaa;
    private SharedPreferences pref;
    Marker[] markers = new Marker[10];
    String[] photos = new String[10];

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /////
        SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
        this.fd = shared.getString("fd", null);
        this.sd = shared.getString("sd", null);
        this.ip1 = shared.getString("ip1", null);
        this.ip2 = shared.getString("ip2", null);
        this.ip3 = shared.getString("ip3", null);
        /////
        Intent intent = getIntent();
        //
        pref = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        /////
        if (intent.getStringExtra("fd") != null && intent.getStringExtra("sd") != null) {
            this.fd = intent.getStringExtra("fd");
            this.sd = intent.getStringExtra("sd");
            editor.putString("fd", fd);
            editor.putString("sd", sd);
        }
        /////
        if (intent.getStringExtra("ip1") != null && intent.getStringExtra("ip2") != null && intent.getStringExtra("ip3") != null) {
            this.ip1 = intent.getStringExtra("ip1");
            this.ip2 = intent.getStringExtra("ip2");
            this.ip3 = intent.getStringExtra("ip3");
            editor.putString("ip1", ip1);
            editor.putString("ip2", ip2);
            editor.putString("ip3", ip3);
        }
        /////test////
        Log.w("activity_maps", "dates: " + fd + " " + sd + " ");
        Log.w("activity_maps", "ips:" + ip1 + " " + ip2 + " " + ip3 + " ");
        /////////////
        if (fd != null && sd != null) {
            flagd = true;
            editor.putBoolean("flagd", flagd);
            Log.w("activity_maps", "flagd is true");

        } else flagd = false;
        //
        if (ip1 != null && ip2 != null && ip3 != null) {
            flagi = true;
            editor.putBoolean("flagd", flagd);
            Log.w("activity_maps", "flagi is true");
        } else flagi = false;
        //
        editor.commit();

    }

    //
    @Override
    public void onMapReady(GoogleMap googleMap) {

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        mMap = googleMap;
        //
        LatLng newYork = new LatLng(40.7590403, -74.0392705);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newYork));
        //
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
        //
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //
        final Button button = (Button) findViewById(R.id.setdates);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagp = false;
                Intent i = new Intent(MapsActivity.this, setDates.class);
                startActivity(i);
            }
        });
        ///////
        final Button button3 = (Button) findViewById(R.id.mip);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagp = false;
                Intent j = new Intent(MapsActivity.this, ips.class);
                startActivity(j);
            }
        });
        ///////
        final Button button2 = (Button) findViewById(R.id.execute);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////all the execute code is here !!!!!!///////
                if (flagd && flagp && flagi) {//an exw kai tetragwno kai hmeromhnies
                    new execute().execute(northeast, southeast, northwest, southwest);
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    Log.w("activity_maps", "ksekinhse to async task");
                }
                /////////////////////////////////////////////////
            }
        });
        ///////
        //-73.68382519820906, -74.27476644515991, 40.988331719265304, 40.55085246740427
        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(40.988331719265304, -73.68382519820906), new LatLng(40.988331719265304, -74.27476644515991), new LatLng(40.55085246740427, -74.27476644515991), new LatLng(40.55085246740427, -73.68382519820906))
                .strokeColor(Color.BLACK));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(40.988331719265304, -73.68382519820906), new LatLng(40.988331719265304, -74.27476644515991), new LatLng(40.55085246740427, -74.27476644515991), new LatLng(40.55085246740427, -73.68382519820906))
                        .strokeColor(Color.BLACK));
                //mMap.addMarker(new MarkerOptions().position(point));
                southwest = SphericalUtil.computeOffset(point, 6000 * Math.sqrt(2.0), 225);
                northeast = SphericalUtil.computeOffset(point, 6000 * Math.sqrt(2.0), 45);
                southeast = SphericalUtil.computeOffset(point, 6000 * Math.sqrt(2.0), 135);
                northwest = SphericalUtil.computeOffset(point, 6000 * Math.sqrt(2.0), 315);
                mMap.addPolygon(new PolygonOptions()
                        .add(northwest, northeast, southeast, southwest)
                        .strokeColor(Color.BLACK));
                flagp = true;
                Log.w("activity_maps", "flagp is true");

            }
        });


    }
    //

    //
    private class execute extends AsyncTask<LatLng, Integer, ArrayList<String>> {
        ////
        @Override
        protected ArrayList<String> doInBackground(LatLng... params) {
            Log.w("activity_maps", "doInBackground just started");

            /////

            double maxlo = -73.68382519820906;
            double minlo = -74.27476644515991;
            double maxla = 40.988331719265304;
            double minla = 40.55085246740427;

            /////////////////////////////////
            double tmp = (maxlo - minlo) / 21;
            square s1 = new square(minla, minlo, maxla, minlo + (tmp * 10));
            square s2 = new square(minla, minlo + (tmp * 10), maxla, minlo + (tmp * 11));
            square s3 = new square(minla, minlo + (tmp * 11), maxla, minlo + (tmp * 21));

            String string1 = s1.getString();

            Log.w("activity_maps", string1);
            String string2 = s2.getString();
            Log.w("activity_maps", string2);
            String string3 = s3.getString();
            Log.w("activity_maps", string3);
            /////////////////////////////////
            //polygon coos
            double fla = southeast.latitude; /////min lat
            double slo = southeast.longitude;////max long
            double flo = northwest.longitude;/////min long
            double sla = northwest.latitude;/////max lat
            ///////////
            square s4 = new square(fla, flo, sla, slo);
            String string4 = s4.getString();
            Log.w("activity_maps", string4);
            Date d1;
            Date d2;
            //////////////////////
            String line = fd;
            String[] parts = line.split("[-: ]");
            d1 = new Date((Integer.parseInt(parts[0]) - 1900), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
            line = sd;
            parts = line.split("[-: ]");
            d2 = new Date((Integer.parseInt(parts[0]) - 1900), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
            /////////////////////
            Socket requestSocket1 = null;
            Socket requestSocket2 = null;
            Socket requestSocket3 = null;
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            String message;
            /////////////////////IP
            String IP1 = ip1;
            String IP2 = ip2;
            String IP3 = ip3;
            try {
                requestSocket1 = new Socket(InetAddress.getByName(IP1), Integer.parseInt("2221"));
                requestSocket2 = new Socket(InetAddress.getByName(IP2), Integer.parseInt("2222"));
                requestSocket3 = new Socket(InetAddress.getByName(IP3), Integer.parseInt("2223"));

                out = new ObjectOutputStream(requestSocket1.getOutputStream());
                in = new ObjectInputStream(requestSocket1.getInputStream());
                try {
                    message = (String) in.readObject();
                    System.out.println("Server says " + message);
                    out.writeObject(string1);
                    out.flush();
                    out.writeObject(string4);
                    out.flush();
                    out.writeObject(d1);
                    out.flush();
                    out.writeObject(d2);
                    out.flush();
                } catch (ClassNotFoundException classNot) {
                    System.out.println("data received in unknown format");
                }
                ///////
                out = new ObjectOutputStream(requestSocket2.getOutputStream());
                in = new ObjectInputStream(requestSocket2.getInputStream());
                try {
                    message = (String) in.readObject();
                    System.out.println("Server says " + message);
                    out.writeObject(string2);
                    out.flush();
                    out.writeObject(string4);
                    out.flush();
                    out.writeObject(d1);
                    out.flush();
                    out.writeObject(d2);
                    out.flush();
                } catch (ClassNotFoundException classNot) {
                    System.out.println("data received in unknown format");
                }
                ///////
                out = new ObjectOutputStream(requestSocket3.getOutputStream());
                in = new ObjectInputStream(requestSocket3.getInputStream());
                try {
                    message = (String) in.readObject();
                    System.out.println("Server says " + message);
                    out.writeObject(string3);
                    out.flush();
                    out.writeObject(string4);
                    out.flush();
                    out.writeObject(d1);
                    out.flush();
                    out.writeObject(d2);
                    out.flush();
                } catch (ClassNotFoundException classNot) {
                    System.out.println("data received in unknown format");
                }

            } catch (UnknownHostException unknownHost) {
                System.out.println("You are trying to connect to an unknown host!");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                try {

                    out.close();
                    requestSocket1.close();
                    requestSocket2.close();
                    requestSocket3.close();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////perimenoume apotelesmata reducer/////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ServerSocket providerSocket = null;
            Socket connection = null;
            Object message2 = null;
            ArrayList<String> stringreceived = new ArrayList<String>();
            boolean flagreceived = false;
            //
            try {
                providerSocket = new ServerSocket(1111);
                connection = providerSocket.accept();
                ObjectOutputStream out2 = new ObjectOutputStream(connection.getOutputStream());

                ObjectInputStream in2 = new ObjectInputStream(connection.getInputStream());

                do {
                    try {

                        message2 = in2.readObject();

                        stringreceived = (ArrayList<String>) message2;
                        flagreceived = true;


                    } catch (ClassNotFoundException classnot) {
                        System.err.println("Data received in unknown format");
                    }
                } while (flagreceived == false);
                in.close();
                connection.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                try {
                    providerSocket.close();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Log.w("activity_maps", "thewritika teleiwse to doinbackground");
            return stringreceived;
        }

        ////
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            resultaa = result;
            String poiname;
            double longi;
            double lati;
            String photo = null;
            String[] strings = new String[resultaa.size()];
            for (int i = 0; i < resultaa.size(); i++) {
                strings[i] = resultaa.get(i);
                Log.w("activity_maps", strings[i]);
            }
            for (int j = 0; j < strings.length; j++) {
                String[] parts = strings[j].split("&&");
                poiname = parts[0];
                longi = Double.parseDouble(parts[1]);
                lati = Double.parseDouble(parts[2]);
                photo = parts[3];
                photos[j]= photo ;
                markers[j] = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lati, longi))
                        .title(poiname));
                markers[j].showInfoWindow();


            }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    for(int k=0;k<10;k++){
                        if(marker.equals(markers[k])){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photos[k]));
                            startActivity(browserIntent);
                        }
                    }


                }
            });
            Log.w("activity_maps", "EXW APOTELESMATA");

        }
    }

}