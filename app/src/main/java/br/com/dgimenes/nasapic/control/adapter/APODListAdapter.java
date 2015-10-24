package br.com.dgimenes.nasapic.control.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.model.APOD;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import butterknife.Bind;
import butterknife.ButterKnife;

public class APODListAdapter extends RecyclerView.Adapter<APODListAdapter.ViewHolder> {

    private final Picasso picasso;
    private List<APOD> dataset;
    private WeakReference<Context> contextWeak;

    private static final String APOD_DATE_FORMAT = "dd/MMM/yyyy";
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(APOD_DATE_FORMAT);
    private APODListAdapter.ErrorListener errorListener;
    private int listWidth;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.date)
        public TextView dateTextView;

        @Bind(R.id.title)
        public TextView titleTextView;

        @Bind(R.id.explanation)
        public TextView explanationTextView;

        @Bind(R.id.apod_preview_image)
        public ImageView apodPreviewImageView;

        public ViewHolder(View cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public APODListAdapter(Context context, List<APOD> dataset, ErrorListener errorListener,
                           int listWidth) {
        this.dataset = dataset;
        this.errorListener = errorListener;
        this.listWidth = listWidth;
        this.picasso = DefaultPicasso.get(context, new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                displayErrorMessage(R.string.error_downloading_apod);
            }
        });
        this.contextWeak = new WeakReference<>(context);
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
        viewHolder.titleTextView.setText(limitString(apod.getTitle(), 28));
        viewHolder.explanationTextView.setText(addQuotes(limitString(apod.getExplanation(), 160)));
        viewHolder.apodPreviewImageView.setTag(apod.getUrl());
    }

    private String limitString(String text, int maxCharacters) {
        if (maxCharacters < text.length()) {
            return text.substring(0, maxCharacters - 3) + "...";
        } else {
            return text;
        }
    }

    private String addQuotes(String text) {
        return "\"" + text + "\"";
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        String src = (String) viewHolder.apodPreviewImageView.getTag();
        picasso.load(src).placeholder(R.drawable.loading).resize(listWidth, 0)
                .into(viewHolder.apodPreviewImageView);
    }

    private void displayErrorMessage(int errorMessageResource) {
        if (errorListener != null) {
            String errorMessage = contextWeak.get().getResources().getString(errorMessageResource);
            errorListener.error(errorMessage);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public interface ErrorListener {
        void error(String errorMessage);
    }
}
