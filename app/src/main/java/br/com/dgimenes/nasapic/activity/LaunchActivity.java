package br.com.dgimenes.nasapic.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import br.com.dgimenes.nasapic.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LaunchActivity extends ActionBarActivity {

    @Bind(R.id.sun_view)
    public TextView sunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ButterKnife.bind(this);
        configureAnimation();
    }

    private void configureAnimation() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(sunView, "alpha", 0f, 1f);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                LaunchActivity.this.startActivity(new Intent(LaunchActivity.this, PictureActivity.class));
                LaunchActivity.this.finish();
            }
        });
        animation.start();
    }
}
