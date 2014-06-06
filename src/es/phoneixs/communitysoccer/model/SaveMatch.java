package es.phoneixs.communitysoccer.model;

import java.util.ArrayList;

import es.phoneixs.communitysoccer.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 
 * Get the match data configured by the user and save it in DB.
 * 
 * @since 20140524
 * 
 * @author Javier Alfonso <phoneixsegovia@gmail.com>
 * 
 */
public class SaveMatch extends AsyncTask<Void, Integer, Long> {

	private Context context;

	private ArrayList<Integer> homePlayersIds;
	private ArrayList<Integer> homeGoals;
	private ArrayList<Integer> homeRedCards;
	private ArrayList<Integer> homeYellowCards;
	private ArrayList<Integer> visitorPlayersIds;
	private ArrayList<Integer> visitorGoals;
	private ArrayList<Integer> visitorRedCards;
	private ArrayList<Integer> visitorYellowCards;

	private int community;

	private long date;

	public SaveMatch(Context context, int community, long date,
			ArrayList<Integer> homePlayersIds, ArrayList<Integer> homeGoals,
			ArrayList<Integer> homeRedCards,
			ArrayList<Integer> homeYellowCards,
			ArrayList<Integer> visitorPlayersIds,
			ArrayList<Integer> visitorGoals,
			ArrayList<Integer> visitorRedCards,
			ArrayList<Integer> visitorYellowCards) {
		
		this.context = context;

		this.homePlayersIds = homePlayersIds;
		this.homeGoals = homeGoals;
		this.homeRedCards = homeRedCards;
		this.homeYellowCards = homeYellowCards;
		this.visitorPlayersIds = visitorPlayersIds;
		this.visitorGoals = visitorGoals;
		this.visitorRedCards = visitorRedCards;
		this.visitorYellowCards = visitorYellowCards;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Long doInBackground(Void... params) {

		/*
		 * Get the DB
		 */

		PersistentDataHelper pdh = new PersistentDataHelper(this.context);

		SQLiteDatabase db = pdh.getWritableDatabase();

		/*
		 * Add the match row.
		 */

		ContentValues values = new ContentValues();

		values.put(PersistentDataHelper.MATCHS_COLUMN_COMMUNITY, this.community);
		values.put(PersistentDataHelper.MATCHS_COLUMN_DATE, this.date);

		long matchId = db.insert(PersistentDataHelper.MATCHS_TABLE_NAME, null,
				values);

		if (matchId == -1) {
			// Error!
			Log.e("es.phoneixs.communitysoccer.saving.savingmatch",
					"Error when trying to save the match into the database");
			return null;
		}

		/*
		 * For each player add its result row.
		 */

		for (int i = 0; i < this.homePlayersIds.size(); i++) {

			values.clear();

			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_MATCH, matchId);
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_HOME, 1); // >0
																			// ==
																			// visitor
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_PLAYER,
					this.homePlayersIds.get(i));
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_GOALS,
					this.homeGoals.get(i));
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_RED_CARDS,
					this.homeRedCards.get(i));
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_YELLOW_CARDS,
					this.homeYellowCards.get(i));

			db.insert(PersistentDataHelper.MATCHS_DATA_TABLE_NAME, null, values);

		}

		for (int i = 0; i < this.visitorPlayersIds.size(); i++) {

			values.clear();

			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_MATCH, matchId);
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_HOME, 0);// 0 ==
																		// visitor
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_PLAYER,
					this.visitorPlayersIds.get(i));
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_GOALS,
					this.visitorGoals.get(i));
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_RED_CARDS,
					this.visitorRedCards.get(i));
			values.put(PersistentDataHelper.MATCHS_DATA_COLUMN_YELLOW_CARDS,
					this.visitorYellowCards.get(i));

			db.insert(PersistentDataHelper.MATCHS_DATA_TABLE_NAME, null, values);

		}

		return matchId;
	}
	
	@Override
	protected void onPostExecute(Long result) {
		super.onPostExecute(result);
		
		Toast toast = Toast.makeText(context, R.string.match_saved, Toast.LENGTH_SHORT);
		
		toast.show();
		
	}

}
