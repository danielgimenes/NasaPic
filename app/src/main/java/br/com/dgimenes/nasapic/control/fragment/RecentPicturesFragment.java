package br.com.dgimenes.nasapic.control.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.adapter.APODListAdapter;
import br.com.dgimenes.nasapic.model.APOD;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentPicturesFragment extends Fragment {

    @Bind(R.id.recent_pics_recycler_view)
    RecyclerView recyclerView;

    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_recent_tab, container, false);
        ButterKnife.bind(this, rootView);
        setupUI();
        return rootView;
    }

    private void setupUI() {
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);


        List<APOD> apods = new ArrayList<>();
        try {
            apods.add(new APOD("url 1", "explanation 1", "title 1", new SimpleDateFormat("dd/MM/yyyy").parse("22/10/2015")));
            apods.add(new APOD("url 2", "explanation 2", "title 2", new SimpleDateFormat("dd/MM/yyyy").parse("21/10/2015")));
            apods.add(new APOD("url 3", "explanation 3", "title 3", new SimpleDateFormat("dd/MM/yyyy").parse("20/10/2015")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recyclerViewAdapter = new APODListAdapter(apods);
        recyclerView.setAdapter(recyclerViewAdapter);

//        setWallpaperButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        loadingDialog.show();
//                        String pictureUrl = apodPagerAdapter.getFragment(apodPager.getCurrentItem())
//                                .getPictureUrl();
//                        apodInteractor.setWallpaper(pictureUrl, new OnFinishListener<Void>() {
//                            @Override
//                            public void onSuccess(Void none) {
//                                displayToastMessage(getString(R.string.wallpaper_set));
//                                loadingDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onError(Throwable throwable) {
//                                loadingDialog.dismiss();
//                                displayToastMessage(
//                                        getString(R.string.error_setting_wallpaper));
//                            }
//                        });
//                    }
//                }
//        );
    }
}
