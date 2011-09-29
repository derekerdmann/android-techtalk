package com.derekerdmann.androidtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
     * Starts the QuestionsActivity
     * @param v - the View that generated the event
     */
    public void onQuestionsButtonClick( View v ){
    	startActivity( new Intent( this, QuestionsActivity.class ) );
    }
    
}