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
    public static HoverEffect makeHoverBackgroundColorEffect(
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
    public static HoverEffect makeHoverBackgroundLinearGradientEffect(
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

    /**
     * Set a effect where the start and end colours of the border and
     * background of a region are provided over a duration.
     *
     * @param duration Duration of the effect.
     * @param textField Region to apply the effect to.
     * @param border State to control the start and end effect of the
     *               border.
     * @param background State to control the start and end effect of
     *                   the background.
     */
    public static void setFocusBorderEffect(
            Duration duration,
            Region textField,
            ThemeState<Color> border,
            ThemeState<Color> background
    ) {
        HoverEffect effect = new HoverEffect(duration, textField);
        effect.setBorderEffect(border);
        effect.setBackgroundColorEffect(background);
        textField.focusedProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if (!oldValue && newValue) {
                        effect.playOnHover();
                    } else if (oldValue && !newValue) {
                        effect.playOnExit();
                    }
                });

        textField.setOnMouseEntered(mouseEvent -> {
            if (!textField.isFocused()) {
                effect.playOnHover();
            }
        });

        textField.setOnMouseExited(mouseEvent -> {
            if (!textField.isFocused()) {
                effect.playOnExit();
            }
        });
    }
}
