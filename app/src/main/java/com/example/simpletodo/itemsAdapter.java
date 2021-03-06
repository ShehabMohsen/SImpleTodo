package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Responsible for displaying data from the model into a row in the recycler view
public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    public itemsAdapter(List<String> items, OnLongClickListener longCLickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longCLickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflater to populate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }
    //Responsible for binding data to a particular View Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position
        String item = items.get(position);
        //Bind the item into the specified View Holder
        holder.bind(item);
    }

    //Returns how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update the view inside the View Holder with the data of String Item
        public void bind(String item) {
        tvItem.setText(item);
        tvItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            clickListener.onItemClicked(getAdapterPosition());
            }

        });
        tvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View V) {
                //Notify the listener which position is long pressed
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            }
        });
        }
    }
}
