package com.xjakubs.nasa;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xjakubs.nasa.meteorite.Meteorite;
import com.xjakubs.nasa.meteorite.MeteoriteAdapter;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MateoriteListActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mateorite_list);

		List<Meteorite> meteoriteList = (List<Meteorite>) getIntent().getExtras().getSerializable("data");

		RecyclerView recyclerView = findViewById(R.id.dataList);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		MeteoriteAdapter meteoriteAdapter = new MeteoriteAdapter(this, meteoriteList);
		recyclerView.setAdapter(meteoriteAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
//		switch (id) {
//			case R.id.action_about: return true;
//			default: return true;
//		}

		return super.onOptionsItemSelected(item);
	}
}