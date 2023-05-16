package themes;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public enum ThemeConfiguration {

    LIGHT,
    DARK,
    HIGH_CONTRAST;

    static {
        Background primaryBackgroundDefault = new Background(
                new BackgroundFill(
                        new LinearGradient(
                                0,
                                0,
                                0,
                                1,
                                true,
                                CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#d7d7d7")),
                                new Stop(1, Color.web("#d7d7d7"))
                        ),
                        new CornerRadii(4),
                        Insets.EMPTY
                )
        );

        Background primaryBackgroundHover = new Background(
                new BackgroundFill(
                        new LinearGradient(
                                0,
                                0,
                                0,
                                1,
                                true,
                                CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#d4e1f9")),
                                new Stop(1, Color.web("#7ea6e9"))
                        ),
                        new CornerRadii(4),
                        Insets.EMPTY
                )
        );

        Background secondaryBackgroundDefault = new Background(
                new BackgroundFill(
                        Color.web("#b6d7a8"),
                        new CornerRadii(4),
                        Insets.EMPTY
                )
        );

        Background secondaryBackgroundHover = new Background(
                new BackgroundFill(
                        Color.web("#aecca1"),
                        new CornerRadii(4),
                        Insets.EMPTY
                )
        );

        Border primaryBorderDefault = new Border(
                new BorderStroke(
                        Color.TRANSPARENT,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(4),
                        BorderWidths.DEFAULT
                )
        );

        Border primaryBorderHover = new Border(
                new BorderStroke(
                        Color.WHITE,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(4),
                        BorderWidths.DEFAULT
                )
        );

        ThemeState<Background> backgroundOne = new ThemeState<>(
                primaryBackgroundDefault,
                primaryBackgroundHover
        );

        ThemeState<Background> backgroundTwo = new ThemeState<>(
                secondaryBackgroundDefault,
                secondaryBackgroundHover
        );

        ThemeState<Border> borderOne = new ThemeState<>(
                primaryBorderDefault,
                primaryBorderHover
        );

        values()[0].setConfiguration(
                backgroundOne,
                backgroundTwo,
                borderOne,
                new ThemeState<>()
        );
    }

    private ThemeState<Background> primaryBackground;
    private ThemeState<Background> secondaryBackground;
    private ThemeState<Border> primaryBorder;
    private ThemeState<Border> secondaryBorder;

    private void setConfiguration(
            ThemeState<Background> primaryBackground,
            ThemeState<Background> secondaryBackground,
            ThemeState<Border> primaryBorder,
            ThemeState<Border> secondaryBorder
    ) {
        this.primaryBackground = primaryBackground;
        this.secondaryBackground = secondaryBackground;
        this.primaryBorder = primaryBorder;
        this.secondaryBorder = secondaryBorder;
    }

    public ThemeState<Background> getPrimaryBackground() {
        return primaryBackground;
    }

    public Paint getPrimaryBackgroundStartFill() {
        return this.getBackgroundFill(
                this.primaryBackground.getStart()
        );
    }

    public Paint getPrimaryBackgroundEndFill() {
        return this.getBackgroundFill(
                this.primaryBackground.getEnd()
        );
    }

    public ThemeState<Background> getSecondaryBackground() {
        return secondaryBackground;
    }

    public Paint getSecondaryBackgroundStartFill() {
        return this.getBackgroundFill(
                this.secondaryBackground.getStart()
        );
    }

    public Paint getSecondaryBackgroundEndFill() {
        return this.getBackgroundFill(
                this.secondaryBackground.getEnd()
        );
    }

    public ThemeState<Border> getPrimaryBorder() {
        return this.primaryBorder;
    }

    public Paint getPrimaryBorderStartFill() {
        return this.getBorderFill(
                this.primaryBorder.getStart()
        );
    }

    public Paint getPrimaryBorderEndFill() {
        return this.getBorderFill(
                this.primaryBorder.getEnd()
        );
    }

    public ThemeState<Border> getSecondaryBorder() {
        return this.secondaryBorder;
    }


    public Paint getSecondaryBorderStartFill() {
        return this.getBorderFill(
                this.secondaryBorder.getStart()
        );
    }

    public Paint getSecondaryBorderEndFill() {
        return this.getBorderFill(
                this.secondaryBorder.getEnd()
        );
    }

    private Paint getBackgroundFill(Background background) {
        return background.getFills()
                .get(0)
                .getFill();
    }

    private Paint getBorderFill(Border border) {
        return border.getStrokes()
                .get(0)
                .getTopStroke();
    }
}
