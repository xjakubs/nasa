package com.xjakubs.nasa.meteorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xjakubs.nasa.MapsActivity;
import com.xjakubs.nasa.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MeteoriteAdapter extends RecyclerView.Adapter<MeteoriteAdapter.MyViewHolder> {

	private Context mCtx;
	private List<Meteorite> mMeteoriteList;
	private View mCard;


	public MeteoriteAdapter(Context ctx, List<Meteorite> meteoriteList) {
		this.mCtx = ctx;
		this.mMeteoriteList = meteoriteList;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		TextView mName;
		TextView mId;
		TextView mReclass;
		TextView mMass;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			mName = (TextView) itemView.findViewById(R.id.dataName);
			mId = (TextView) itemView.findViewById(R.id.dataId);
			mReclass = (TextView) itemView.findViewById(R.id.dataReclass);
			mMass = (TextView) itemView.findViewById(R.id.dataMass);
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		mCard = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.meteorite_card, parent, false);
		MyViewHolder myViewHolder = new MyViewHolder(mCard);
		return myViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		holder.mName.setText(mMeteoriteList.get(position).getName());
		holder.mId.setText(mMeteoriteList.get(position).getId());
		holder.mReclass.setText(mMeteoriteList.get(position).getRecclass());
		holder.mMass.setText(mMeteoriteList.get(position).getMass() + "g");

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mCtx, MapsActivity.class);
				i.putExtra("name", mMeteoriteList.get(position).getName());
				i.putExtra("reclat", mMeteoriteList.get(position).getReclat());
				i.putExtra("reclong", mMeteoriteList.get(position).getReclong());
				mCtx.startActivity(i);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mMeteoriteList.size();
	}
}
