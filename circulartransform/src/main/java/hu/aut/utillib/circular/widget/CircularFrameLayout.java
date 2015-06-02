package hu.aut.utillib.circular.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import hu.aut.utillib.circular.animation.CircularAnimator;


public class CircularFrameLayout extends FrameLayout implements CircularAnimator {

    Path mRevealPath;

    boolean mClipOutlines;

    int mCenterX;
    int mCenterY;
    float mRadius;

    View mTarget;
    View mSource;


    public CircularFrameLayout(Context context) {
        this(context, null);
    }

    public CircularFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRevealPath = new Path();
    }

    /**
     * Animation target to appear
     */
    @Override
    public void setTarget(View view) {
        mTarget = view;
    }

    /**
     * Animation target to disappear
     */
    @Override
    public void setSource(View view) {
        mSource = view;
    }

    /**
     * Epicenter of animation circle reveal
     */
    @Override
    public void setCenter(int centerX, int centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    /**
     * Flag that animation is enabled
     */
    @Override
    public void setClipOutlines(boolean clip) {
        mClipOutlines = clip;
    }

    /**
     * Sets the clipping circle radius size. Radius won't be smaller than 1F
     */
    @Override
    public void setRevealRadius(float radius) {
        mRadius = Math.max(1F, radius);
        invalidate();
    }

    /**
     * Returns the clipping circle radius size
     */
    @Override
    public float getRevealRadius() {
        return mRadius;
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        if (mClipOutlines && (child == mTarget || child == mSource)) {

            final int state = canvas.save();

            mRevealPath.reset();
            mRevealPath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);

            if (child == mTarget) {
                //appearing
                mRevealPath.setFillType(Path.FillType.EVEN_ODD);
            } else {
                //disappearing
                mRevealPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            }

            canvas.clipPath(mRevealPath);

            boolean isInvalided = super.drawChild(canvas, child, drawingTime);

            canvas.restoreToCount(state);

            return isInvalided;
        }

        return super.drawChild(canvas, child, drawingTime);
    }

}
