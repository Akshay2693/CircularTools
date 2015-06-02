package hu.gordon.circulardemo.fragments;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import hu.aut.utillib.circular.animation.ViewAnimationUtils;
import hu.gordon.circulardemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransformFragment extends Fragment implements Animator.AnimatorListener {

    public static final String TAG = TransformFragment.class.getSimpleName();

    private int screenWidth;
    private int screenHeight;
    private FloatingActionButton fab;
    private ImageView imageView1, imageView2;
    private boolean state = true;
    private boolean animationInProgress = false;
    private ObjectAnimator animator;

    private ImageView mySourceView;
    private ImageView myTargetView;

    public TransformFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_transform, container, false);

        imageView1 = (ImageView) root.findViewById(R.id.iv1);
        imageView2 = (ImageView) root.findViewById(R.id.iv2);

        if (state) {
            imageView2.setVisibility(View.INVISIBLE);
        } else {
            imageView1.setVisibility(View.INVISIBLE);
        }
        return root;
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


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (animationInProgress) {
                    animator.cancel();
                    return;
                }

                if (state) {
                    mySourceView = imageView1;
                    myTargetView = imageView2;
                } else {
                    mySourceView = imageView2;
                    myTargetView = imageView1;
                }

                //
                // Pre-calculations
                //
                // get the final radius for the clipping circle
                float finalRadius = ViewAnimationUtils.hypo(screenWidth, screenHeight);
                int[] center = ViewAnimationUtils.getCenter(fab, myTargetView);

                animator =
                        ViewAnimationUtils.createCircularTransform(myTargetView, mySourceView, center[0], center[1], 0F, finalRadius);

                animator.addListener(TransformFragment.this);

                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1500);

                animator.start();
                state = !state;
            }
        });
    }

    @Override
    public void onAnimationStart(Animator animator) {
        Log.d(TAG, "animation start");
        animationInProgress = true;
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        Log.d(TAG, "animation end");
        animationInProgress = false;
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        Log.d(TAG, "animation cancel");
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
