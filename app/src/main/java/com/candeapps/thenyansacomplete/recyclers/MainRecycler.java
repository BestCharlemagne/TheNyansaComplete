package com.candeapps.thenyansacomplete.recyclers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.candeapps.thenyansacomplete.Objects.DataItem;
import com.candeapps.thenyansacomplete.R;

public class MainRecycler extends RecyclerView.Adapter<MainRecycler.MyViewHolder>{

    private Context context;
    private DataItem item;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView body,subTitle;
        private RecyclerView recyclerView;
        private LinearLayout layout;
        private final View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            body = view.findViewById(R.id.body);
            subTitle = view.findViewById(R.id.sub_title);
            layout = view.findViewById(R.id.holderId);

            recyclerView = view.findViewById(R.id.my_recycler_view);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(manager);
            RecyclerView.Adapter adapterClient = new TitleRecycler(null);
            recyclerView.setAdapter(adapterClient);
        }
    }

    public MainRecycler(DataItem item){
        if(item != null) {
            this.item = item;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout_client, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(item != null) {
            if (position < item.selfSize()) {
                holder.subTitle.setText(item.getSubtitle(context, position));
                holder.body.setText(item.getBody(context, position));
            }
            else if(item.getSubGroup() != null) {
                holder.layout.setVisibility(View.GONE);
                //Call display for next title
                holder.recyclerView.setVisibility(View.VISIBLE);
                RecyclerView.Adapter adapterClient = new TitleRecycler(item.getSubGroup());
                holder.recyclerView.setAdapter(adapterClient);
                adapterClient.notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        if(item != null) {
            return item.size();
        }
        return 0;
    }
}
