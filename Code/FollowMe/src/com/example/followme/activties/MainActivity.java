package com.example.followme.activties;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.example.followme.R;
import com.example.followme.adapters.UsersAdapter;
import com.example.followme.modules.User;

public class MainActivity extends SherlockListActivity {
	private List<User> mData;

	// SearchView
	private SearchView mSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mData = new ArrayList<User>();
		mData.add(new User("Mishka", false));
		mData.add(new User("Gayness", true));
		mData.add(new User("Alex", false));

		UsersAdapter adapter = new UsersAdapter(this, R.layout.user_list_item,
				mData);
		setListAdapter(adapter);

	}
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);

		mSearch = new SearchView(this);
		mSearch.setIconifiedByDefault(true);
		
		MenuItem m = menu.findItem(R.id.blz);
		
		
	//	menu.add("").setActionView(mSearch);
		
		m.setActionView(mSearch);
		
		
		
		
		
//		menu.add("").setActionView(mSearch)
//				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

}
