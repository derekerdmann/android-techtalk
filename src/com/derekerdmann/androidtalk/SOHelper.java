package com.derekerdmann.androidtalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Retrieves HttpRequests from StackOverflow API
 * @author Derek Erdmann
 */
public class SOHelper {

	private static final String tag = "SOHelper";
	
	private static final String question_total_url = 
			"http://api.stackoverflow.com/1.1/questions?pagesize=0";
    
	/**
	 * Gets the total number of questions on StackOverflow
	 * @return Returns the number of questions, 0 if there is an error
	 */
	public int getTotalQuestions(){
		
		int result = 0;
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet( question_total_url );
			
			HttpResponse response = client.execute( get );
			HttpEntity entity = response.getEntity();
			
			if( entity != null ){
				
				String json = unzipResponse( entity.getContent() );
				Log.v( tag, json );
				result = parseTotal( json );
			}

		} catch (ClientProtocolException e) {
			Log.e(tag, e.getMessage() );
			Log.e( tag, Log.getStackTraceString( e ) );
		} catch (IOException e) {
			Log.e(tag, e.getMessage() );
			Log.e( tag, Log.getStackTraceString( e ) );
		}
		
		return result;
	}
	
	
	/**
	 * Returns the top questions currently on StackOverflow
	 * @return Returns a collection of Question objects
	 */
	public Collection<Question> getTopQuestions(){
		return null;
	}
	

	/**
	 * Unzips the response input stream
	 * @param in - the input stream to unzip
	 * @return Returns the content of the response1
	 */
	protected String unzipResponse( InputStream in ){
		
		StringBuilder builder = new StringBuilder();
		String line = null;
		
		try {
			
			GZIPInputStream gzip = new GZIPInputStream( in );
			
			BufferedReader reader = 
					new BufferedReader( new InputStreamReader( gzip ) );
				
			while( ( line = reader.readLine() ) != null ){
				builder.append( line );
			}
			
			reader.close();
			
		}catch( IOException e ){
			Log.e(tag,  e.getMessage() );
			Log.e( tag, Log.getStackTraceString( e ) );
		}
		
		return builder.toString();
		
	}
	
	
	/**
	 * Parses the question total from the JSON string
	 * @param json - Returns the total number of questions in the request
	 */
	protected int parseTotal( String json ){
		try {
			JSONObject obj = new JSONObject( json );
			String totalString = obj.getString( "total" );
			
			return Integer.parseInt( totalString );
			
		} catch (JSONException e) {
			Log.e(tag, e.getMessage() );
			return 0;
		}
	}
}
