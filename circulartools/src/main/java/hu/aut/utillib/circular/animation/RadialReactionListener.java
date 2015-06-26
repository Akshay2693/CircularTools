package hu.aut.utillib.circular.animation;

import android.view.View;

public interface RadialReactionListener {

    /**
     * This callback is called from a RadialReactionParent, when the radius of radial action reached
     * a view
     *
     * @param affectedView The reached view
     * @param action       A previously given identifier for event
     */
    void onRadialReaction(View affectedView, String action);
}
