package es.phoneixs.communitysoccer;

import es.phoneixs.communitysoccer.model.PersistentDataHelper;
import es.phoneixs.communitysoccer.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PersistentDataHelper pdh = new PersistentDataHelper(this);
		
		SQLiteDatabase db = pdh.getReadableDatabase();
		
		String columns[] = {PersistentDataHelper.COMMUNITIES_COLUMN_NAME};
		
		Cursor cursor = db.query(PersistentDataHelper.COMMUNITIES_TABLE_NAME, columns, null, null, null, null, "1");
		
		if (cursor != null && cursor.moveToFirst()) {
			
			String name = cursor.getString(0);
			
			TextView tvNombre = (TextView) findViewById(R.id.tvNombreComunidad);
			
			tvNombre.setText(name);
			
		}
		
		findViewById(R.id.btnNewMatch).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Start the new match activity
				Intent newMatchIntent = new Intent(MainActivity.this, NewMatch.class);
				startActivity(newMatchIntent);
				
			}
		});
		
		findViewById(R.id.btnViewStatistics).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Start the statistics menu activity.
				Intent statisticsIntent = new Intent(MainActivity.this, StatisticsMainActivity.class);
				startActivity(statisticsIntent);
				
			}
		});
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

}
