package com.derekerdmann.androidtalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
	
	private static final String top_question_url = 
			"http://api.stackoverflow.com/1.1/questions";
	
	
	private final HttpClient client = new DefaultHttpClient();
	
    
	/**
	 * Gets the total number of questions on StackOverflow
	 * @return Returns the number of questions, 0 if there is an error
	 */
	public int getTotalQuestions(){
		
		try {
			JSONObject obj = new JSONObject( getJson( question_total_url ) );
			String totalString = obj.getString( "total" );
			
			return Integer.parseInt( totalString );
			
		} catch (JSONException e) {
			Log.e(tag, e.getMessage() );
			Log.e(tag, Log.getStackTraceString( e ) );
			return 0;
		}
		
	}
	
	
	/**
	 * Returns the top questions currently on StackOverflow
	 * @return Returns a collection of Question objects
	 */
	public Collection<Question> getTopQuestions(){
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
		try {
			JSONObject response = new JSONObject( getJson( top_question_url ) );
			JSONArray questionArray = response.getJSONArray( "questions" );
			
			for( int i = 0, len = questionArray.length(); i < len; i++ ){
				JSONObject qObj = questionArray.getJSONObject( i );
				
				Question q = new Question();
				q.setTitle( qObj.getString( "title" ) );
				q.setCreated( new Date( qObj.getLong( "creation_date" ) ) );
				q.setUpvotes( qObj.getInt( "up_vote_count" ) );
				q.setDownvotes( qObj.getInt( "down_vote_count" ) );

				questions.add( q );
			}
			
		} catch (JSONException e) {
			Log.e(tag, e.getMessage() );
			Log.e(tag, Log.getStackTraceString( e ) );
			return null;
		}	

		return null;
	}
	
	
	/**
	 * Gets the JSON from a GET request to the specified URL
	 * @param url - The location of the request
	 * @return Returns the HTTP response
	 */
	protected String getJson( String url ){
		try {
			HttpGet get = new HttpGet( top_question_url );
			
			HttpResponse response = client.execute( get );
			HttpEntity entity = response.getEntity();
			
			if( entity != null ){
				return unzipResponse( entity.getContent() );
			}

		} catch (ClientProtocolException e) {
			Log.e(tag, e.getMessage() );
			Log.e( tag, Log.getStackTraceString( e ) );
		} catch (IOException e) {
			Log.e(tag, e.getMessage() );
			Log.e( tag, Log.getStackTraceString( e ) );
		}
		
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
}
