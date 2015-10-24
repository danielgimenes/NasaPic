package br.com.dgimenes.nasapic.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.model.APOD;
import butterknife.Bind;
import butterknife.ButterKnife;

public class APODListAdapter extends RecyclerView.Adapter<APODListAdapter.ViewHolder> {

    private List<APOD> dataset;

    private static final String APOD_DATE_FORMAT = "dd/MMM/yyyy";
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(APOD_DATE_FORMAT);

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.date)
        public TextView dateTextView;

        @Bind(R.id.title)
        public TextView titleTextView;

        @Bind(R.id.url)
        public TextView urlTextView;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

    public APODListAdapter(List<APOD> dataset) {
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View apodCardRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apod_card,
                parent, false);
        return new ViewHolder(apodCardRootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        APOD apod = dataset.get(position);
        viewHolder.dateTextView.setText(dateFormatter.format(apod.getDate()));
        viewHolder.titleTextView.setText(apod.getTitle());
        viewHolder.urlTextView.setText(apod.getUrl());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }



}
