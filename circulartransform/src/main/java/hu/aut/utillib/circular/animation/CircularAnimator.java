package hu.aut.utillib.circular.animation;

import android.view.View;


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
     * Reference the target of circular animation to appear
     *
     * @param target View to clip outlines
     */
    void setTarget(View target);


    /**
     * Reference the target of circular animation to disappear
     *
     * @param target View to clip outlines
     */
    void setSource(View target);

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

}