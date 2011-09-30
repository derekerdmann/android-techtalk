/**
 * 
 */
package com.derekerdmann.androidtalk;

import java.util.Collection;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Displays a list of questions
 * @author Derek Erdmann
 */
public class QuestionsActivity extends ListActivity {

	private ArrayAdapter<Question> adapter;
	private SOHelper sohelper;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		
		adapter = new ArrayAdapter<Question>( this, R.layout.question );
		adapter.setNotifyOnChange( true );
		getListView().setAdapter( adapter );
		
		sohelper = new SOHelper();
	}
	
	
	@Override
	protected void onStart(){
		super.onStart();
		new QuestionsTask().execute();
	}
	
	
	/**
	 * Retrieves questions
	 * @author Derek Erdmann
	 */
	class QuestionsTask extends AsyncTask<Void, Question, Collection<Question>>{
		
		private ProgressDialog progress;
		
		@Override
    	protected void onPreExecute(){
    		progress = new ProgressDialog( QuestionsActivity.this );
    		progress.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
    		progress.setIndeterminate( true );
    		progress.setMessage( "Getting questions..." );
    		progress.show();
    	}
    	
		@Override
		protected Collection<Question> doInBackground(Void... arg0) {
			return sohelper.getTopQuestions();
		}
		
		@Override
		protected void onPostExecute( Collection<Question> result ){
			progress.setIndeterminate( false );
			progress.setMax( result.size() );
			
			for( Question q : result ){
				publishProgress( q );
			}
			
			progress.dismiss();
		}
		
		@Override
		protected void onProgressUpdate( Question... questions ){
			for( Question q : questions ){
				progress.incrementProgressBy( 1 );
				adapter.add( q );
				adapter.notifyDataSetChanged();
			}
		}
		
	}
	
	
}
