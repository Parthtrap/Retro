package com.parthtrap.donationapp.HelperApadters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancedSearchItemAdapter extends RecyclerView.Adapter<AdvancedSearchItemAdapter.ViewHolder> {

    private List<ExchangeItemClass> itemStorage;
    OnItemClickListener listener;

    public AdvancedSearchItemAdapter(List<ExchangeItemClass> itemStorage) {
        this.itemStorage = itemStorage;
    }

    @NonNull
    @NotNull
    @Override
    public AdvancedSearchItemAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_profile_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdvancedSearchItemAdapter.ViewHolder holder, int position) {
        holder.NameBox.setText(itemStorage.get(position).getName().substring(0, 1).toUpperCase() + itemStorage.get(position).getName().substring(1));
        holder.DescBox.setText(itemStorage.get(position).getDescription());
        Picasso.get().load(itemStorage.get(position).getImageURL()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return itemStorage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView NameBox, DescBox;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageItemRecyclerViewProfile);
            NameBox = itemView.findViewById(R.id.NameItemRecyclerViewProfile);
            DescBox = itemView.findViewById(R.id.DescItemRecyclerViewProfile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(itemStorage.get(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ExchangeItemClass item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
