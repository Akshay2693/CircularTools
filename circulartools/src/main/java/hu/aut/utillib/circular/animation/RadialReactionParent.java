package hu.aut.utillib.circular.animation;

import android.view.View;

import java.util.List;

public interface RadialReactionParent extends CircularAnimator {

    void addListener(RadialReactionListener listener);

    void addAffectedViews(View view);

    void addAffectedViews(List<View> viewList);

    void setAction(String action);

    int getMaxRadius();
}
