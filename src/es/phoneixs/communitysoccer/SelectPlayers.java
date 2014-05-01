package es.phoneixs.communitysoccer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import es.phoneixs.communitysoccer.model.PersistentDataHelper;

/**
 * This activity let you select some players (users) an return the result to the
 * calling activity.
 * 
 * @author Javier Alfonso <phoneixsegovia@gmail.com>
 * @version 20140501
 * 
 */
public class SelectPlayers extends ActionBarActivity {

	/**
	 * Indicate that the parameter is about the already selected players ids.
	 * 
	 * @since 20140501
	 */
	protected static final String SELECTED_IDS = "es.phoneixs.communitysoccer.ALREADY_SELECTED";
	
	/**
	 * Indicate that the parameter is about the already selected players names.
	 * 
	 * @since 20140501
	 */
	protected static final String SELECTED_NAMES = "es.phoneixs.communitysoccer.SELECTED_NAMES";

	/**
	 * Indicate that the parameter is about players that must be show as
	 * disabled.
	 * 
	 * @since 20140501
	 */
	protected static final String DISABLED_PLAYERS = "es.phoneixs.communitysoccer.DISABLED_PLAYERS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_players);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_players, menu);
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

		private Activity activity;

		private Map<CheckBox, Integer> checkBox2UserId;

		public PlaceholderFragment() {

			this.checkBox2UserId = new HashMap<CheckBox, Integer>();

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			this.activity = getActivity();
			Intent originalIntent = activity.getIntent();

			View rootView = inflater.inflate(R.layout.fragment_select_players,
					container, false);

			View btnAdd = rootView.findViewById(R.id.btnAdd);
			btnAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addSelectedClicked();
				}

			});
			
			View btnCancel = rootView.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					cancelCliked();
				}
			});

			LinearLayout llVerticalList = (LinearLayout) rootView
					.findViewById(R.id.llVerticalList);

			// Get what must be already selected
			ArrayList<Integer> selectedPlayers = originalIntent.getIntegerArrayListExtra(SELECTED_IDS);
			Collections.sort(selectedPlayers);

			// And what must be disabled.
			ArrayList<Integer> disabledPlayers = originalIntent.getIntegerArrayListExtra(DISABLED_PLAYERS);
			Collections.sort(disabledPlayers);

			PersistentDataHelper pdh = new PersistentDataHelper(activity);

			SQLiteDatabase db = pdh.getReadableDatabase();

			String columns[] = { PersistentDataHelper.USERS_COLUMN_ID,
					PersistentDataHelper.USERS_COLUMN_NAME };

			Cursor cursor = db.query(PersistentDataHelper.USERS_TABLE_NAME,
					columns, null, null, null, null, "2");

			if (cursor != null) {

				while (cursor.moveToNext()) {

					int id = cursor.getInt(0);

					String name = cursor.getString(1);

					CheckBox cb = new CheckBox(activity);

					cb.setText(name);

					if (Collections.binarySearch(selectedPlayers, id) >= 0) {

						cb.setChecked(true);

					}

					if (Collections.binarySearch(disabledPlayers, id) >= 0) {

						cb.setEnabled(false);

					}

					this.checkBox2UserId.put(cb, id);

					llVerticalList.addView(cb);

				}

			}

			return rootView;
		}

		/**
		 * Find what players have been selected (and are enabled) and finish the
		 * activity returning they.
		 * 
		 * @since 20140501
		 */
		public void addSelectedClicked() {

			// Check what users have been selected.

			ArrayList<Integer> selectedUsers = new ArrayList<Integer>();
			ArrayList<String> selectedNames = new ArrayList<String>();

			for (Entry<CheckBox, Integer> userInfo : this.checkBox2UserId
					.entrySet()) {

				if (userInfo.getKey().isChecked()
						&& userInfo.getKey().isEnabled()) {

					selectedUsers.add(userInfo.getValue());
					selectedNames.add(userInfo.getKey().getText().toString());

				}

			}

			// Create the intent to return the result.

			Intent resultIntent = new Intent();

			resultIntent.putIntegerArrayListExtra(SelectPlayers.SELECTED_IDS, selectedUsers);
			
			resultIntent.putStringArrayListExtra(SelectPlayers.SELECTED_NAMES, selectedNames);

			activity.setResult(RESULT_OK, resultIntent);

			activity.finish();

		}

		/**
		 * Set the result of the activity to {@link Activity#RESULT_CANCELED}
		 * and finish the current activity.
		 * 
		 * @since 20140501
		 */
		public void cancelCliked() {

			activity.setResult(RESULT_CANCELED);

			activity.finish();

		}
	}

}
