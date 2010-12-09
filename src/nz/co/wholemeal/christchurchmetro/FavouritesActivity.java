package nz.co.wholemeal.christchurchmetro;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
import org.json.JSONArray;

import android.app.Activity;
import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FavouritesActivity extends ListActivity {

  public final String TAG = "FavouritesActivity";

  private ArrayList stops = new ArrayList<Stop>();
  /*
  private static final String[] FAVOURITES = new String[] {
    "40188", "20763", "21450", "37375", "37334", "14864", "21957"
  };
  */
  private static String STOPS_JSON = "[" +
    "{" +
      "\"attributes\" : {" +
        "\"OBJECTID\" : 773," +
        "\"Name\" : \"Bower Ave & Pinewood Ave\"," +
        "\"PlatformTa\" : 821," +
        "\"RoadName\" : \"Bower Ave\"," +
        "\"PlatformNo\" : 40188," +
        "\"Routes\" : \"70:814|70:816\"," +
        "\"Lat\" : -43.488801000000002," +
        "\"Long\" : 172.71179900000001," +
        "\"BearingToR\" : null," +
        "\"RouteNos\" : \"70\"," +
        "\"RouteTags\" : \"814|816\"" +
      "}," +
      "\"geometry\" : {" +
        "\"x\" : 19226189.518700004," +
        "\"y\" : -5386670.7789999992" +
      "}" +
    "}," +
    "{" +
    "  \"attributes\" : {" +
    "    \"OBJECTID\" : 766," +
    "    \"Name\" : \"Bower Ave & Castletown Pl\"," +
    "    \"PlatformTa\" : 814," +
    "    \"RoadName\" : \"Bower Ave\"," +
    "    \"PlatformNo\" : 20763," +
    "    \"Routes\" : \"49:804|70:814|70:816\"," +
    "    \"Lat\" : -43.497529," +
    "    \"Long\" : 172.71062900000001," +
    "    \"BearingToR\" : null," +
    "    \"RouteNos\" : \"49|70\"," +
    "    \"RouteTags\" : \"804|814|816\"" +
    "  }," +
    "  \"geometry\" : {" +
    "    \"x\" : 19226059.2971," +
    "    \"y\" : -5388010.0443000011" +
    "  }" +
    "}" +
  "]";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initFavourites();

    setListAdapter(new StopAdapter(this, R.layout.list_item, stops));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {
        Intent intent = new Intent();
        Stop stop = (Stop)stops.get(position);

        if (stop == null) {
          Log.e(TAG, "Didn't get a stop");
          finish();
        }
        intent.putExtra("platformNumber", stop.getPlatformNumber());
        /*
        Toast.makeText(getApplicationContext(), ((Stop)stops.get(position)).getPlatformNumber(),
            Toast.LENGTH_SHORT).show();
         */
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }

  private void initFavourites() {
    try {
      JSONArray stops_array = (JSONArray) new JSONTokener(STOPS_JSON).nextValue();
      for (int i = 0;i < stops_array.length();i++) {
        JSONObject stop_json = (JSONObject)stops_array.get(i);
        Stop stop = new Stop(stop_json);
        stops.add(stop);
        Log.d(TAG, "initFavourites(): added stop " + stop.getPlatformNumber());
      }
    } catch (JSONException e) {
      Log.e(TAG, "initFavourites(): " + e.toString());
    }
  }

  private class StopAdapter extends ArrayAdapter<Stop> {

    private ArrayList<Stop> items;

    public StopAdapter(Context context, int textViewResourceId, ArrayList<Stop> items) {
      super(context, textViewResourceId, items);
      this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      if (v == null) {
        LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.list_item, null);
      }
      Stop stop = items.get(position);
      if (stop != null) {
        TextView list_item = (TextView) v;
        if (list_item != null) {
          list_item.setText(stop.getName());
        }
      }
      return v;
    }
  }
}