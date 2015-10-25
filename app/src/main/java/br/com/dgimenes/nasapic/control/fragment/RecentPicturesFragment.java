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
            APOD apod1 = new APOD.Builder()
                    .url("http://apod.nasa.gov/apod/image/1510/IC2118_A_full900.jpg")
                    .explanation("Double, double toil and trouble; Fire burn, and cauldron bubble .... maybe Macbeth should have consulted the Witch Head Nebula. A frighteningly shaped reflection nebula, this cosmic crone is about 800 light-years away though. Its malevolent visage seems to glare toward nearby bright star Rigel in Orion, just off the right edge of this frame. More formally known as IC 2118, the interstellar cloud of dust and gas is nearly 70 light-years across, its dust grains reflecting Rigel's starlight. In this composite portrait, the nebula's color is caused not only by the star's intense bluish light but because the dust grains scatter blue light more efficiently than red. The same physical process causes Earth's daytime sky to appear blue, although the scatterers in planet Earth's atmosphere are molecules of nitrogen and oxygen.")
                    .title("The Witch Head Nebula")
                    .date(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2015"))
                    .build();
            apods.add(apod1);

            APOD apod2 = new APOD.Builder()
                    .url("http://apod.nasa.gov/apod/image/1510/Charon-Neutral-Bright-Release1024c.jpg")
                    .explanation("A darkened and mysterious north polar region informally known as Mordor Macula caps this premier high-resolution portrait of Charon, Pluto's largest moon. Captured by New Horizons near its closest approach on July 14, the image data was transmitted to Earth on September 21. The combined blue, red, and infrared data is processed to enhance colors, following variations in surface properties with a resolution of about 2.9 kilometers (1.8 miles). In fact, Charon is 1,214 kilometers (754 miles) across, about 1/10th the size of planet Earth but a whopping 1/2 the diameter of Pluto itself. That makes it the largest satellite relative to its planet in the solar system. This remarkable image of Charon's Pluto-facing hemisphere shows a clearer view of an apparently moon-girdling belt of fractures and canyons that seems to separate smooth southern plains from varied northern terrain.")
                    .title("Charon: Moon of Pluto")
                    .date(new SimpleDateFormat("dd/MM/yyyy").parse("02/10/2015"))
                    .build();
            apods.add(apod2);

            APOD apod3 = new APOD.Builder()
                    .url("http://apod.nasa.gov/apod/image/1509/nh-apluto-mountains-plains-9-17-15.jpg")
                    .explanation("This shadowy landscape of majestic mountains and icy plains stretches toward the horizon of a small, distant world. It was captured from a range of about 18,000 kilometers when New Horizons looked back toward Pluto, 15 minutes after the spacecraft's closest approach on July 14. The dramatic, low-angle, near-twilight scene follows rugged mountains still popularly known as Norgay Montes from foreground left, and Hillary Montes along the horizon, giving way to smooth Sputnik Planum at right. Layers of Pluto's tenuous atmosphere are also revealed in the backlit view. With a strangely familiar appearance, the frigid terrain likely includes ices of nitrogen and carbon monoxide with water-ice mountains rising up to 3,500 meters (11,000 feet). That's comparable in height to the majestic mountains of planet Earth. This Plutonian landscape is 380 kilometers (230 miles) across.")
                    .title("A Plutonian Landscape")
                    .date(new SimpleDateFormat("dd/MM/yyyy").parse("18/09/2015"))
                    .build();
            apods.add(apod3);
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
