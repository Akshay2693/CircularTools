package hu.aut.utillib.circular.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import hu.aut.utillib.circular.widget.TransformFrameLayout;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class ViewAnimationUtils {

    private static final String CLIP_RADIUS = "RevealRadius";

    private final static boolean LOLLIPOP_PLUS = SDK_INT >= LOLLIPOP;

    /**
     * Returns an Animator which can animate a clipping circle.
     * <p/>
     * Any shadow cast by the View will respect the circular clip from this animator.
     * <p/>
     * Only a single non-rectangular clip can be applied on a View at any time.
     * Views clipped by a circular reveal animation take priority over
     * {@link View#setClipToOutline(boolean) View Outline clipping}.
     * <p/>
     * Note that the animation returned here is a one-shot animation. It cannot
     * be re-used, and once started it cannot be paused or resumed.
     *
     * @param view        The View will be clipped to the animating circle.
     * @param centerX     The x coordinate of the center of the animating circle.
     * @param centerY     The y coordinate of the center of the animating circle.
     * @param startRadius The starting radius of the animating circle.
     * @param endRadius   The ending radius of the animating circle.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularReveal(View view,
                                                int centerX, int centerY,
                                                float startRadius, float endRadius) {

        if (!(view.getParent() instanceof CircularAnimator)) {
            throw new IllegalArgumentException("View must be inside RevealFrameLayout or RevealLinearLayout.");
        }

        CircularAnimator revealLayout = (CircularAnimator) view.getParent();
        revealLayout.setTarget(view);
        revealLayout.setClipOutlines(true);
        revealLayout.setCenter(centerX, centerY);
        revealLayout.setRadius(startRadius, endRadius);

        if (LOLLIPOP_PLUS) {
            return android.view.ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        }

        ObjectAnimator reveal = ObjectAnimator.ofFloat(revealLayout, CLIP_RADIUS, startRadius, endRadius);
        reveal.addListener(getRevealFinishListener(revealLayout, revealLayout.getTargetBounds()));

        return reveal;
    }

    /**
     * Returns an Animator which can animate a clipping circle over two views in the same parent.
     * <p/>
     * Any shadow cast by the View will respect the circular clip from this animator.
     * <p/>
     * Only a single non-rectangular clip can be applied on a View at any time.
     * Views clipped by a circular reveal animation take priority over
     * {@link View#setClipToOutline(boolean) View Outline clipping}.
     * <p/>
     * Note that the animation returned here is a one-shot animation. It cannot
     * be re-used, and once started it cannot be paused or resumed.
     *
     * @param target      The appearing View will be clipped to the animating circle.
     * @param source      The disappearing View will be inverse clipped to the animating circle.
     * @param centerX     The x coordinate of the center of the animating circle.
     * @param centerY     The y coordinate of the center of the animating circle.
     * @param startRadius The starting radius of the animating circle.
     * @param endRadius   The ending radius of the animating circle.
     */
    public static Animator createCircularTransform(View target, View source, int centerX, int centerY, float startRadius, float endRadius) {

        //TODO check for nulls!

        if (target.getParent() != source.getParent()) {
            throw new IllegalArgumentException("Target and source parent must be the same!");
        }

        if (!(target.getParent() instanceof TransformAnimator)) {
            throw new IllegalArgumentException("View must be inside TransformFrameLayout");
        }

        TransformAnimator transformLayout = (TransformAnimator) target.getParent();
        transformLayout.setTarget(target);
        transformLayout.setClipOutlines(true);
        transformLayout.setSource(source);
        transformLayout.setCenter(centerX, centerY);
        transformLayout.setRadius(startRadius, endRadius);

        Rect bounds = new Rect();
        target.getHitRect(bounds);

        ObjectAnimator transform = ObjectAnimator.ofFloat(transformLayout, CLIP_RADIUS, startRadius, endRadius);

        transform.addListener(getRevealFinishListener(transformLayout, bounds));

        return transform;
    }


    //TODO javadoc
    public static int[] getCenter(View origin, View target) {

        //the top left corner of origin
        int[] originPosition = new int[2];
        origin.getLocationOnScreen(originPosition);

        //the center of origin
        originPosition[0] = originPosition[0] + origin.getWidth() / 2;
        originPosition[1] = originPosition[1] + origin.getHeight() / 2;

        // get the center for the clipping circle for the view
        int[] targetPosition = new int[2];
        TransformFrameLayout parent = ((TransformFrameLayout) target.getParent());
        parent.getLocationOnScreen(targetPosition);

        int[] center = new int[2];

        center[0] = originPosition[0] - targetPosition[0];
        center[1] = originPosition[1] - targetPosition[1];

        return center;
    }

    //TODO javadoc
    public static float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }


    //TODO javadoc
    static AnimatorListener getRevealFinishListener(CircularAnimator target, Rect bounds) {
        if (SDK_INT >= JELLY_BEAN_MR2) {
            return new CircularAnimator.RevealFinishedJellyBeanMr2(target, bounds);
        } else if (SDK_INT >= ICE_CREAM_SANDWICH) {
            return new CircularAnimator.RevealFinishedIceCreamSandwich(target, bounds);
        } else {
            return new CircularAnimator.RevealFinishedHoneycomb(target, bounds);
        }
    }


    static class SimpleAnimationListener implements AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
