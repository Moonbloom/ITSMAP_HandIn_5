package grp14.itsmap.com.hi514.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import grp14.itsmap.com.hi514.R;
import grp14.itsmap.com.hi514.models.Train;

public class TrainListAdapter extends ArrayAdapter<Train> {

    //region Variables
    private Context context;
    private int resource;
    private List<Train> list;
    private List<Train> originalList;


    private TrainListFilter filter;
    //endregion

    //region Injects
    @InjectView(R.id.train_list_tile_name_textview) TextView nameTextView;
    @InjectView(R.id.train_list_tile_wid_textview) TextView widTextView;
    @InjectView(R.id.train_list_tile_x_textview) TextView xTextView;
    @InjectView(R.id.train_list_tile_y_textview) TextView yTextView;
    //endregion

    public TrainListAdapter(Context context, int resource, List<Train> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.list = objects;
        this.originalList = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(this.resource, parent, false);
        }

        ButterKnife.inject(this, convertView);

        if(position < list.size()) {
            Train train = list.get(position);

            nameTextView.setText(train.getName());
            widTextView.setText(String.valueOf(train.getWid()));
            xTextView.setText("x: " + String.valueOf(train.getX()));
            yTextView.setText("y: " + String.valueOf(train.getY()));
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TrainListFilter();
        }

        return filter;
    }

    public void updateOriginalList(ArrayList<Train> trainList) {
        this.originalList.clear();
        this.originalList.addAll(trainList);
    }

    private class TrainListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String searchString = constraint.toString().toLowerCase();

            //If the user hasn't searched for anything, show the full list
            if(TextUtils.isEmpty(searchString)) {
                results.values = originalList;
                results.count = originalList.size();
            } else { //Else the filter the list according to the searched string
                ArrayList<Train> filteredList = new ArrayList<>();

                for (Train train : originalList) {
                    String lowerCaseName = train.getName().toLowerCase();

                    if (lowerCaseName.contains(searchString)) {
                        filteredList.add(train);
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<Train>)results.values;

            clear();

            for (Train train : list) {
                add(train);
            }
        }
    }
}
