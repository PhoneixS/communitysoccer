package es.phoneixs.communitysoccer;

import es.phoneixs.communitysoccer.model.PersistentDataHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import static es.phoneixs.communitysoccer.model.PersistentDataHelper.*;

public class RankingByScoreActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_by_score);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ranking_by_score, menu);
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
	public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

		private static final int URL_BY_SCORE_LOADER = 0;
		
		// This is the Adapter being used to display the list's data
	    SimpleCursorAdapter mAdapter;


		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_ranking_by_score, container, false);
			
			// For the cursor adapter, specify which columns go into which views
	        String[] fromColumns = {PersistentDataHelper.USERS_COLUMN_NAME, "score"};
	        int[] toViews = {android.R.id.text1, android.R.id.text2}; // The TextView in simple_list_item_1

	        // Create an empty adapter we will use to display the loaded data.
	        // We pass null for the cursor, then update it in onLoadFinished()
			mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2,
					null, fromColumns, toViews, 0);
			
			ListView lstScores = (ListView)rootView.findViewById(R.id.lstScores);
			
			lstScores.setAdapter(mAdapter);

			getLoaderManager().initLoader(URL_BY_SCORE_LOADER, null, this);

			return rootView;
		}

		@Override
		public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
			
			// Create the loader that will load the cursor to read the data.
			
			switch (loaderId) {
			case URL_BY_SCORE_LOADER:
				
				CursorLoader cl = new CursorLoader(getActivity()){
					
					@Override
					public Cursor loadInBackground() {
						
						PersistentDataHelper pdh = new PersistentDataHelper(this.getContext());

						SQLiteDatabase db = pdh.getReadableDatabase();

						String sql = "SELECT u." + USERS_COLUMN_ID + " AS _id" +
								", u." + USERS_COLUMN_NAME +
								", sum(CASE WHEN mr." + MATCHS_RESULT_COLUMN_WINNER + " = " + MATCHS_RESULT_WINNER_HOME + " AND md." + MATCHS_DATA_COLUMN_HOME + " > 0 THEN 3 WHEN " + MATCHS_RESULT_COLUMN_WINNER + " = " + MATCHS_RESULT_WINNER_VISITOR + " AND md." + MATCHS_DATA_COLUMN_HOME + " = 0 THEN 3 WHEN " + MATCHS_RESULT_COLUMN_WINNER + " = " + MATCHS_RESULT_WINNER_TIE + " THEN 1 ELSE 0 END) as score" +
							" FROM " + USERS_TABLE_NAME + " as u" +
								" LEFT JOIN " + MATCHS_DATA_TABLE_NAME + " as md ON u." + USERS_COLUMN_ID + " = md." + MATCHS_DATA_COLUMN_PLAYER + "" +
								" LEFT JOIN " + MATCHS_RESULT_TABLE_NAME + " as mr ON mr." + MATCHS_RESULT_COLUMN_MATCH + " = md." + MATCHS_DATA_COLUMN_MATCH +
							" GROUP BY u." + USERS_COLUMN_ID +
							" ORDER BY score DESC";
						
						Cursor cursor = db.rawQuery(sql, null);
						
						return cursor;
						
					}
					
				};
								
				return cl;

			default:
				// An invalid id was passed in
				return null;
			}
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			
			mAdapter.swapCursor(cursor);
			
		}

		// Called when a previously created loader is reset, making the data unavailable
		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			
			// This is called when the last Cursor provided to onLoadFinished()
	        // above is about to be closed.  We need to make sure we are no
	        // longer using it.
			mAdapter.swapCursor(null);
			
		}


	}

}
