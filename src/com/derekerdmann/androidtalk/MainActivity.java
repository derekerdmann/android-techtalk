package com.derekerdmann.androidtalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Main activity for the Stack Overflow browser.  Displays some basic 
 * information about Stack Overflow
 * 
 * @author Derek Erdmann
 */
public class MainActivity extends Activity {
	
	
	private static final String question_total_url = 
			"http://api.stackoverflow.com/1.1/questions?pagesize=0";
    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    }
    
    
    /**
     * Called when the activity starts
     */
    @Override
    public void onStart(){
    	super.onStart();
    	new QuestionTotalTask().execute();
    }
    
    
    /**
     * Starts the QuestionsActivity
     * @param v - the View that generated the event
     */
    public void onQuestionsButtonClick( View v ){
    	startActivity( new Intent( this, QuestionsActivity.class ) );
    }
    
    
    
    /**
     * Gets the total number of questions on Stack Overflow
     * @author Derek Erdmann
     */
    class QuestionTotalTask extends AsyncTask<Void, Void, Integer>{
    	
    	private static final String tag = "QuestionTotalTask";
    	
    	private ProgressDialog progress;
    	
    	@Override
    	protected void onPreExecute(){
    		progress = new ProgressDialog( MainActivity.this );
    		progress.setMessage( "Counting..." );
    		progress.show();
    	}
    	
		@Override
		protected Integer doInBackground(Void... arg0) {
			
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
		
		
		@Override
		protected void onPostExecute( Integer result ){
			
			TextView total = (TextView) findViewById( R.id.total );
			total.setText( result.toString() );
			
			progress.dismiss();
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
    
}