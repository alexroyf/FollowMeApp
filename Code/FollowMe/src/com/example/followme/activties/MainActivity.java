package com.example.followme.activties;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
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

		registerForContextMenu(getListView());

		/*
		 * ListView lv; lv = getListView(); lv.setOnItemLongClickListener(new
		 * OnItemLongClickListener() {
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> arg0, View
		 * view, int position, long id) {
		 * 
		 * Toast.makeText(getApplicationContext(),
		 * "this is my Toast message!!! =)", Toast.LENGTH_LONG).show(); return
		 * true; } });
		 */
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_user_list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.delete_user:
			
			
			//.remove(info.position);
			
			
			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}

	/**
	 * List Item Click.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(new Intent(this, MapActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);

		mSearch = new SearchView(this);
		mSearch.setIconifiedByDefault(true);

		MenuItem m = menu.findItem(R.id.blz);

		m.setActionView(mSearch);

		return true;
	}

}
