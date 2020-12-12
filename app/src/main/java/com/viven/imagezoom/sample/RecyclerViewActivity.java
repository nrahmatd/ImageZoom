package com.viven.imagezoom.sample;

import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.viven.imagezoom.ImageZoomHelper;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    ImageZoomHelper imageZoomHelper;
    ImageZoomHelper.OnZoomListener onZoomListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL,
                false));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                String url = "https://media.giphy.com/media/3oz8xRF0v9WMAUVLNK/giphy.gif";

                // Wrap ImageView with FrameLayout to avoid RecyclerView issue
                FrameLayout frameLayout = new FrameLayout(parent.getContext());
                ImageView imageView = new ImageView(RecyclerViewActivity.this);
                imageView.setTag(R.id.imageUrl, url);
                imageView.setImageResource(R.mipmap.ic_launcher);
                ImageZoomHelper.setViewZoomable(imageView);
                imageView.setMinimumHeight(400);
                frameLayout.addView(imageView);
                Glide.with(RecyclerViewActivity.this)
                        .load(url)
                        .asGif()
                        .centerCrop()
                        .into(imageView);
                return new RecyclerView.ViewHolder(frameLayout) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ImageZoomHelper.setZoom(swipeRefreshLayout, false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        ImageZoomHelper.setZoom(swipeRefreshLayout, true);
                    }
                }, 2000);
            }
        });

        imageZoomHelper = new ImageZoomHelper(this);

        onZoomListener = new ImageZoomHelper.OnZoomListener() {
            @Override
            public void onImageZoomStarted(final View view) {

            }

            @Override
            public void onImageZoomEnded(View view) {

            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return imageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageZoomHelper.addOnZoomListener(onZoomListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        imageZoomHelper.removeOnZoomListener(onZoomListener);
    }
}
