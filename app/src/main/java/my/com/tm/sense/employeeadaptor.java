package my.com.tm.sense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 1/25/2016.
 */
public class employeeadaptor extends ArrayAdapter implements Filterable {

    private List<employeemodel> ttmodellist;
    private List<employeemodel> orig;
    private int resource;
    private LayoutInflater inflator;

    public employeeadaptor(Context context, int resource, List<employeemodel> objects) {
        super(context, resource, objects);

        ttmodellist = objects;
        this.resource = resource;
        inflator = (LayoutInflater)getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }



    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<employeemodel> results = new ArrayList<employeemodel>();
                if (orig == null)
                    orig = ttmodellist;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final employeemodel g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            else if (g.getDivision().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                            else if(g.getEmail().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);

                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                ttmodellist = (ArrayList<employeemodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ttmodellist.size();
    }

    @Override
    public Object getItem(int position) {
        return ttmodellist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = inflator.inflate(resource,null);
        }


        TextView name;
        TextView email;
        TextView division;
        TextView staffid;

        name = (TextView)convertView.findViewById(R.id.employeename);
        email = (TextView)convertView.findViewById(R.id.employeeemail);
        division = (TextView)convertView.findViewById(R.id.employeediv);
        staffid = (TextView)convertView.findViewById(R.id.employeestaffid);

        name.setText(ttmodellist.get(position).getName());
        email.setText(ttmodellist.get(position).getEmail());
        division.setText(ttmodellist.get(position).getDivision());
        staffid.setText(ttmodellist.get(position).getStaffid());




        return convertView;
    }
}