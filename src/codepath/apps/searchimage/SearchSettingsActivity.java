package codepath.apps.searchimage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchSettingsActivity extends Activity {

	Spinner spImageSize;
	Spinner spImageType;
	Spinner spColor;
	
	EditText etSiteFilter;
	
	ArrayAdapter<CharSequence> size_adapter;
	ArrayAdapter<CharSequence> type_adapter;
	ArrayAdapter<CharSequence> color_adapter;
	
	static int REQUEST_CODE = 1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_settings);
		setupSpinners();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_settings, menu);
		return true;
	}
	
	public void setupSpinners()
	{
		//spinner and adapter for image size
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		size_adapter = ArrayAdapter.createFromResource(this,
		        R.array.size_array, android.R.layout.simple_spinner_item);
		size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spImageSize.setAdapter(size_adapter);
		
		//spinner and adapter for image type (jpg, png, gif)
		spImageType = (Spinner) findViewById(R.id.spImageType);
		type_adapter = ArrayAdapter.createFromResource(this,
		        R.array.type_array, android.R.layout.simple_spinner_item);
		type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spImageType.setAdapter(type_adapter);

		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);

		//spinner and adapter for color filter 
		spColor = (Spinner) findViewById(R.id.spColorFilter);
		color_adapter = ArrayAdapter.createFromResource(this,
		        R.array.color_array, android.R.layout.simple_spinner_item);
		color_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spColor.setAdapter(color_adapter);
		
		
	}
	
	//@Override
	public void onSubmit(View v) {
	  // Prepare data intent 
	  Intent data = new Intent();
	  
	  //Put in all the data
	  data.putExtra("site", etSiteFilter.getText().toString());
	  data.putExtra("size", spImageSize.getSelectedItem().toString());
	  data.putExtra("type", spImageType.getSelectedItem().toString());
	  data.putExtra("color", spColor.getSelectedItem().toString());

	  // Activity finished ok, return the data
	  setResult(RESULT_OK, data); // set result code and bundle data for response
	  finish(); // closes the activity, pass data to parent
	} 

}
