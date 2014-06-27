package es.phoneixs.communitysoccer;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class RankingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rankings);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rankings, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnClickListener {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_rankings,
					container, false);
			
			rootView.findViewById(R.id.btnByScore).setOnClickListener(this);
			rootView.findViewById(R.id.btnByGoals).setOnClickListener(this);
			rootView.findViewById(R.id.btnByYellowCards).setOnClickListener(this);
			rootView.findViewById(R.id.btnByRedCards).setOnClickListener(this);
			
			return rootView;
		}
		
		public void onClick(View v) {
			
			Class<?> c = null;
			
			switch (v.getId()) {
			case R.id.btnByScore:
				c = RankingByScoreActivity.class;
				break;
				
			case R.id.btnByGoals:
				c = RankingByGoalsActivity.class;
				break;
				
			case R.id.btnByYellowCards:
				c = RankingByYellowCardsActivity.class;
				break;
				
			case R.id.btnByRedCards:
				c = RankingByRedCardsActivity.class;
				break;

			default:
				Log.w("rankings", "A button was pushed that we don't know what to do with it.");
				return;
			}
			
			Intent intent = new Intent(getActivity(), c);
			
			this.getActivity().startActivity(intent);
			
		}
		
	}

}
