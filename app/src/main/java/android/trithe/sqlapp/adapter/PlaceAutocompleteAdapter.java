package android.trithe.sqlapp.adapter;

/**
 * Created by admin on 15/1/2018.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.trithe.sqlapp.R;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaceAutocompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final String TAG = PlaceAutocompleteAdapter.class.getSimpleName();

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

    private ArrayList<AutocompletePrediction> mResultList;

    private GeoDataClient mGeoDataClient;

    private LatLngBounds mBounds;

    private AutocompleteFilter mPlaceFilter;

    public PlaceAutocompleteAdapter(Context context, GeoDataClient geoDataClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, R.layout.item_autocomplete, R.id.place_name);
        mGeoDataClient = geoDataClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    @Override
    public int getCount() {
        if (mResultList != null) {
            return mResultList.size();
        } else {
            return 0;
        }
    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        AutocompletePrediction item = getItem(position);
        TextView textView1 = row.findViewById(R.id.place_name);
        TextView textView2 = row.findViewById(R.id.place_detail);
        textView1.setText(item.getPrimaryText(STYLE_BOLD));
        textView2.setText(item.getSecondaryText(STYLE_BOLD));
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<AutocompletePrediction> filterData = new ArrayList<>();
                if (constraint != null) {
                    filterData = getAutocomplete(constraint);
                }

                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }
                Log.d("abc", results.toString());
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count >= 0) {
                    try {
                        mResultList = (ArrayList<AutocompletePrediction>) results.values;
                    } catch (Exception e) {
                        mResultList = new ArrayList<>();
                    }
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        Task<AutocompletePredictionBufferResponse> results =
                mGeoDataClient.getAutocompletePredictions(constraint.toString(), mBounds,
                        mPlaceFilter);

        try {
            Tasks.await(results, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        try {
            AutocompletePredictionBufferResponse autocompletePredictions = results.getResult();
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        } catch (RuntimeExecutionException e) {
            return null;
        }
    }

    class MyFilterAsyncTask extends AsyncTask<Void, Void, ArrayList<AutocompletePrediction>> {
        private Context context;
        private CharSequence text;

        public MyFilterAsyncTask(Context context, CharSequence charSequence) {
            this.context = context;
            this.text = charSequence;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<AutocompletePrediction> doInBackground(Void... voids) {
            ArrayList<AutocompletePrediction> filterData = new ArrayList<>();
            if (text != null) {
                filterData = getAutocomplete(text);
            }
            Log.d(TAG, "Speech voice Filter finish: " + System.currentTimeMillis());
            return filterData;
        }

        @Override
        protected void onPostExecute(ArrayList<AutocompletePrediction> autocompletePredictions) {
            super.onPostExecute(autocompletePredictions);
            if (autocompletePredictions != null && autocompletePredictions.size() > 0) {
                mResultList = autocompletePredictions;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void myFilter(CharSequence text) {
        MyFilterAsyncTask myFilterAsyncTask = new MyFilterAsyncTask(getContext(), text);
        myFilterAsyncTask.execute();
    }
}