package codepath.apps.searchimage;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8779945460716777143L;
	private String fullURL;
	private String thumbURL;

	public ImageResult(JSONObject json){
		try {
			this.fullURL = json.getString("url");
			this.thumbURL = json.getString("tbUrl");
		} catch (JSONException e)
		{
			this.fullURL = null;
			this.thumbURL = null;
		}
	}
	
	
	public String getFullURL() {
		return fullURL;
	}
	
	public String getThumbURL() {
		return thumbURL;
	}
	
	public String toString() {
		return this.thumbURL;
	}


	public static ArrayList<ImageResult> fromJSONArray(
			JSONArray array) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for(int i = 0; i<array.length(); i++)
		{
			try {
				results.add(new ImageResult(array.getJSONObject(i)));

			}
			catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		return results;
		
	}
	
}
