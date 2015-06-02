package hu.gordon.circulardemo.fragments;


import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import hu.aut.utillib.circular.animation.ViewAnimationUtils;
import hu.gordon.circulardemo.R;

public class RevealFragment extends Fragment {

    public static final String TAG = RevealFragment.class.getSimpleName();

    private int screenWidth;
    private int screenHeight;
    private FloatingActionButton fab;
    private CardView myView;

    public RevealFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reveal, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }

        myView = (CardView) view.findViewById(R.id.card);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myView.setVisibility(View.INVISIBLE);
            }
        });
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myView.getVisibility() == View.VISIBLE) {
                    return;
                }

                //
                // Pre-calculations
                //
                // get the final radius for the clipping circle
                float finalRadius = ViewAnimationUtils.hypo(screenWidth, screenHeight);
                int[] center = ViewAnimationUtils.getCenter(fab, myView);

                ObjectAnimator animator =
                        ViewAnimationUtils.createCircularReveal(myView, center[0], center[1], 0, finalRadius);

                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1500);
                animator.start();

            }
        });
    }

}
