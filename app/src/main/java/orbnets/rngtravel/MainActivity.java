package orbnets.rngtravel;

import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

// libraries for spinners
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

// libraries to handle text display
import android.widget.TextView;
import android.widget.Toast;

// google maps API libraries
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

// Location services libraries
import com.google.android.gms.location.LocationServices;

// LatLng libraries
import com.google.android.gms.maps.model.LatLng;

// Marker Libraries
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

// drawing libraries



public class MainActivity extends FragmentActivity implements OnMapReadyCallback,AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    // initiate Objects for GoogleMaps application
    protected GoogleMap mGmap;
    private GoogleApiClient mGoogleServicesClient;

    private Double mCurrentLat;     // stores the current latitude
    private Double mCurrentLong;    // stores the current longitude

    protected int mapType = 1; // set default for the map = NORMAL

    // initiate object for spinner
    Spinner drop_down;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*

            Ensure a fragment is created for the spinner to attach to. Then have the OnItemSelectedListener wait for input

 */
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);              // display a map on the given Activity Fragment.

        initiateClient();           // setup client connector for Google services API
        generateSpinner();                      // place the spinner on the fragment view.

        drop_down.setOnItemSelectedListener(this);      // wait for the user to hit spinner drop down.

    }
    /*
        Method: InitiateClient()
        Parameters: none
        return: none
        Use: Creates a connector to the Google Services API. This is needed to retrieve location data (longitude and latitude).
     */
    private void initiateClient() {

        if (mGoogleServicesClient == null){

            mGoogleServicesClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            mGoogleServicesClient.connect();

        }
    }
    /*  API
        Method: onConnected(Bundle bundle)
        parameters: bundle
        return: none
        Use: when initiateClient successfully connects, then access the location data
     */
    public void onConnected(@Nullable Bundle bundle) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleServicesClient);

        if(mLastLocation != null){

            this.mCurrentLat = mLastLocation.getLatitude();
            this.mCurrentLong = mLastLocation.getLongitude();

        }

    }
    /*  API
        Method: onConnectionSuspended
        parameters: int i
        return: none
        Use: API requires this method to be included. If the connection is suspended...do nothing.
     */
    public void onConnectionSuspended(int i) {

    }
    /*
        Method: onConnectedFailed(ConnectedResult connectionResult)
        parameters: connectionResult
        return: none
        Use: when initiateClient fails to connect, then display the failed result.

     */
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(MainActivity.this, "Connection Result: " + connectionResult, Toast.LENGTH_SHORT).show();

    }

    // OnMapReady comes with the google API. When a selection is made via spinner, then update OnMapReady
    /* API
        Method: OnMapReady(GoogleMap googleMap)
        parameters: googleMap
        return: none
        Use: when the map fragment is called (mapAsync()), then onMapReady will instantiate a non-null instance of
            the GoogleMaps object. All map information (adding, changing map types etc.) are managed here.
     */
    public void onMapReady(GoogleMap googleMap) {

        generateButton(googleMap);

        googleMap.setMapType(mapType);
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
        googleMap.setMyLocationEnabled(true);

        // Listen for long presses on the map activity.
        addMarkerLongClickListener(googleMap);
    }

    public void addMarkerLongClickListener(final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                map.addMarker(new MarkerOptions().position(latLng).draggable(true));
            }
        });

    }

    /*
        Method: generateSpinner()
        parameters: none
        return: none
        Use: create the Spinner object that will be used to switch between map types.
     */
    private void generateSpinner() {

        drop_down = (Spinner) findViewById(R.id.maptype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.maptype_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_down.setAdapter(adapter);
    }

     /*
            function name: generateButton
            parameters: GoogleMap object
            return: none
            use: create the button object, then wait for the users input. If the button is pressed, then pass
                the google map object to MarkerRequest
     */
    private void generateButton(final GoogleMap googleMap) {

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                markerRequest(googleMap);
            }
        });

    }
    /*
            function name: markerRequest
            parameters: GoogleMap object
            return: none
            use: when the user clicks on the "add marker" button, retrieve the current coordinates of the device.
                 Once retrieved, place a marker at that location with draggable = true.
                 Parameter must be a none-null instance of Google Map
     */
    private void markerRequest(GoogleMap map) {

        initiateClient();
        map.addMarker(new MarkerOptions().position(new LatLng(mCurrentLat,mCurrentLong)).draggable(true));
        Toast.makeText(MainActivity.this, "Added Marker at current location", Toast.LENGTH_SHORT).show();

    }
    private void addLine(GoogleMap map, LatLng Latitude, LatLng Longitude){
        // example provided by https://developers.google.com/maps/documentation/android-api/shapes
        Polyline newLine = map.addPolyline(new PolylineOptions().add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                .width(5).color(Color.RED));

    }

    /*
        MethodName: CreateMap
        Parameters: Map, mapType
        Use: A pass function for "OnMapReady". Createmap will receive the mapType and current map, then
            pass those values to "OnMapReady".
    */
    private void createMap(GoogleMap map, int mapType){

        try {
            if (map == null) {      // by default, the GoogleMap object will be null, meaning the map hasn't been created yet.

                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                onMapReady(map);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*  API
        Method: OnItemSelected
        parameters: parent, view, position, id
        return: none
        Use: when an item in the spinner is selected, then create a map that corresponds to the map type.
     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

 /*
         check the text which was selected from the drop down.
         The spinner drop down should appear as the following...

         Normal
         Satellite
         Terrain
         Hybrid

         then if nothing else, choose "none"

*/
        TextView item = (TextView) view;

        if(item.getText().equals("Normal")){
            Toast.makeText(this, "Changing map type to "+item.getText(),Toast.LENGTH_SHORT).show();
            mapType = GoogleMap.MAP_TYPE_NORMAL;
            createMap(mGmap, mapType);

        }

        if(item.getText().equals("Satellite")){
            Toast.makeText(this, "Changing map type to "+item.getText(),Toast.LENGTH_SHORT).show();
            mapType = GoogleMap.MAP_TYPE_SATELLITE;
            createMap(mGmap, mapType);

        }

        if(item.getText().equals("Terrain")){
            Toast.makeText(this, "Changing map type to "+item.getText(),Toast.LENGTH_SHORT).show();
            mapType = GoogleMap.MAP_TYPE_TERRAIN;
            createMap(mGmap, mapType);

        }
        if(item.getText().equals("Hybrid")){
            Toast.makeText(this, "Changing map type to "+item.getText(),Toast.LENGTH_SHORT).show();
            mapType = GoogleMap.MAP_TYPE_HYBRID;
            createMap(mGmap, mapType);


        }
        if(item.getText().equals("None")){
            Toast.makeText(this, "Changing map type to "+item.getText(),Toast.LENGTH_SHORT).show();
            mapType = GoogleMap.MAP_TYPE_NONE;
            createMap(mGmap, mapType);

        }

    }


        public void onNothingSelected(AdapterView<?> parent) {
        // if nothing selected then do nothing
    }

}
