package hu.aut.utillib.circular.animation;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import java.lang.ref.WeakReference;

import static hu.aut.utillib.circular.animation.ViewAnimationUtils.SimpleAnimationListener;

/**
 * @hide
 */
public interface CircularAnimator {


    /**
     * Whether enable {@link android.graphics.Canvas} to clip
     * outlines of the certain or not
     *
     * @param clip Whether clip outlines or not
     *
     * @see #setCenter(int, int)
     * @see #setRevealRadius(float)
     * @see #setTarget(View)
     */
    void setClipOutlines(boolean clip);

    /**
     * Sets central points where to start clipping
     * certain child
     *
     * @param cx x point of child
     * @param cy y point of child
     *
     * @see #setClipOutlines(boolean) (float, float)
     * @see #setRevealRadius(float)
     * @see #setTarget(View)
     */
    void setCenter(int cx, int cy);

    /**
     * Reference the target of reveal animation
     *
     * @param target View to clip outlines
     */
    void setTarget(View target);

    /**
     * Used with animator to animate view clipping
     *
     * @param value clip radius
     */
    void setRevealRadius(float value);

    /**
     * Used with animator to animate view clipping
     *
     * @return current radius
     */
    float getRevealRadius();

    /**
     * Invalidate certain rectangle
     *
     * @param bounds bounds to redraw
     */
    void invalidate(Rect bounds);

    /**
     * Temporarily
     */
    void setupStartValues();

    /**
     * Keep this value for reverse animation
     *
     * @param start The start value of clip radius
     * @param end The end value
     */
    void setRadius(float start, float end);

    Rect getTargetBounds();


    //TODO WTF does this here?
    class RevealFinishedHoneycomb extends SimpleAnimationListener {
        WeakReference<CircularAnimator> mReference;
        volatile Rect mInvalidateBounds;

        RevealFinishedHoneycomb(CircularAnimator target, Rect bounds) {
            mReference = new WeakReference<>(target);
            mInvalidateBounds = bounds;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            CircularAnimator target = mReference.get();
            target.setupStartValues();
            target.invalidate(mInvalidateBounds);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class RevealFinishedIceCreamSandwich extends RevealFinishedHoneycomb {
        int mFeaturedLayerType;
        int mLayerType;

        RevealFinishedIceCreamSandwich(CircularAnimator target, Rect bounds) {
            super(target, bounds);

            mLayerType = ((View) target).getLayerType();
            mFeaturedLayerType = View.LAYER_TYPE_SOFTWARE;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            onAnimationEnd(animation);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            ((View) mReference.get()).setLayerType(mFeaturedLayerType, null);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ((View) mReference.get()).setLayerType(mLayerType, null);
            super.onAnimationEnd(animation);
        }
    }

    class RevealFinishedJellyBeanMr2 extends RevealFinishedIceCreamSandwich {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        RevealFinishedJellyBeanMr2(CircularAnimator target, Rect bounds) {
            super(target, bounds);

            mFeaturedLayerType = View.LAYER_TYPE_HARDWARE;
        }
    }

}