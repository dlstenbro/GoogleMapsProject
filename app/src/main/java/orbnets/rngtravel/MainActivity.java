package orbnets.rngtravel;


import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

// libraries for spinners
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import android.widget.TextView;
import android.widget.Toast;

// google maps API libraries
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback,AdapterView.OnItemSelectedListener {

    protected GoogleMap mGmap;
    protected int mapType = 1; // set default for the map = NORMAL
    Spinner drop_down;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*

            Ensure a fragment is created for the spinner to attach to. Then have the OnItemSelectedListener wait for input

 */
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);              // display a map on the given Activity Fragment.

        generateSpinner();                      // place the spinner on the fragment view.
        drop_down.setOnItemSelectedListener(this);      // wait for the user to hit spinner drop down.

    }

    // OnMapReady comes with the google API. When a selection is made via spinner, then update OnMapReady
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(mapType);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
        googleMap.setMyLocationEnabled(true);

    }


    // create the drop down menu for users to choose the type of map they want to switch between

    private void generateSpinner() {

        drop_down = (Spinner) findViewById(R.id.maptype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.maptype_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_down.setAdapter(adapter);
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
