package controllers;

import effects.HoverEffect;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.util.Duration;
import themes.ThemeState;

/*
Class containing utility methods used among the
controllers.
 */
public class Utilities {

    /**
     * Create a hover effect with the starting and ending colors
     * of a region that will play once hovered and exited over a duration.
     *
     * @param duration Duration of the effect.
     * @param node Region to apply the effect to.
     * @param background State which holds the starting and ending values
     *                   for the background color.
     * @param border State which holds the starting and ending values
     *               for the border color.
     * @param text State which holds the starting and ending values
     *             for the text color/
     * @return hover effect which can be played when the component is
     * hovered/exited to adjust the colors of the background, border, and
     * text.
     */
    public HoverEffect makeHoverBackgroundColorEffect(
            Duration duration,
            Region node,
            ThemeState<Color> background,
            ThemeState<Color> border,
            ThemeState<Color> text
    ) {
        HoverEffect effect = new HoverEffect(duration, node);
        effect.setBackgroundColorEffect(background);
        effect.setBorderEffect(border);
        effect.setTextEffect(text);
        return effect;
    }

    /**
     * Create a hover effect with the starting and ending colors
     * of a region that will play once hovered and exited over a duration.
     *
     * @param duration Duration of the effect.
     * @param node Region to apply the effect to.
     * @param background State which holds the starting and ending values
     *                   for the background linear-gradient.
     * @param border State which holds the starting and ending values
     *               for the border color.
     * @param text State which holds the starting and ending values
     *             for the text color/
     * @return hover effect which can be played when the component is
     * hovered/exited to adjust the linear gradient of the background,
     * and the colors of the border, and text.
     */
    public HoverEffect makeHoverBackgroundLinearGradientEffect(
            Duration duration,
            Region node,
            ThemeState<LinearGradient> background,
            ThemeState<Color> border,
            ThemeState<Color> text
    ) {
        HoverEffect effect = new HoverEffect(duration, node);
        effect.setBackgroundLinearGradientEffect(background);
        effect.setBorderEffect(border);
        effect.setTextEffect(text);
        return effect;
    }

}
