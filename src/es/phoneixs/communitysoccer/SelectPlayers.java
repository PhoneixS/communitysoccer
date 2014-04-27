package es.phoneixs.communitysoccer;

import es.phoneixs.communitysoccer.model.PersistentDataHelper;
import es.phoneixs.communitysoccer.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class SelectPlayers extends ActionBarActivity {

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
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_select_players,
					container, false);
			
			LinearLayout llVerticalList = (LinearLayout) rootView.findViewById(R.id.llVerticalList);
			
			PersistentDataHelper pdh = new PersistentDataHelper(getActivity());
			
			SQLiteDatabase db = pdh.getReadableDatabase();
			
			String columns[] = {PersistentDataHelper.USERS_COLUMN_ID, PersistentDataHelper.USERS_COLUMN_NAME};
			
			Cursor cursor = db.query(PersistentDataHelper.USERS_TABLE_NAME, columns, null, null, null, null, "2");
			
			if (cursor != null) {
				
				while (cursor.moveToNext()) {
					
					int id = cursor.getInt(0);
				
					String name = cursor.getString(1);
					
					CheckBox cb = new CheckBox(getActivity());
					
					cb.setText(name);
					
					llVerticalList.addView(cb);
				
				}
				
			}
			
			return rootView;
		}
	}

}
