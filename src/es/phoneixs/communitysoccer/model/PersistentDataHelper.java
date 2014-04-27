/**
 * 
 */
package es.phoneixs.communitysoccer.model;

import es.phoneixs.communitysoccer.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Javier Alfonso <phoneixsegovia@gmail.com>
 * 
 */
public class PersistentDataHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "MOC_DATA";

	/*
	 * Communities table
	 */

	public static final String COMMUNITIES_TABLE_NAME = "communities";

	public static final String COMMUNITIES_COLUMN_ID = "id";

	public static final String COMMUNITIES_COLUMN_NAME = "name";

	private static final String CREATE_COMMUNITIES_TABLE = "CREATE TABLE "
			+ COMMUNITIES_TABLE_NAME + " (" + COMMUNITIES_COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMMUNITIES_COLUMN_NAME
			+ " TEXT);";

	/*
	 * Users table
	 */

	public static final String USERS_TABLE_NAME = "users";

	public static final String USERS_COLUMN_ID = "id";

	public static final String USERS_COLUMN_NAME = "name";

	private static final String CREATE_USERS_TABLE = "CREATE TABLE "
			+ USERS_TABLE_NAME + " (" + USERS_COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERS_COLUMN_NAME
			+ " TEXT);";

	/*
	 * Comunity users table
	 */

	public static final String COMMUNITIES_USERS_TABLE_NAME = "communities_users";

	public static final String COMMUNITIES_USERS_COLUMN_COMMUNITY = "community";

	public static final String COMMUNITIES_USERS_COLUMN_USER = "user";

	private static final String CREATE_COMMUNITIES_USERS_TABLE = "CREATE TABLE "
			+ COMMUNITIES_USERS_TABLE_NAME
			+ " ("
			+ COMMUNITIES_USERS_COLUMN_COMMUNITY
			+ " INTEGER NOT NULL, "
			+ COMMUNITIES_USERS_COLUMN_USER
			+ " INTEGER NOT NULL,"
			+ "PRIMARY KEY ("
			+ COMMUNITIES_USERS_COLUMN_COMMUNITY
			+ ", "
			+ COMMUNITIES_USERS_COLUMN_USER
			+ "),"
			+ " FOREIGN KEY("
			+ COMMUNITIES_USERS_COLUMN_COMMUNITY
			+ ") REFERENCES "
			+ COMMUNITIES_TABLE_NAME
			+ " ("
			+ COMMUNITIES_COLUMN_ID
			+ "),  FOREIGN KEY("
			+ COMMUNITIES_USERS_COLUMN_USER
			+ ") REFERENCES "
			+ USERS_TABLE_NAME
			+ " ("
			+ USERS_COLUMN_ID
			+ "));";

	/*
	 * Matchs table
	 */

	public static final String MATCHS_TABLE_NAME = "matchs";

	public static final String MATCHS_COLUMN_ID = "id";

	public static final String MATCHS_COLUMN_DATE = "date";

	public static final String MATCHS_COLUMN_COMMUNITY = "community";

	private static final String CREATE_MATCHS_TABLE = "CREATE TABLE "
			+ MATCHS_TABLE_NAME + " (" + MATCHS_COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + MATCHS_COLUMN_DATE
			+ " INTEGER NOT NULL, " + MATCHS_COLUMN_COMMUNITY
			+ " INTEGER NOT NULL, FREIGN KEY (" + MATCHS_COLUMN_COMMUNITY
			+ ") REFERENCES " + COMMUNITIES_TABLE_NAME + " ("
			+ COMMUNITIES_COLUMN_ID + "))";

	/*
	 * Matchs datas table
	 */

	public static final String MATCHS_DATA_TABLE_NAME = "matchs_data";

	public static final String MATCHS_DATA_COLUMN_ID = "id";

	public static final String MATCHS_DATA_COLUMN_MATCH = "match";
	
	public static final String MATCHS_DATA_COLUMN_PLAYER = "player";
	
	/**
	 * Indicate (if >0 or True) that the player is playing in the home team.
	 */
	public static final String MATCHS_DATA_COLUMN_HOME = "home";
	
	public static final String MATCHS_DATA_COLUMN_GOALS = "goals";
	
	public static final String MATCHS_DATA_COLUMN_RED_CARDS = "red_cards";
	
	public static final String MATCHS_DATA_COLUMN_YELLOW_CARDS = "yellow_cards";
	
	private static final String CREATE_MATCHS_DATA_TABLE = "CREATE TABLE " + MATCHS_DATA_TABLE_NAME +
			" (" + MATCHS_DATA_COLUMN_ID +
			" INTEGER PRIMARY KEY AUTOINCREMENT, " + MATCHS_DATA_COLUMN_MATCH + " INTEGER NOT NULL, " +
			MATCHS_DATA_COLUMN_PLAYER + " INTEGER NOT NULL, " +
			MATCHS_DATA_COLUMN_HOME + " BOOLEAN DEFAULT 0, " +
			MATCHS_DATA_COLUMN_GOALS + " INTEGER DEFAULT 0, " +
			MATCHS_DATA_COLUMN_RED_CARDS + " INTEGER DEFAULT 0," +
			MATCHS_DATA_COLUMN_YELLOW_CARDS + " INTEGER DEFAULT 0," +
			"UNIQUE (" + MATCHS_DATA_COLUMN_MATCH +
			", " + MATCHS_DATA_COLUMN_PLAYER +
			")," +
			" FOREIGN KEY (" + MATCHS_DATA_COLUMN_MATCH +
			") " + MATCHS_TABLE_NAME +
			" (" + MATCHS_COLUMN_ID +
			"), " +
			" FOREIGN KEY (" + MATCHS_DATA_COLUMN_PLAYER +
			") " + USERS_TABLE_NAME +
			" (" + USERS_COLUMN_ID +
			")"
			+ ")";

	/*
	 * Properties of PersistentDataHelper.
	 */
	
	private Context context;

	public PersistentDataHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		this.context = context;

	}

	/*
	 * Players of match table
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_USERS_TABLE);

		db.execSQL(CREATE_COMMUNITIES_TABLE);

		db.execSQL(CREATE_COMMUNITIES_USERS_TABLE);

		db.execSQL(CREATE_MATCHS_TABLE);
		
		db.execSQL(CREATE_MATCHS_DATA_TABLE);

		insertInitialData(db);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private void insertInitialData(SQLiteDatabase db) {

		ContentValues values = new ContentValues();

		values.put(PersistentDataHelper.COMMUNITIES_COLUMN_NAME, this.context
				.getResources().getText(R.string.community_default_name)
				.toString());
		db.insert(PersistentDataHelper.COMMUNITIES_TABLE_NAME, null, values);

		values.clear();

		values.put(PersistentDataHelper.USERS_COLUMN_NAME, "Javier Alfonso");
		db.insert(PersistentDataHelper.USERS_TABLE_NAME, null, values);

		values.clear();

		values.put(PersistentDataHelper.USERS_COLUMN_NAME, "Benito");
		db.insert(PersistentDataHelper.USERS_TABLE_NAME, null, values);

		values.clear();

		values.put(PersistentDataHelper.COMMUNITIES_USERS_COLUMN_COMMUNITY, 1);
		values.put(COMMUNITIES_USERS_COLUMN_USER, 1);
		db.insert(COMMUNITIES_USERS_TABLE_NAME, null, values);

		values.clear();

		values.put(PersistentDataHelper.COMMUNITIES_USERS_COLUMN_COMMUNITY, 1);
		values.put(COMMUNITIES_USERS_COLUMN_USER, 2);
		db.insert(COMMUNITIES_USERS_TABLE_NAME, null, values);

	}

}
