package codepath.apps.searchimage;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchImage extends Activity {

	EditText etSearch;
	GridView gvImages;
	Button btnSearch;
	
    String filters = "";
    String filter_type = ""; 
    String filter_site = "";
    String filter_size = "";
    String filter_color = "";
        
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultsArrayAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_image);
		setupViews();
		
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		gvImages.setAdapter(imageAdapter);
				
		gvImages.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                
	    	String query = etSearch.getText().toString();
	    	
	    	//first check that there is an active search
	    	if( query!= null && !query.isEmpty())
	    	{
				//start filling in images after the current set of images
				String request_url = "https://ajax.googleapis.com/ajax/services/search/images?"+ "rsz=8"  + 
				           filters + "&start=" + totalItemsCount + "&v=1.0" + "&q=";
				
				AsyncHttpClient client = new AsyncHttpClient();

				client.get(request_url + Uri.encode(query), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJSONResults = null;
						try {
							imageJSONResults = response.getJSONObject(
									"responseData").getJSONArray("results");
							
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJSONResults));
						}
						catch (JSONException e) {
							e.printStackTrace();
						}
						
						
					}
				});
			
	    	}    
	    }
        });

		//setting a click listener for every image to open a big version of the image
		gvImages.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("image_result", imageResult);
				startActivity(i);	
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_image, menu);
		return true;
	}
	
	//the onclick function for the settings menu button
	public void changeSettings(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), SearchSettingsActivity.class);	
		//i.putExtra("site", filter_site);
		startActivityForResult(i, SearchSettingsActivity.REQUEST_CODE); 	
	}
	
	//the function called when the user returns from the settings activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == SearchSettingsActivity.REQUEST_CODE) {
	     Toast.makeText(this, "filtering results",
	        Toast.LENGTH_SHORT).show();
	     
	     //reset old filters string
	     filters ="";
	     
	     //get all the data
	     filter_type = data.getExtras().getString("type");
	     filter_site = data.getExtras().getString("site");
	     filter_size = data.getExtras().getString("size");
	     filter_color = data.getExtras().getString("color");
	     
	     //add all new filters	 
	     if( filter_type!= null  && !filter_type.isEmpty() ){
	    	 filters = filters + "&imgtype=" + filter_type;
	     }
	     if(filter_site != null && !filter_site.isEmpty() ){
	    	 filters = filters + "&as_sitesearch=" + filter_site;
	     }
	     if(filter_size != null && !filter_size.isEmpty()){
	    	 filters = filters + "&imgsz=" + filter_size;
	     }
	     if(filter_color != null && !filter_color.isEmpty() ){
	    	 filters = filters + "&imgcolor=" + filter_color;
	     }
	  }
	} 
	
	
	
	public void setupViews() {
		etSearch = (EditText) findViewById(R.id.etSearch);
		gvImages = (GridView) findViewById(R.id.gvImages);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}
	
	public void onImageSearch(View v) {
		String query = etSearch.getText().toString();
		Toast.makeText(this, "searching for " + query, Toast.LENGTH_LONG).show();
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		String request_url = "https://ajax.googleapis.com/ajax/services/search/images?"+ "rsz=8" + 
		           filters + "&start=" + 0 + "&v=1.0" + "&q=";
		
		//https://ajax.googleapis.com/ajax/services/search/images?rsz=9&start=0&v=1.0&q=squid
		
		Log.d("DEBUG", request_url);
		Log.d("DEBUG", "full url: " + request_url + Uri.encode(query));

		
		client.get(request_url + Uri.encode(query), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJSONResults = null;
				try {
					imageJSONResults = response.getJSONObject(
							"responseData").getJSONArray("results");
					
					Log.d("DEBUG", "Got results: " + imageJSONResults.toString());


					imageResults.clear();
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJSONResults));
					Log.d("DEBUG", imageResults.toString());
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
		
	}

}
