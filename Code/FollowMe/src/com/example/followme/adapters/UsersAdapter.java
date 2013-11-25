package com.example.followme.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.followme.R;
import com.example.followme.modules.User;

public class UsersAdapter extends ArrayAdapter {

	private LayoutInflater mInflater;

	public UsersAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.user_list_item, null);

		TextView text = (TextView) view.findViewById(R.id.user_name);
		ImageView img = (ImageView) view.findViewById(R.id.img_ppl);

		User user = (User) getItem(position);

		text.setText(user.getUserText());

		if (user.isUserOnline())
			img.setImageResource(R.drawable.green_button);
		else
			img.setImageResource(R.drawable.red_button);
		return view;
	}

}
