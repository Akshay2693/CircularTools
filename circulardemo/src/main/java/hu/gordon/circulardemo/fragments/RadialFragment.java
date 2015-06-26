package hu.gordon.circulardemo.fragments;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;

import hu.aut.utillib.circular.animation.CircularAnimationUtils;
import hu.aut.utillib.circular.animation.RadialReactionListener;
import hu.aut.utillib.circular.animation.RadialReactionParent;
import hu.gordon.circulardemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadialFragment extends Fragment implements RadialReactionListener, Animator.AnimatorListener {

    static final String RADIAL_DEMO_ACTION = "fab_action";

    RadialReactionParent reactionParent;
    boolean animationInProgress = false;
    boolean isAppearing = true;
    ArrayList<View> childList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radial, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reactionParent = (RadialReactionParent) view.findViewById(R.id.reaction_parent);
        reactionParent.addListener(this);

        childList = new ArrayList<>();

        childList.add(view.findViewById(R.id.child_1));
        childList.add(view.findViewById(R.id.child_2));
        childList.add(view.findViewById(R.id.child_3));
        childList.add(view.findViewById(R.id.child_4));
        childList.add(view.findViewById(R.id.child_5));
        childList.add(view.findViewById(R.id.child_6));
        childList.add(view.findViewById(R.id.child_7));
        childList.add(view.findViewById(R.id.child_8));
        childList.add(view.findViewById(R.id.child_9));

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animationInProgress) {
                    reactionParent.addAffectedViews(childList);
                    ObjectAnimator animator = CircularAnimationUtils.createRadialReaction(reactionParent, fab, RADIAL_DEMO_ACTION);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(1500);
                    animator.addListener(RadialFragment.this);
                    animator.start();
                }
            }
        });

    }

    @Override
    public void onRadialReaction(final View affectedView, String action) {
        if (action == RADIAL_DEMO_ACTION) {
            if (isAppearing) {
                Animation fadeIn = new AlphaAnimation(0.0F, 1.0F);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(300);

                Animation scale = new ScaleAnimation(0.5F, 1.0F, 0.5F, 1.0F, 0.5F, 0.5F);
                scale.setInterpolator(new AccelerateDecelerateInterpolator());
                scale.setDuration(300);

                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(fadeIn);
                animationSet.addAnimation(scale);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        affectedView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                affectedView.startAnimation(animationSet);
            } else {
                Animation fadeOut = new AlphaAnimation(1.0F, 0.0F);
                fadeOut.setInterpolator(new DecelerateInterpolator());
                fadeOut.setDuration(300);

                Animation scale = new ScaleAnimation(1.0F, 0.5F, 1.0F, 0.5F, 0.5F, 0.5F);
                scale.setInterpolator(new AccelerateDecelerateInterpolator());
                scale.setDuration(300);

                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(fadeOut);
                animationSet.addAnimation(scale);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        affectedView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                affectedView.startAnimation(animationSet);
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        animationInProgress = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        animationInProgress = false;
        isAppearing = !isAppearing;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
