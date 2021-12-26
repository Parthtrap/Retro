package com.parthtrap.donationapp.HelperApadters;

import android.util.Log;
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
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ExchangeItemDisplayAdapter extends FirestoreRecyclerAdapter<ExchangeItemClass, ExchangeItemDisplayAdapter.ItemHolder>{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("EXCHANGE_ITEMS_DISPLAY_ADAPTER_LOGS");

	// Adding onClick to the Adapter
	OnItemClickListener listener;

	// Constructor
	public ExchangeItemDisplayAdapter(@NonNull @NotNull FirestoreRecyclerOptions<ExchangeItemClass> options){
		super(options);
	}

	// Binding Items to the View Holder
	@Override protected void onBindViewHolder(@NonNull @NotNull ItemHolder holder, int position, @NonNull @NotNull ExchangeItemClass model){
		holder.NameBox.setText(model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1));
		holder.DescBox.setText(model.getDescription());
		Picasso.get().load(model.getImageURL()).into(holder.ImageBox);
		Log.d(LOG.getLOCAL_LOG(), "Adapter Binded to View Holder");
	}

	// Setting View Holder.... AKA the Individual Cards that are to be displayed in the recycler view
	@NonNull @NotNull @Override public ItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType){
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_profile_page, parent, false);
		return new ItemHolder(v);
	}

	// Putting View Holders to Recycler View
	class ItemHolder extends RecyclerView.ViewHolder{

		TextView NameBox, DescBox;
		ImageView ImageBox;

		public ItemHolder(@NonNull @NotNull View itemView){
			super(itemView);
			NameBox = itemView.findViewById(R.id.NameItemRecyclerViewProfile);
			DescBox = itemView.findViewById(R.id.DescItemRecyclerViewProfile);
			ImageBox = itemView.findViewById(R.id.ImageItemRecyclerViewProfile);
			Log.d(LOG.getLOCAL_LOG(), "View Holder Defined");

			// Setting OnClick to view Holder
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

	// creating an abstract class
	public interface OnItemClickListener{

		void onItemClick(DocumentSnapshot documentSnapshot, int position);
	}

	// OnItemClick Listener setter
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}
}
