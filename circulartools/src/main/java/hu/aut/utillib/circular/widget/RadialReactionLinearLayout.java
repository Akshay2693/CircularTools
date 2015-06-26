package hu.aut.utillib.circular.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import hu.aut.utillib.circular.animation.RadialReactionDelegate;
import hu.aut.utillib.circular.animation.RadialReactionListener;
import hu.aut.utillib.circular.animation.RadialReactionParent;

public class RadialReactionLinearLayout extends LinearLayout implements RadialReactionParent {

    private RadialReactionDelegate mDelegate = new RadialReactionDelegate();

    public RadialReactionLinearLayout(Context context) {
        super(context);
    }

    public RadialReactionLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadialReactionLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadialReactionLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setAnimated(boolean isAnimated) {
        mDelegate.setAnimated(isAnimated);
    }

    @Override
    public void setCenter(int cx, int cy) {
        mDelegate.setCenter(cx, cy);
    }

    @Override
    public float getRadius() {
        return mDelegate.getRevealRadius();
    }

    @Override
    public void setRadius(float value) {
        mDelegate.setRevealRadius(value);
    }

    @Override
    public void addListener(RadialReactionListener listener) {
        mDelegate.addListener(listener);
    }

    @Override
    public void addAffectedViews(View view) {
        mDelegate.addAffectedView(view);
    }

    @Override
    public void addAffectedViews(List<View> viewList) {
        mDelegate.addAffectedViews(viewList);
    }

    @Override
    public void setAction(String action) {
        mDelegate.setAction(action);
    }

    @Override
    public int getMaxRadius() {
        return Math.max(getWidth(), getHeight());
    }
}
