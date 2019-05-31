package my.com.tm.sense;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

class SectionCategoryReward extends StatelessSection {
    ArrayList<rewardmodel> itemList;
    String title;
    SectionedRecyclerViewAdapter section;
    Context ctx;

    public SectionCategoryReward(String title, ArrayList<rewardmodel> itemlist, SectionedRecyclerViewAdapter section, Context ctx) {


        super(SectionParameters.builder()
                .itemResourceId(R.layout.category1_row)
                .headerResourceId(R.layout.category1_header)
                .build());
        this.itemList = itemlist;
        this.title = title;
        this.section = section;
        this.ctx = ctx;
    }






    @Override
    public int getContentItemsTotal() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyItemViewHolder itemHolder = (MyItemViewHolder) holder;




        itemHolder.item_name.setText( itemList.get(position).getItem());
        itemHolder.point.setText( itemList.get(position).getPoint());


        if(itemList.get(position).getId().isEmpty()){

            itemHolder.deleteitem.setVisibility(View.INVISIBLE);
        }

        itemHolder.deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardActivity.getInstance().deleteItem(itemList.get(position).getId());
            }
        });


        if(itemList.get(position).getItem().equals("Zero Medical Certificate")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon1);
        }
        if(itemList.get(position).getItem().equals("High Performer Per Division (9-10)")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon2);
        }
        if(itemList.get(position).getItem().equals("Moderate per Division (7-8)")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon2);
        }

        if(itemList.get(position).getItem().equals("360 > 4 TM Award(GLT,GCEO,CIIC,NPC)")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon3);
        }
        if(itemList.get(position).getItem().equals("Per 1s Sort,Set in Order,Shine, Standardize,Sustain")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon4);
        }
        if(itemList.get(position).getItem().equals("Organizer Of the Event/Volunteers")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon51);
        }
        if(itemList.get(position).getItem().equals("Participant of the Event")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon52);
        }
        if(itemList.get(position).getItem().equals("Supporters of the Event")) {
            itemHolder.iconimage.setImageResource(R.mipmap.icon53);
        }



    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);


        headerHolder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RewardActivity.getInstance().addItem(title);
            }
        });

    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final Button addItem;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.titleCategory);
            addItem = (Button)view.findViewById(R.id.createaissues);
        }
    }




    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name, point;
        private Button deleteitem;
        private ImageView iconimage;


        private View rootView;

        private MyItemViewHolder(View view) {
            super(view);


            item_name = (TextView) view.findViewById(R.id.item);
            point = (TextView) view.findViewById(R.id.point);
            deleteitem = (Button)view.findViewById(R.id.deleteitem);
            iconimage = (ImageView)view.findViewById(R.id.iconachievement);








            rootView = view;
        }
    }
}
