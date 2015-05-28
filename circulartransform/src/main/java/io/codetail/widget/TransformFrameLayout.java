package io.codetail.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.TransformAnimator;
import io.codetail.animation.TransformViewAnimationUtils;

public class TransformFrameLayout extends FrameLayout implements TransformAnimator {

    public static final String TAG = TransformFrameLayout.class.getSimpleName();

    Path mRevealPath;

    boolean mClipOutlines;

    int mCenterX;
    int mCenterY;
    float mRadius;

    View mTarget;
    View mSource;

    float mStartRadius;
    float mEndRadius;

    final Rect mTargetBounds = new Rect();


    public TransformFrameLayout(Context context) {
        this(context, null);
    }

    public TransformFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRevealPath = new Path();
    }

    /**
     * Animation target to appear
     *
     */
    @Override
    public void setTarget(View view) {
        mTarget = view;
    }

    /**
     * Animation target to disappear
     *
     */
    @Override
    public void setSource(View view) {
        mSource = view;
    }

    /**
     * Epicenter of animation circle reveal
     *
     */
    @Override
    public void setCenter(int centerX, int centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    /**
     * Flag that animation is enabled
     *
     */
    @Override
    public void setClipOutlines(boolean clip) {
        mClipOutlines = clip;
    }

    /**
     * Circle radius size
     *
     */
    @Override
    public void setRevealRadius(float radius) {
        mRadius = Math.max(1F, radius);
        invalidate();
    }

    /**
     * Circle radius size
     *
     */
    @Override
    public float getRevealRadius() {
        return mRadius;
    }

    @Override
    public void setupStartValues() {
        mClipOutlines = false;
        mRadius = 1F;
    }

    @Override
    public void setRadius(float start, float end) {
        mStartRadius = start;
        mEndRadius = end;
    }

    @Override
    public Rect getTargetBounds() {
        return mTargetBounds;
    }

    @Override
    public SupportAnimator startReverseAnimation() {
        return TransformViewAnimationUtils.createCircularTransform(mTarget, mSource, mCenterX, mCenterY,
                mEndRadius, mStartRadius);
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
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
