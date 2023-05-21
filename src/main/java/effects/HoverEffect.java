package effects;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import themes.ThemeState;
import transitions.*;

/*
This class is responsible for displaying a hover effect on the border,
text, or background fill when the mouse enters the area of the component
(region).
 */
public class HoverEffect {

    // Used to play effects when mouse hovers.
    private final ParallelTransition hoverTransitions;

    // Used to play the effects when mouse exits.
    private final ParallelTransition exitTransitions;

    // The component (region) to display the effect on.
    private final Region component;

    // Duration of this effect.
    private final Duration baseDuration;


    /**
     * Creat a hover effect for a component that will play for a certain
     * amount of time.
     *
     * @param duration Duration of the effect.
     * @param region The region to apply the effect on.
     */
    public HoverEffect(Duration duration, Region region) {
        this.hoverTransitions = new ParallelTransition();
        this.exitTransitions = new ParallelTransition();
        this.baseDuration = duration;
        this.component = region;
    }

    /**
     * Set the hover effect of the background color of the component
     * to start and end at specific colors.
     *
     * @param color The state which will give starting and ending
     *              color of the effect.
     */
    public void setBackgroundColorEffect(ThemeState<Color> color) {
        // On hover.
        BaseFillTransition hover = new BackgroundFillTransition(
                this.baseDuration,
                this.component
        );

        // On exit.
        BaseFillTransition exit = new BackgroundFillTransition(
                this.baseDuration,
                this.component
        );

        addTransitions(hover, exit, color);
    }

    /**
     * Set the hover effect of the background linear gradient of the component
     * to start and end at specific colors.
     *
     * @param linearGradient The state which will give starting and ending
     *                       linear gradients of the effect.
     */
    public void setBackgroundLinearGradientEffect(
            ThemeState<LinearGradient> linearGradient
    ) {
        // On hover.
        BaseFillTransition hover = new LinearGradientFillTransition(
                this.baseDuration,
                this.component
        );

        // On exit.
        BaseFillTransition exit = new LinearGradientFillTransition(
                this.baseDuration,
                this.component
        );

        addTransitions(hover, exit, linearGradient);
    }

    /**
     * Set the hover effect of the border of the component
     * to start and end at specific colors.
     *
     * @param border The state which will give starting and ending
     *               border of the effect.
     */
    public void setBorderEffect(ThemeState<Color> border) {
        // On hover.
        BaseFillTransition hover = new BorderFillTransition(
                this.baseDuration,
                this.component
        );

        // On exit.
        BaseFillTransition exit = new BorderFillTransition(
                this.baseDuration,
                this.component
        );

        addTransitions(hover, exit, border);
    }

    /**
     * Set the hover effect of the text fill of the component
     * to start and end at specific colors.
     *
     * @param text The state which will give starting and ending
     *             text of the effect.
     */
    public void setTextEffect(ThemeState<Color> text) {
        // On hover.
        BaseFillTransition hover = new TextFillTransition(
                this.baseDuration,
                this.component
        );

        // On exit.
        BaseFillTransition exit = new TextFillTransition(
                this.baseDuration,
                this.component
        );

        addTransitions(hover, exit, text);
    }

    /**
     * Bind the starting and ending colors of any type of effect
     * (fill colour, linear gradient, border, or text) so all effects
     * can be played in parallel.
     *
     * @param hover Transition to control what happens when mouse is hovered.
     * @param exit Transition to control what happens when mouse exits.
     * @param state State controlling the starting and ending colors of
     *              the transition.
     */
    private void addTransitions(
            BaseFillTransition hover,
            BaseFillTransition exit,
            ThemeState<? extends Paint> state
    ) {
        hover.toValueProperty().bind(state.endProperty());
        exit.toValueProperty().bind(state.startProperty());
        this.hoverTransitions.getChildren().add(hover);
        this.exitTransitions.getChildren().add(exit);
    }

    /**
     * Adjust all the transitions to start at the current region's color
     * (background, linear gradient, border or text) before playing the
     * transition (which could either be the hover or exit).
     *
     * @param transition All the transitions to be played.
     */
    private void prepareTransitions(ParallelTransition transition) {
        for (Animation animation : transition.getChildren()) {
            ((BaseFillTransition) animation).prepareTransition();
        }
    }


    /**
     * Play the effect when hovered.
     */
    public void playOnHover() {
        prepareTransitions(this.hoverTransitions);
        hoverTransitions.play();
    }

    /*
    Play the effect when exited.
     */
    public void playOnExit() {
        prepareTransitions(this.exitTransitions);
        exitTransitions.play();
    }

}
