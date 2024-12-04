package scansort.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener {
//GoogleMap.OnInfoWindowClickListener,

    private GoogleMap mMap;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234; //code de requête de geolocalisation choisi au hasard
    private static final String TAG  = "Map Activity";
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mLocation;
    private static final float DEFAULT_ZOOM  = 14F;
    protected LocationManager locationManager;
    private GeoApiContext mGeoApiContext = null;
    //private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private Location mUserLocation;
    private ArrayList<PolylineData> mPolylineData = new ArrayList<>();
    private ArrayList<Marker> polylineMarkers = new ArrayList<>();
    //private Marker mSelectedMarker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }

    private void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Log.d(TAG, "initMap is ready");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setOnPolylineClickListener(this);
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera

        /*//Iinvisible marker
        Marker mmmm = mMap.addMarker(new MarkerOptions().position(new LatLng(48.874115, 2.376260)).title("CHEONG").snippet("Loïc").icon(BitmapDescriptorFactory.fromResource(R.drawable.invisible_marker))); //Belleville
        mmmm.showInfoWindow();*/
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.828325, 2.327114)).title("Poubelle blanche").snippet("Pots, bouteilles, bocaux en verre").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_blanche))); //Alésia
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.867040, 2.383152)).title("Poubelle blanche").snippet("Pots, bouteilles, bocaux en verre").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_blanche))); //Ménilmontant
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.867802, 2.364355)).title("Poubelle blanche").snippet("Pots, bouteilles, bocaux en verre").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_blanche))); //République
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.876214, 2.357059)).title("Poubelle blanche").snippet("Pots, bouteilles, bocaux en verre").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_blanche))); //Gare de l'EST

        mMap.addMarker(new MarkerOptions().position(new LatLng(48.882067, 2.382159)).title("Poubelle vert").snippet("Déchets non triés").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_vert)));//Mairie 19e
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.883473, 2.327782)).title("Poubelle vert").snippet("Déchets non triés").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_vert)));//Place de Clichy
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.855089, 2.347276)).title("Poubelle vert").snippet("Déchets non triés").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_vert)));//Cité
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.829260, 2.357533)).title("Poubelle vert").snippet("Déchets non triés").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_vert)));//Place d'Italie

        mMap.addMarker(new MarkerOptions().position(new LatLng(48.848969, 2.334133)).title("Poubelle marron").snippet("Déchets alimentaires").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_marron))); //Angélina Luxembourg
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.858034, 2.348004)).title("Poubelle marron").snippet("Déchets alimentaires").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_marron))); //Chatelet
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.843858, 2.355042)).title("Poubelle marron").snippet("Déchets alimentaires").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_marron))); //Jussieu
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.891192, 2.377607)).title("Poubelle marron").snippet("Déchets alimentaires").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_marron))); //Crimée

        mMap.addMarker(new MarkerOptions().position(new LatLng(48.872315, 2.377851)).title("Poubelle jaune").snippet("Emballages de papier, carton, plastique, métal").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_jaune)));//Belleville
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.847757, 2.396054)).title("Poubelle jaune").snippet("Emballages de papier, carton, plastique, métal").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_jaune)));//Nation
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.853782, 2.289716)).title("Poubelle jaune").snippet("Emballages de papier, carton, plastique, métal").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_jaune)));//Bir-Hakeim
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.871915, 2.300638)).title("Poubelle jaune").snippet("Emballages de papier, carton, plastique, métal").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_jaune)));//George V
        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16F));*/

        //mMap.setOnInfoWindowClickListener(this); // use GoogleMap.OnInfoWindowClickListener,
        mMap.setOnMarkerClickListener(this);

        if (mLocationPermissionGranted){
            getDeviceLocation();
        }
    }

    private void getLocationPermission(){
        Log.d(TAG, "getting Location");
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            initMap();
            }
            else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    for (int i=0; i< grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: failed");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: granted");
                    initMap();
                }
            }
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: called");
        mLocation = LocationServices.getFusedLocationProviderClient(this);

        try{
            if (mLocationPermissionGranted){
                Task location = mLocation.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "getDeviceLocation: location found !");
                            mUserLocation = (Location) task.getResult();
                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            moveCamera(new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude()), DEFAULT_ZOOM);
                        }
                        else{
                            Log.d(TAG, "getDeviceLocation: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch(SecurityException e){
            Log.d(TAG, "getDeviceLocation : SecurityException ==> " +e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to :  lat="+latLng.latitude +", Lng="+ latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }


    //Cette fonction affiche les différents types de fond de carte : normal, hybrid, satellite, terrain (relief)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //change the map type based on the user's selection
        switch (item.getItemId()){
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*
    @Override
    public void onInfoWindowClick(Marker marker) {
        final Context context = this;
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.873599, 2.375490)).title("Hello").snippet("Déchets alimentaires").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_marron))); //Belleville
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(marker.getSnippet())
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, @SuppressWarnings("unused") int id) {
                        dialog.cancel();
                    }
                });
    }
