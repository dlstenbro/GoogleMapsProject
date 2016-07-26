package orbnets.rngtravel;


import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

// google maps API libraries
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback,AdapterView.OnItemSelectedListener {

    protected GoogleMap mGmap;
    protected int mapType;
    Spinner drop_down;

    public MainActivity(int mapType) {
        this.mapType = mapType;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (mGmap == null) {
                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                onMapReady(mGmap);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(mapType);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
        googleMap.setMyLocationEnabled(true);

        generateSpinner(googleMap);
        drop_down.setOnItemSelectedListener(this);
    }


    // create the drop down menu for users to choose the type of map they want to switch between

    private void generateSpinner(GoogleMap googleMap) {

        drop_down = (Spinner) findViewById(R.id.maptype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.maptype_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_down.setAdapter(adapter);
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

        if(parent.getItemAtPosition(position) == "Normal"){

            this.mapType = GoogleMap.MAP_TYPE_NORMAL;

        }
        if(parent.getItemAtPosition(position) == "Satellite"){

            this.mapType = GoogleMap.MAP_TYPE_SATELLITE;

        }
        if(parent.getItemAtPosition(position) == "Terrain"){

            this.mapType = GoogleMap.MAP_TYPE_TERRAIN;

        }
        if(parent.getItemAtPosition(position) == "Hybrid"){

            this.mapType = GoogleMap.MAP_TYPE_HYBRID;

        }
        if(parent.getItemAtPosition(position) == "None"){
            this.mapType = GoogleMap.MAP_TYPE_NONE;
        }
    }


        public void onNothingSelected(AdapterView<?> parent) {
        // if nothing selected then do nothing
    }

}
