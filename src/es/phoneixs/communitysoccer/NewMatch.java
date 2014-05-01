package es.phoneixs.communitysoccer;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;

import es.phoneixs.communitysoccer.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewMatch extends ActionBarActivity {

	private static final int PICK_USERS_REQUEST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_match);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_match, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_new_match,
					container, false);
			
			String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
			
			TextView tvDate = (TextView) rootView.findViewById(R.id.tvDate);
			
			tvDate.setText(currentDateTimeString);
			
			rootView.findViewById(R.id.btnAddHomePlayer).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(getActivity(), SelectPlayers.class);
					
					int selectedPlayers[] = {}; // TODO Get players that are already added to the team.
					
					intent.putExtra(SelectPlayers.ALREADY_SELECTED, selectedPlayers);
					
					int disabledPlayers[] = {}; // TODO Get players that are already added in other team.
					
					intent.putExtra(SelectPlayers.DISABLED_PLAYERS, disabledPlayers);
					
					startActivityForResult(intent, PICK_USERS_REQUEST);
					
					
				}
			});
			
			return rootView;
		}
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
	    if (resultCode == Activity.RESULT_OK && requestCode == PICK_USERS_REQUEST) {
	    	
	        // Perform a query to the contact's content provider for the contact's name
	    	
	    	// XXX TE HAS QUEDADO AQUÍ
	    	/*
	        Cursor cursor = getContentResolver().query(data.getData(),
	        new String[] {Contacts.DISPLAY_NAME}, null, null, null);
	        if (cursor.moveToFirst()) { // True if the cursor is not empty
	            int columnIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
	            String name = cursor.getString(columnIndex);
	            // Do something with the selected contact's name...
	        }
	        */
	        
	    }
	    
	}

}
