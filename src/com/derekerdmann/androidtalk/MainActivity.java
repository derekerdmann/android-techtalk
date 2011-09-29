package com.derekerdmann.androidtalk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Main activity for the Stack Overflow browser.  Displays some basic 
 * information about Stack Overflow
 * 
 * @author Derek Erdmann
 */
public class MainActivity extends Activity {
    
	
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

    	private ProgressDialog progress;
    	
    	@Override
    	protected void onPreExecute(){
    		progress = new ProgressDialog( MainActivity.this );
    		progress.setMessage( "Counting..." );
    		progress.show();
    	}
    	
		@Override
		protected Integer doInBackground(Void... arg0) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 5;
		}
		
		
		@Override
		protected void onPostExecute( Integer result ){
			
			TextView total = (TextView) findViewById( R.id.total );
			total.setText( result.toString() );
			
			progress.dismiss();
		}
    	
    }
    
    
}