package my.com.tm.sense;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

class SectionCategoryReward extends StatelessSection {
    ArrayList<categotymodel> itemList;
    String title;
    SectionedRecyclerViewAdapter section;
    Context ctx;

    public SectionCategoryReward(String title, ArrayList<categotymodel> itemlist, SectionedRecyclerViewAdapter section, Context ctx) {


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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyItemViewHolder itemHolder = (MyItemViewHolder) holder;



//
//        itemHolder.preplan_status.setText("STATUS:"+(CharSequence) itemList.get(position).getPreplan_status());
//        itemHolder.preplan_name.setText("PREPLAN NAME:"+(CharSequence) itemList.get(position).getPreplan_name());
//        itemHolder.preplan_id.setText("PREPLAN ID:"+(CharSequence) itemList.get(position).getPreplan_id());
//
//        itemHolder.createdby.setText("Createdby:"+(CharSequence) itemList.get(position).getCreated_by());
//        itemHolder.updatedby.setText("Updatedby:"+(CharSequence) itemList.get(position).getUpdate_by());
//        itemHolder.project_required.setText("Project Required:"+(CharSequence) itemList.get(position).getProject_required());




//        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ctx,
//                        String.format("Clicked on position #%s of Section %s",
//                                itemList.get(section.getPositionInSection(itemHolder.getAdapterPosition())).getOrderid(),
//                                title),
//                        Toast.LENGTH_SHORT).show();
//
//
//
//
//            }
//        });

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.titleCategory);
        }
    }




    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        private TextView preplan_status, preplan_name, preplan_id,createdby,updatedby,project_required;


        private View rootView;

        private MyItemViewHolder(View view) {
            super(view);


//            preplan_status = (TextView) view.findViewById(R.id.preplan_status);
//            preplan_name = (TextView) view.findViewById(R.id.preplan_name);
//            preplan_id = (TextView) view.findViewById(R.id.REMARK);
//            createdby = (TextView) view.findViewById(R.id.createdby);
//            updatedby = (TextView) view.findViewById(R.id.updateby);
//            project_required = (TextView) view.findViewById(R.id.projectrequired);






            rootView = view;
        }
    }
}
