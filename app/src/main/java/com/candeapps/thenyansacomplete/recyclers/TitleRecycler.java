package com.candeapps.thenyansacomplete.recyclers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.candeapps.thenyansacomplete.Objects.DataItem;
import com.candeapps.thenyansacomplete.R;

import java.util.ArrayList;


public class TitleRecycler extends RecyclerView.Adapter<TitleRecycler.MyViewHolder> {

    private Context context;
    private ArrayList<DataItem> items;
    private int mExpandedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button title;
        public TextView megaTitle;
        private RecyclerView recyclerView;

        private MyViewHolder(View view) {
            super(view);
            megaTitle = view.findViewById(R.id.megaTitle);
            title = view.findViewById(R.id.full_title);
            recyclerView = view.findViewById(R.id.my_recycler_view);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(manager);
            RecyclerView.Adapter adapterClient = new com.candeapps.thenyansacomplete.recyclers.MainRecycler(null);
            recyclerView.setAdapter(adapterClient);
        }
    }

    public TitleRecycler(ArrayList<DataItem> items){
        if(items != null) {
            this.items = items;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.titles_recycler_view, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DataItem currentItem = items.get(position);
        if(items != null) {
            holder.title.setText(currentItem.getTitle());
            //Call display for DataItem info
            holder.recyclerView.setVisibility(View.VISIBLE);
            RecyclerView.Adapter adapterClient = new com.candeapps.thenyansacomplete.recyclers.MainRecycler(currentItem);
            holder.recyclerView.setAdapter(adapterClient);
            adapterClient.notifyDataSetChanged();

            //Method to expand on click
            final boolean isExpanded = position == mExpandedPosition;
            holder.recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.itemView.setActivated(isExpanded);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1 : position;
                    notifyItemChanged(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(items != null) {
            return items.size();
        }
        return 0;
    }
}
