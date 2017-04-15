package com.foroughi.pedram.storial.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foroughi.pedram.storial.R;
import com.foroughi.pedram.storial.model.Story;

import java.util.ArrayList;

/**
 * Created by Pedram on 4/14/2017.
 */

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoryHolder> {

    private ArrayList<Story> items;
    OnStoryClickedListener listener;
    public interface OnStoryClickedListener{
        void onStorySelected(String id);
    }

    public StoryRecyclerAdapter(ArrayList<Story> items, OnStoryClickedListener listener) {
        this.items = items;
        this.listener = listener;
        if (items == null)
            this.items = new ArrayList<>();
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new StoryHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_story, parent,false));
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<Story> items) {
        this.items.addAll(items);
        if (this.items.size() == items.size())
            notifyDataSetChanged();
        else
            notifyItemRangeInserted(this.items.size() - items.size(), items.size());
    }

    public void setItems(ArrayList<Story> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(Story item) {
        this.items.add(0,item);
        if (this.items.size()==1)
            notifyDataSetChanged();
        else
            notifyItemInserted(0);
    }

    public class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title;
        TextView tv_content;

        public StoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_title = (TextView) itemView.findViewById(R.id.story_tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.story_tv_content);
        }

        public void bind(int position) {

            tv_title.setText(items.get(position).getTitle());
            tv_content.setText(items.get(position).getContent());
        }

        @Override
        public void onClick(View view) {
            listener.onStorySelected(items.get(getAdapterPosition()).getId());
        }
    }
}
