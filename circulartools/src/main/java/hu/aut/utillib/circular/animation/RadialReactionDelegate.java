package hu.aut.utillib.circular.animation;

import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadialReactionDelegate {

    private boolean mAnimated;
    private int mCx;
    private int mCy;
    private float mRadius;
    private List<RadialReactionListener> mListeners;
    private List<View> mAffectedViews;
    private String mAction;

    public RadialReactionDelegate() {
        mListeners = new ArrayList<>();
        mAffectedViews = new ArrayList<>();
    }

    public void setAnimated(boolean isAnimated) {
        mAnimated = isAnimated;
    }

    public void setCenter(int cx, int cy) {
        mCx = cx;
        mCy = cy;
    }

    public float getRevealRadius() {
        return mRadius;
    }

    public void setRevealRadius(float value) {
        mRadius = value;

        for (Iterator iterator = mAffectedViews.iterator(); iterator.hasNext(); ) {

            View item = (View) iterator.next();
            int[] position = new int[2];
            item.getLocationOnScreen(position);

            //corner coordinates where tLx means Top Left X coordinate, etc.
            int tLx = position[0];
            int tLy = position[1];
            int tRx = tLx + item.getWidth();
            int tRy = tLy;
            int bLx = tLx;
            int bLy = tLy + item.getHeight();
            int bRx = bLx + item.getWidth();
            int bRy = bLy;

            if (mRadius >= CircularAnimationUtils.hypo(mCx - tLx, mCy - tLy) ||
                    mRadius >= CircularAnimationUtils.hypo(mCx - tRx, mCy - tRy) ||
                    mRadius >= CircularAnimationUtils.hypo(mCx - bLx, mCy - bLy) ||
                    mRadius >= CircularAnimationUtils.hypo(mCx - bRx, mCy - bRy)) {
                // view reached by circle
                for (RadialReactionListener listener : mListeners) {
                    listener.onRadialReaction(item, mAction);
                    iterator.remove();
                }
            }
        }
    }

    public void addListener(RadialReactionListener listener) {
        mListeners.add(listener);
    }

    public void addAffectedView(View view) {
        mAffectedViews.add(view);
    }

    public void addAffectedViews(List<View> viewList) {
        mAffectedViews.addAll(viewList);
    }

    public void setAction(String action) {
        mAction = action;
    }
}
