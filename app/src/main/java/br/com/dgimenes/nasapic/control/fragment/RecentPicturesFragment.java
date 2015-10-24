package br.com.dgimenes.nasapic.control.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.adapter.APODListAdapter;
import br.com.dgimenes.nasapic.model.APOD;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentPicturesFragment extends Fragment implements APODListAdapter.ErrorListener {

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
            APOD apod1 = new APOD("http://apod.nasa.gov/apod/image/1510/IC2118_A_full900.jpg", "Double, double toil and trouble; Fire burn, and cauldron bubble .... maybe Macbeth should have consulted the Witch Head Nebula. A frighteningly shaped reflection nebula, this cosmic crone is about 800 light-years away though. Its malevolent visage seems to glare toward nearby bright star Rigel in Orion, just off the right edge of this frame. More formally known as IC 2118, the interstellar cloud of dust and gas is nearly 70 light-years across, its dust grains reflecting Rigel's starlight. In this composite portrait, the nebula's color is caused not only by the star's intense bluish light but because the dust grains scatter blue light more efficiently than red. The same physical process causes Earth's daytime sky to appear blue, although the scatterers in planet Earth's atmosphere are molecules of nitrogen and oxygen.", "The Witch Head Nebula", new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2015"));
            apods.add(apod1);
            apods.add(apod1);
            apods.add(apod1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recyclerViewAdapter = new APODListAdapter(getActivity(), apods, this, getDisplayWidth());
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

    @Override
    public void error(String errorMessage) {
        Snackbar.make(recyclerView, errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    public int getDisplayWidth() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
