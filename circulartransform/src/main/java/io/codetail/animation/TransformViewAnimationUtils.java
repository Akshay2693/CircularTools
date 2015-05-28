package io.codetail.animation;

import android.graphics.Rect;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

import io.codetail.widget.TransformFrameLayout;

import static io.codetail.animation.RevealAnimator.CLIP_RADIUS;

public class TransformViewAnimationUtils extends ViewAnimationUtils {

    public static SupportAnimator createCircularTransform(View target, View source, int centerX, int centerY, float startRadius, float endRadius) {
        if (target.getParent() != source.getParent()){
            throw new IllegalArgumentException("Target and source parent must be the same!");
        }

        if (!(target.getParent() instanceof TransformAnimator)){
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

        return new SupportAnimatorPreL(transform, transformLayout);
    }

    public static int[] getCenter(View origin, View target){

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

    public static float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

}
