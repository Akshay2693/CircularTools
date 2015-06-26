package hu.aut.utillib.circular.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;

import java.lang.ref.WeakReference;

import hu.aut.utillib.circular.widget.CircularFrameLayout;

public class CircularAnimationUtils {

    public static final int AUTOMATIC = 0;
    public static final int MANUAL = 1;
    private static final String RADIUS = "Radius";

    /**
     * Creates a radial reaction animation in the container parent with the provided action
     *
     * @param parent The container view. It's radius property will be animated
     * @param origin The view which will be the origin of the event
     * @param action This helps to identify the event in that case when there are multiple use cases for radial reaction.
     *               In the RadialReactionListener.onRadialReaction() callback you'll get back this action to detect which event happened.
     * @return The radial reaction animation
     */
    public static ObjectAnimator createRadialReaction(RadialReactionParent parent, View origin, String action) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent can't be null!");
        } else if (origin == null) {
            throw new IllegalArgumentException("Origin can't be null!");
        }
        int[] position = new int[2];
        origin.getLocationOnScreen(position);

        int[] center = getCenter(origin, (View) parent);
        parent.setCenter(center[0], center[1]);
        parent.setAction(action);

        ObjectAnimator radialReaction = ObjectAnimator.ofFloat(parent, RADIUS, 0, parent.getMaxRadius());
        radialReaction.addListener(new RadialListener(parent));
        return radialReaction;
    }


    /**
     * Creates a radial reaction animation in the container parent with the provided action
     *
     * @param parent  The container view. It's radius property will be animated
     * @param centerX X coordinate of the circle's center
     * @param centerY Y coordinate of the circle's center
     * @param action  This helps to identify the event in that case when there are multiple use cases for radial reaction.
     *                In the RadialReactionListener.onRadialReaction() callback you'll get back this action to detect which event happened.
     * @return The radial reaction animation
     */
    public static ObjectAnimator createRadialReaction(RadialReactionParent parent, int centerX, int centerY, String action) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent can't be null!");
        }

        parent.setCenter(centerX, centerY);
        parent.setAction(action);

        ObjectAnimator radialReaction = ObjectAnimator.ofFloat(parent, RADIUS, 0, parent.getMaxRadius());
        radialReaction.addListener(new RadialListener(parent));
        return radialReaction;
    }

    /**
     * Returns an ObjectAnimator which can animate a clipping circle for a reveal effect.
     * The animated views visibility will change automatically.
     * See {@link CircularAnimationUtils#createCircularReveal(View, int, int, float, float, int)}
     *
     * @param view        The View will be clipped to the animating circle.
     * @param centerX     The x coordinate of the center of the animating circle.
     * @param centerY     The y coordinate of the center of the animating circle.
     * @param startRadius The starting radius of the animating circle.
     * @param endRadius   The ending radius of the animating circle.
     */
    public static ObjectAnimator createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius) {
        return createCircularTransform(view, null, centerX, centerY, startRadius, endRadius, AUTOMATIC);
    }

    /**
     * Returns an ObjectAnimator which can animate a clipping circle for a reveal effect.
     *
     * @param view        The View will be clipped to the animating circle.
     * @param centerX     The x coordinate of the center of the animating circle.
     * @param centerY     The y coordinate of the center of the animating circle.
     * @param startRadius The starting radius of the animating circle.
     * @param endRadius   The ending radius of the animating circle.
     * @param mode        The behavior of the animation. If set to {@link #AUTOMATIC}
     *                    the animated views visibility will change automatically,
     *                    otherwise these properties won't be touched.
     */
    public static ObjectAnimator createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius, int mode) {
        return createCircularTransform(view, null, centerX, centerY, startRadius, endRadius, mode);
    }

    /**
     * Returns an ObjectAnimator which can animate a clipping circle over two views in the same parent.
     *
     * @param target      The appearing View will be clipped to the animating circle.
     * @param source      The disappearing View will be inverse clipped to the animating circle.
     * @param centerX     The x coordinate of the center of the animating circle.
     * @param centerY     The y coordinate of the center of the animating circle.
     * @param startRadius The starting radius of the animating circle.
     * @param endRadius   The ending radius of the animating circle.
     */
    public static ObjectAnimator createCircularTransform(View target, View source, int centerX, int centerY, float startRadius, float endRadius) {
        return createCircularTransform(target, source, centerX, centerY, startRadius, endRadius, AUTOMATIC);
    }

    /**
     * Returns an ObjectAnimator which can animate a clipping circle over two views in the same parent.
     *
     * @param target      The appearing View will be clipped to the animating circle.
     * @param source      The disappearing View will be inverse clipped to the animating circle.
     * @param centerX     The x coordinate of the center of the animating circle.
     * @param centerY     The y coordinate of the center of the animating circle.
     * @param startRadius The starting radius of the animating circle.
     * @param endRadius   The ending radius of the animating circle.
     * @param mode        The behavior of the animation. If set to {@link #AUTOMATIC}
     *                    the animated views visibility will change automatically,
     *                    otherwise these properties won't be touched.
     */
    public static ObjectAnimator createCircularTransform(View target, View source, int centerX, int centerY, float startRadius, float endRadius, int mode) {

        if (target == null) {
            throw new IllegalArgumentException("Target can't be null!");
        } else if (source != null) {
            if (target.getParent() != source.getParent()) {
                throw new IllegalArgumentException("Target and source parent must be the same!");
            }
        }

        if (!(target.getParent() instanceof CircularAnimator)) {
            throw new IllegalArgumentException("View must be inside CircularFrameLayout");
        }

        CircularFrameLayout transformLayout = (CircularFrameLayout) target.getParent();
        transformLayout.setTarget(target);
        transformLayout.setAnimated(true);
        transformLayout.setSource(source);
        transformLayout.setCenter(centerX, centerY);

        ObjectAnimator transform = ObjectAnimator.ofFloat(transformLayout, RADIUS, startRadius, endRadius);

        if (source == null) {
            transform.addListener(new RevealListener(target, mode));
        } else {
            transform.addListener(new TransformListener(target, source, mode));
        }

        return transform;
    }


    /**
     * Computes the center of the clipping circle used by transform and reveal animations
     * The result is relative to the target.
     *
     * @param origin The Origin of the effect (eg. pressed view)
     * @param target Targeted view, that will be clipped
     * @return x and y coordinates in an array, in that order
     */
    public static int[] getCenter(View origin, View target) {

        //the top left corner of origin
        int[] originPosition = new int[2];
        origin.getLocationOnScreen(originPosition);

        //the center of origin
        originPosition[0] = originPosition[0] + origin.getWidth() / 2;
        originPosition[1] = originPosition[1] + origin.getHeight() / 2;

        // get the center for the clipping circle for the view
        int[] targetPosition = new int[2];
        target.getLocationOnScreen(targetPosition);

        int[] center = new int[2];

        center[0] = originPosition[0] - targetPosition[0];
        center[1] = originPosition[1] - targetPosition[1];

        return center;
    }

    /**
     * Returns the square root of the sum of squares of its arguments, see {@link Math#hypot(double, double)}
     */
    public static float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public static boolean isHWAsupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
    }

    static class RevealListener implements AnimatorListener {
        int originalLayerType;
        int mode;
        WeakReference<View> targetReference;
        WeakReference<CircularFrameLayout> parentReference;

        RevealListener(View target, int mode) {
            targetReference = new WeakReference<>(target);
            parentReference = new WeakReference<>((CircularFrameLayout) targetReference.get().getParent());
            originalLayerType = ((CircularFrameLayout) target.getParent()).getLayerType();
            this.mode = mode;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (!isHWAsupported()) {
                parentReference.get().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            if (mode == AUTOMATIC) {
                targetReference.get().setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (!isHWAsupported()) {
                parentReference.get().setLayerType(originalLayerType, null);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    static class TransformListener extends RevealListener {
        WeakReference<View> sourceReference;

        TransformListener(View target, View source, int mode) {
            super(target, mode);
            sourceReference = new WeakReference<>(source);
        }


        @Override
        public void onAnimationStart(Animator animation) {
            if (!isHWAsupported()) {
                parentReference.get().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            if (mode == AUTOMATIC) {
                targetReference.get().setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!isHWAsupported()) {
                parentReference.get().setLayerType(originalLayerType, null);
            }
            if (mode == AUTOMATIC) {
                sourceReference.get().setVisibility(View.INVISIBLE);
            }
        }
    }

    static class RadialListener implements AnimatorListener {
        WeakReference<RadialReactionParent> parentReference;

        RadialListener(RadialReactionParent parent) {
            parentReference = new WeakReference<>(parent);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            parentReference.get().setAnimated(true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            parentReference.get().setAnimated(false);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
