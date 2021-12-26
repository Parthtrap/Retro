package com.parthtrap.donationapp.HelperApadters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancedSearchDonateItemAdapter extends RecyclerView.Adapter<AdvancedSearchDonateItemAdapter.ViewHolder>{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("DONATE_ITEMS_ADVANCED_SEARCH_ADAPTER_LOGS");

	private List<DonateItemClass> itemStorage;
	OnItemClickListener listener;

	// Constructor
	public AdvancedSearchDonateItemAdapter(List<DonateItemClass> itemStorage){
		this.itemStorage = itemStorage;
	}

	// Setting View Holder.... AKA the Individual Cards that are to be displayed in the recycler view
	@NonNull @NotNull @Override public AdvancedSearchDonateItemAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_profile_page, parent, false);
		return new ViewHolder(view);
	}

	// Binding Items to the View Holder
	@Override public void onBindViewHolder(@NonNull @NotNull AdvancedSearchDonateItemAdapter.ViewHolder holder, int position){
		holder.NameBox.setText(itemStorage.get(position).getName().substring(0, 1).toUpperCase() + itemStorage.get(position).getName().substring(1));
		holder.DescBox.setText(itemStorage.get(position).getDescription());
		Picasso.get().load(itemStorage.get(position).getImageURL()).into(holder.imageView);
		Log.d(LOG.getLOCAL_LOG(), "Adapter Binded to View Holder");
	}

	// Keeping the count of the items to be displayed
	@Override public int getItemCount(){
		return itemStorage.size();
	}

	// Putting View Holders to Recycler View
	public class ViewHolder extends RecyclerView.ViewHolder{

		ImageView imageView;
		TextView NameBox, DescBox;

		public ViewHolder(@NonNull @NotNull View itemView){
			super(itemView);
			imageView = itemView.findViewById(R.id.ImageItemRecyclerViewProfile);
			NameBox = itemView.findViewById(R.id.NameItemRecyclerViewProfile);
			DescBox = itemView.findViewById(R.id.DescItemRecyclerViewProfile);
			Log.d(LOG.getLOCAL_LOG(), "View Holder Defined");

			// Setting OnClick to view Holder
			itemView.setOnClickListener(new View.OnClickListener(){
				@Override public void onClick(View v){
					if(listener != null){
						int position = getAdapterPosition();
						listener.onItemClick(itemStorage.get(position), position);
					}
				}
			});
		}
	}

	// creating an abstract class
	public interface OnItemClickListener{

		void onItemClick(DonateItemClass item, int position);
	}

	// OnItemClick Listener setter
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}

}
