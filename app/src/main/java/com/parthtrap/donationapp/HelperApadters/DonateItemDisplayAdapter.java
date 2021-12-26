package com.parthtrap.donationapp.HelperApadters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class DonateItemDisplayAdapter extends FirestoreRecyclerAdapter<DonateItemClass, DonateItemDisplayAdapter.ItemHolder>{

	OnItemClickListener listener;

	public DonateItemDisplayAdapter(@NonNull @NotNull FirestoreRecyclerOptions<DonateItemClass> options){
		super(options);
	}

	@Override protected void onBindViewHolder(@NonNull @NotNull ItemHolder holder, int position, @NonNull @NotNull DonateItemClass model){
		holder.NameBox.setText(model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1));
		holder.DescBox.setText(model.getDescription());
		Picasso.get().load(model.getImageURL()).into(holder.ImageBox);
	}

	@NonNull @NotNull @Override public ItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType){
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_profile_page, parent, false);
		return new ItemHolder(v);
	}

	class ItemHolder extends RecyclerView.ViewHolder{

		TextView NameBox, DescBox;
		ImageView ImageBox;

		public ItemHolder(@NonNull @NotNull View itemView){
			super(itemView);
			NameBox = itemView.findViewById(R.id.NameItemRecyclerViewProfile);
			DescBox = itemView.findViewById(R.id.DescItemRecyclerViewProfile);
			ImageBox = itemView.findViewById(R.id.ImageItemRecyclerViewProfile);

			itemView.setOnClickListener(new View.OnClickListener(){
				@Override public void onClick(View v){
					if(listener != null){
						int position = getAdapterPosition();
						listener.onItemClick(getSnapshots().getSnapshot(position), position);
					}

				}
			});
		}
	}

	public interface OnItemClickListener{

		void onItemClick(DocumentSnapshot documentSnapshot, int position);
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}
}
