package hu.gordon.circulardemo.fragments;


import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.shamanland.fab.FloatingActionButton;

import hu.gordon.circulardemo.MainActivity;
import hu.gordon.circulardemo.R;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class RevealLinearFragment extends Fragment {

    public static final String TAG = RevealLinearFragment.class.getSimpleName();

    private int screenWidth;
    private int screenHeight;
    private FloatingActionButton fab;
    private CardView myView;

    public RevealLinearFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reveal_linear, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        if(android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }

        myView = (CardView) view.findViewById(R.id.card_right);

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

                if(myView.getVisibility() == View.VISIBLE){
                    return;
                }

                //
                // Pre-calculations
                //

                //the top left corner of the FAB
                int[] fabPosition = new int[2];
                fab.getLocationOnScreen(fabPosition);
                //Log.d("DEBUG", "FAB X: " + fabPosition[0] + " FAB Y: " + fabPosition[1]);

                //the center of the FAB
                fabPosition[0] = fabPosition[0] + fab.getWidth() / 2;
                fabPosition[1] = fabPosition[1] + fab.getHeight() / 2;
                Log.d("DEBUG", "FAB CX: " + fabPosition[0] + " FAB CY: " + fabPosition[1]);


                // get the final radius for the clipping circle
                float finalRadius = hypo(screenWidth, screenHeight);

                // get the center for the clipping circle for each view
                int[] myLeftViewPosition = new int[2];
                int[] myRightViewPosition = new int[2];

                ((View) myView.getParent()).getLocationOnScreen(myRightViewPosition);
                Log.d("DEBUG", "mVR X: " + myRightViewPosition[0] + " mRV Y: " + myRightViewPosition[1]);

                int rightcx = fabPosition[0] - myRightViewPosition[0];
                int rightcy = fabPosition[1] - myRightViewPosition[1];
                Log.d("DEBUG", "RCX: " + rightcx + " RCY: " + rightcy);

                SupportAnimator animator =
                        ViewAnimationUtils.createCircularReveal(myView, rightcx, rightcy, 0, finalRadius);

                animator.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                        Log.d(TAG, "animation start");
                    }

                    @Override
                    public void onAnimationEnd() {
                        Log.d(TAG, "animation end");
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });

                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1500);

                myView.setVisibility(View.VISIBLE);

                animator.start();
            }
        });
    }

    static float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }
}