*/

    @Override
    public boolean onMarkerClick(Marker marker) {
        calculateDirections(marker);
        //mMap.addMarker(new MarkerOptions().position(new LatLng(48.873599, 2.375490)).title("Hello").snippet("Déchets alimentaires").icon(BitmapDescriptorFactory.fromResource(R.drawable.poubelle_marron))); //Belleville
        return false;
    }


    //Code taken from goo.gl/nDiTgx
    private void calculateDirections(Marker marker){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mUserLocation.getLatitude(),
                        mUserLocation.getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                //Log.d(TAG, "Hello from calc");
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }


    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);
                if (mPolylineData.size() > 0 ){
                    for(PolylineData polylineData: mPolylineData){
                        polylineData.getPolyline().remove();
                    }
                    mPolylineData.clear();
                    mPolylineData = new ArrayList<>();
                    removePolylineMarkers();
                }

                double duration = 99999999;
                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){
//                        Log.d(TAG, "run: latlng: " + latLng.toString());
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(MapsActivity.this, R.color.darkGrey));
                    polyline.setClickable(true);
                    mPolylineData.add(new PolylineData(polyline, route.legs[0]));

                    double tempDurtion = route.legs[0].duration.inSeconds;
                    if (tempDurtion < duration){
                        duration = tempDurtion;
                        polyline.setColor(ContextCompat.getColor(MapsActivity.this, R.color.blue));
                        polyline.setZIndex(1);
                    }

                }
            }
        });
    }

    // Enlève le markeur qui donne la durée et la distance d'un chemin
    private void removePolylineMarkers(){
        for(Marker marker : polylineMarkers){
            marker.remove();
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        if (polylineMarkers.size() > 0) removePolylineMarkers(); // Enlève le markeur qui donne la durée et la distance d'un chemin

        for(PolylineData polylineData: mPolylineData){
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(MapsActivity.this, R.color.blue));
                polylineData.getPolyline().setZIndex(1);

                //Ajout du marker qui donne la distance et la durée du parcours au milieu du chemin
                List<LatLng> latitudeLongitude = polylineData.getPolyline().getPoints();
                int size = latitudeLongitude.size();
                int middleIndex = (int) size/2;
                double Lat = latitudeLongitude.get(middleIndex).latitude;
                double Lng = latitudeLongitude.get(middleIndex).longitude;
                DecimalFormat df = new DecimalFormat("0.000000"); //arrondi
                Lat = Double.valueOf(df.format(Lat)); //cast String en double
                Lng = Double.valueOf(df.format(Lng));
                Log.d(TAG, "run: Invisible marker Lat : " +Lat+", Lng : "+ Lng);
                Log.d(TAG, "run: Invisible marker size : " + size + ",  middle index: "+ middleIndex+ ", LatLng : "+latitudeLongitude.get(middleIndex));
                Marker infoWindow = mMap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lng))
                        .title("Estimation")
                        .snippet("Duration: "+polylineData.getLeg().duration + ", Distance: "+polylineData.getLeg().distance)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.invisible_marker)));
                infoWindow.showInfoWindow();
                polylineMarkers.add(infoWindow);

            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(MapsActivity.this, R.color.darkGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }




}
