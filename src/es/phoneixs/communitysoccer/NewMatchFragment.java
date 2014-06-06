package es.phoneixs.communitysoccer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import es.phoneixs.communitysoccer.model.PersistentDataHelper;
import es.phoneixs.communitysoccer.model.SaveMatch;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A fragment that let you create a new match result.
 * 
 * @author Javier Alfonso <phoneixsegovia@gmail.com>
 * @version 20140511
 */
public class NewMatchFragment extends Fragment implements OnClickListener {

	private final class AddPlayersListener implements View.OnClickListener {

		private boolean addHomePlayers;

		public AddPlayersListener(boolean addHomePlayers) {

			this.addHomePlayers = addHomePlayers;

		}

		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getActivity(), SelectPlayers.class);

			if (addHomePlayers) {

				intent.putIntegerArrayListExtra(SelectPlayers.SELECTED_IDS,
						homePlayersIds);

				intent.putIntegerArrayListExtra(SelectPlayers.DISABLED_PLAYERS,
						visitorPlayersIds);

				startActivityForResult(intent,
						NewMatch.PICK_HOME_PLAYERS_REQUEST);
			} else {

				intent.putIntegerArrayListExtra(SelectPlayers.SELECTED_IDS,
						visitorPlayersIds);

				intent.putIntegerArrayListExtra(SelectPlayers.DISABLED_PLAYERS,
						homePlayersIds);

				startActivityForResult(intent,
						NewMatch.PICK_VISITOR_PLAYERS_REQUEST);
			}

		}
	}

	/**
	 * Indicate a key to be used when need to store the home players
	 * identifiers.
	 * 
	 * @since 20140511
	 */
	private static final String HOME_PLAYERS_IDS = "HOME_PLAYERS_ID";

	private static final String HOME_PLAYERS_NAMES = "HOME_PLAYERS_NAMES";

	/**
	 * Indicate a key to be used to store the visitor players identifiers.
	 * 
	 * @since 20140511
	 */
	private static final String VISITOR_PLAYERS_IDS = "VISITOR_PLAYERS_IDS";

	private static final String VISITOR_PLAYERS_NAMES = "VISITOR_PLAYERS_NAMES";

	private View rootView;

	private ArrayList<Integer> homePlayersIds;
	private ArrayList<String> homePlayersNames;
	private ArrayList<EditText> homePlayersGoals;
	private ArrayList<EditText> homePlayersRedCards;
	private ArrayList<EditText> homePlayersYellowCards;
	private ArrayList<Integer> visitorPlayersIds;
	private ArrayList<String> visitorPlayersNames;
	private ArrayList<EditText> visitorPlayersGoals;
	private ArrayList<EditText> visitorPlayersRedCards;
	private ArrayList<EditText> visitorPlayersYellowCards;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_new_match, container,
				false);

		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());

		TextView tvDate = (TextView) rootView.findViewById(R.id.tvDate);

		tvDate.setText(currentDateTimeString);

		rootView.findViewById(R.id.btnAddHomePlayer).setOnClickListener(
				new AddPlayersListener(true));

		rootView.findViewById(R.id.btnAddVisitorPlayer).setOnClickListener(
				new AddPlayersListener(false));

		if (savedInstanceState == null) {

			// It's new, create an empty state.

			this.homePlayersIds = new ArrayList<Integer>();
			this.homePlayersNames = new ArrayList<String>();
			this.homePlayersGoals = new ArrayList<EditText>();
			this.homePlayersRedCards = new ArrayList<EditText>();
			this.homePlayersYellowCards = new ArrayList<EditText>();
			this.visitorPlayersIds = new ArrayList<Integer>();
			this.visitorPlayersNames = new ArrayList<String>();
			this.visitorPlayersGoals = new ArrayList<EditText>();
			this.visitorPlayersRedCards = new ArrayList<EditText>();
			this.visitorPlayersYellowCards = new ArrayList<EditText>();

		} else {

			// We need to restore the previous state.

			this.homePlayersIds = savedInstanceState
					.getIntegerArrayList(HOME_PLAYERS_IDS);
			this.homePlayersNames = savedInstanceState
					.getStringArrayList(HOME_PLAYERS_NAMES);
			this.visitorPlayersIds = savedInstanceState
					.getIntegerArrayList(VISITOR_PLAYERS_IDS);
			this.visitorPlayersNames = savedInstanceState
					.getStringArrayList(VISITOR_PLAYERS_NAMES);

			this.homePlayersGoals = new ArrayList<EditText>();
			this.homePlayersRedCards = new ArrayList<EditText>();
			this.homePlayersYellowCards = new ArrayList<EditText>();
			this.visitorPlayersGoals = new ArrayList<EditText>();
			this.visitorPlayersRedCards = new ArrayList<EditText>();
			this.visitorPlayersYellowCards = new ArrayList<EditText>();

			// And add GUI elements.

			createListOfPlayers(rootView.findViewById(R.id.tvNoHomePlayers),
					(GridLayout) rootView.findViewById(R.id.llHomePlayers),
					this.homePlayersNames, this.homePlayersGoals,
					this.homePlayersRedCards, this.homePlayersYellowCards);

			createListOfPlayers(rootView.findViewById(R.id.tvNoVisitorPlayers),
					(GridLayout) rootView.findViewById(R.id.llVisitorPlayers),
					this.visitorPlayersNames, this.visitorPlayersGoals,
					this.visitorPlayersRedCards, this.visitorPlayersYellowCards);

		}

		rootView.findViewById(R.id.btnSaveMatch).setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d("NewMatch", "Return from result: RQ=" + requestCode + ", RS="
				+ resultCode);

		// If the request went well (OK) and the request was
		// PICK_CONTACT_REQUEST
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case NewMatch.PICK_HOME_PLAYERS_REQUEST:

				setPlayers(data, true);

				break;

			case NewMatch.PICK_VISITOR_PLAYERS_REQUEST:
				setPlayers(data, false);
				break;

			default:
				break;
			}

		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// We need to save the lists of players

		outState.putIntegerArrayList(HOME_PLAYERS_IDS, this.homePlayersIds);
		outState.putStringArrayList(HOME_PLAYERS_NAMES, this.homePlayersNames);

		outState.putIntegerArrayList(VISITOR_PLAYERS_IDS,
				this.visitorPlayersIds);
		outState.putStringArrayList(VISITOR_PLAYERS_NAMES,
				this.visitorPlayersNames);

	}

	/**
	 * Add the players in the intent data to the list of home players or visitor
	 * players.
	 * 
	 * @param data
	 *            The intent that have the data of what players must be added.
	 * @param replaceHomePlayers
	 *            If players must be set for home or for visitor.
	 * @since 20140504
	 */
	private void setPlayers(Intent data, boolean replaceHomePlayers) {

		View tvNoPlayers;
		GridLayout llPlayers;

		// Get the players of the team that we are configuring.

		ArrayList<Integer> selectedPlayersIds = data
				.getIntegerArrayListExtra(SelectPlayers.SELECTED_IDS);
		ArrayList<String> selectedPlayersNames = data
				.getStringArrayListExtra(SelectPlayers.SELECTED_NAMES);

		if (replaceHomePlayers) {

			tvNoPlayers = rootView.findViewById(R.id.tvNoHomePlayers);
			llPlayers = (GridLayout) rootView.findViewById(R.id.llHomePlayers);

			homePlayersIds = selectedPlayersIds;
			homePlayersNames = selectedPlayersNames;

			createListOfPlayers(tvNoPlayers, llPlayers, selectedPlayersNames,
					this.homePlayersGoals, this.homePlayersRedCards,
					this.homePlayersYellowCards);

		} else {

			tvNoPlayers = rootView.findViewById(R.id.tvNoVisitorPlayers);
			llPlayers = (GridLayout) rootView
					.findViewById(R.id.llVisitorPlayers);

			visitorPlayersIds = selectedPlayersIds;
			visitorPlayersNames = selectedPlayersNames;

			createListOfPlayers(tvNoPlayers, llPlayers, selectedPlayersNames,
					this.visitorPlayersGoals, this.visitorPlayersRedCards,
					this.visitorPlayersYellowCards);

		}
	}

	/**
	 * Create the controls of players in the lists into the noPlayersView.
	 * 
	 * @param noPlayersView
	 *            The view to tell the user there is not player.
	 * @param playersLayout
	 *            The layout in which the controls will be created.
	 * @param playersNames
	 *            The names of the players.
	 * @param goals
	 *            The list in which we will save the edittext that have the
	 *            goals. It will be cleared and then added the new elements.
	 * @param redCards
	 *            The list in which we will save the edittext that have the red
	 *            cards. It will be cleared and then added the new elements.
	 * @param yellowCards
	 *            The list in which we will save the edittext that have the
	 *            yellow cards. It will be cleared and then added the new
	 *            elements.
	 * @since 20140511
	 */
	private void createListOfPlayers(View noPlayersView,
			GridLayout playersLayout, ArrayList<String> playersNames,
			ArrayList<EditText> goals, ArrayList<EditText> redCards,
			ArrayList<EditText> yellowCards) {

		if (playersNames.isEmpty()) {

			noPlayersView.setVisibility(View.VISIBLE);
			playersLayout.setVisibility(View.GONE);
			playersLayout.removeAllViews();

		} else {

			noPlayersView.setVisibility(View.GONE);
			playersLayout.setVisibility(View.VISIBLE);
			playersLayout.removeAllViews();

			LayoutInflater inflater = LayoutInflater.from(this.getActivity());

			playersLayout.setRowCount(1 + playersNames.size());

			goals.clear();
			redCards.clear();
			yellowCards.clear();

			int id = 0;

			for (String name : playersNames) {

				inflater.inflate(R.layout.player_in_match, playersLayout, true);

				TextView tvName = (TextView) playersLayout
						.getChildAt(playersLayout.getChildCount() - 4);

				tvName.setId(id++);

				tvName.setText(name);

				EditText v = (EditText) playersLayout.getChildAt(playersLayout
						.getChildCount() - 3);
				v.setId(id++);
				goals.add(v);

				v = (EditText) playersLayout.getChildAt(playersLayout
						.getChildCount() - 2);
				v.setId(id++);
				yellowCards.add(v);

				v = (EditText) playersLayout.getChildAt(playersLayout
						.getChildCount() - 1);
				v.setId(id++);
				redCards.add(v);

			}

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnSaveMatch:
			saveMatchData();
			break;

		default:
			break;
		}

	}

	/**
	 * Save the current data of the match as a new entry in the database.
	 */
	private void saveMatchData() {
		
		// TODO Use the current community instead of 1.
		// TODO Use the selected date instead of the current.
		SaveMatch sm = new SaveMatch(this.getActivity(), 1, System.currentTimeMillis(),
				this.homePlayersIds,
				extractDataFromView(this.homePlayersGoals),
				extractDataFromView(this.homePlayersRedCards),
				extractDataFromView(this.homePlayersYellowCards),
				this.visitorPlayersIds,
				extractDataFromView(this.visitorPlayersGoals),
				extractDataFromView(this.visitorPlayersRedCards),
				extractDataFromView(this.visitorPlayersYellowCards));
		
		// Do the work in background.
		sm.execute();
		
		// And return to the main menu.
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	/**
	 * Get the integer values of the {@link EditText} list.
	 * 
	 * @param views
	 *            The list of view which will be used to get the values.
	 * @return A list with the values of the {@link EditText} in the argument
	 *         list.
	 * @since 20140524
	 */
	private ArrayList<Integer> extractDataFromView(List<EditText> views) {

		ArrayList<Integer> result = new ArrayList<Integer>(views.size());

		for (EditText editText : views) {

			String value = editText.getText().toString();

			if (value.equals("")) {

				result.add(Integer.valueOf(0));

			} else {

				try {

					result.add(Integer.valueOf(value));

				} catch (NumberFormatException e) {

					Log.e("es.phoneixs.communitysoccer.savingmatch", String
							.format("Can't convert a value %s to an integer",
									value), e);

					result.add(Integer.valueOf(0));

				}

			}

		}

		return result;

	}

}