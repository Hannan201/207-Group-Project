package cypher.enforcers.views.interfaces;

import cypher.enforcers.views.View;

/**
 * This interface is responsible for allowing
 * an application to retrieve and set the
 * previous view to display. Enables the
 * application to go back to a specific
 * view.
 */
public interface Reversible {

    /**
     * Get the previous view which this
     * application was displaying.
     *
     * @return The previous view for this
     * application.
     */
    View getPreviousView();

    /**
     * Set the previous view for this application
     * to display.
     *
     * @param newPreviousView The new previous
     *                        view for this
     *                        application.
     */
    void setPreviousView(View newPreviousView);

}