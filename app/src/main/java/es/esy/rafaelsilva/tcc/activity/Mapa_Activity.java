package es.esy.rafaelsilva.tcc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Historico;
import es.esy.rafaelsilva.tcc.util.Util;

public class Mapa_Activity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "Mapa_Activity";
    private GoogleMap mMap;
    String numero = "";
    double [] latitude;//= {-21.671406};
    double [] longitude;// = {-49.726823};
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location l;
    private LocationManager gps;
    private boolean isOn;
    List<Historico> hist;
    private String cordenadas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_);
//        if(getIntent().getStringExtra("local") != null){
//           // buscaCoordenadas(getIntent().getStringExtra("lote"));
//        }
//        verificarGps();
        //******************************************************
        if (getIntent().getStringExtra("historico") != null) {
            Gson gson = new Gson();
            String json = getIntent().getStringExtra("historico");
            Type type = new TypeToken<List<Historico>>() {}.getType();
            hist = gson.fromJson(json, type);
        }else{
            cordenadas = getIntent().getStringExtra("cordenadas");
        }
        //*******************************************************
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
//            startLocationUpdate();
//        }

    }
    private void verificarGps() {
        gps = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        isOn = gps.isProviderEnabled( LocationManager.GPS_PROVIDER);
        if (isOn){
            callConnection();
        }else{

            Toast.makeText(this, "Você precisara ativar o gps do seu aparelho!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            startLocationUpdate();
        }
    }
    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


    }
    public void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            MarkerOptions options = new MarkerOptions();

            if (hist != null) {
                for (Historico h : hist) {
                    String[] cordenadas = h.getCordenadas().split(", ");
                    LatLng local = new LatLng(Double.parseDouble(cordenadas[0]), Double.parseDouble(cordenadas[1]));
                    options.position(local);
                    options.title(Util.formatDataDDmesYYYY(h.getData())).visible(true);
                    options.snippet(h.getNome()).visible(true);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(local));
                    googleMap.addMarker(options);
                }
            }else{
                String[] cordenadas = this.cordenadas.split(", ");
                LatLng local = new LatLng(Double.parseDouble(cordenadas[0]), Double.parseDouble(cordenadas[1]));
                options.position(local);
                options.title(Util.formatDataDDmesYYYY(getIntent().getStringExtra("data"))).visible(true);
                options.snippet(getIntent().getStringExtra("nome")).visible(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(local));
                googleMap.addMarker(options);
            }
            googleMap.setMinZoomPreference(10);

        }catch(SecurityException ex){
            Log.e(TAG, "Error", ex);
        }

    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LOG", "onConnected(" + bundle + ")");


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (l != null) {
            Log.i("LOG", "Latitude: " + l.getLatitude());
            Log.i("LOG", "Longitude: " + l.getLongitude());
            Toast.makeText(this, "Latitude: " + l.getLatitude() + "\nLongitude: " + l.getLongitude(), Toast.LENGTH_LONG).show();
        }


        startLocationUpdate();

        Toast.makeText(this, "Latitude: " + l.getLatitude() + "\nLongitude: " + l.getLongitude(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i +")");
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude[0] = location.getLatitude();
        longitude[0] = location.getLongitude();
    }
    private void startLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
