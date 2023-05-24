package themes;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.*;

public enum ThemeConfiguration {

    LIGHT,
    DARK,
    HIGH_CONTRAST;

    static {
        //
        // Light Mode Configurations.
        //
        LinearGradient primaryBackgroundDefault =
                new LinearGradient(
                    0,
                    0,
                    0,
                    1,
                    true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#d7d7d7")),
                    new Stop(1, Color.web("#d7d7d7"))
                );
        LinearGradient primaryBackgroundHover =
                new LinearGradient(
                        0,
                        0,
                        0,
                        1,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#d4e1f9")),
                        new Stop(1, Color.web("#7ea6e9"))
                );

        Color secondaryBackgroundDefault = Color.web("#b6d7a8");
        Color secondaryBackgroundHover = Color.web("#aecca1");

        Color tertiaryBackgroundDefault = Color.web("#d9d9d9");
        Color tertiaryBackgroundHover = Color.rgb(207, 207, 207);

        Color primaryTextDefault = Color.BLACK;
        Color primaryTextHover = Color.BLACK;

        Color secondaryTextDefault = Color.BLACK;
        Color secondaryTextHover = Color.BLACK;

        Color tertiaryTextDefault = Color.BLACK;
        Color tertiaryTextHover = Color.BLACK;

        Color primaryBorderDefault = Color.TRANSPARENT;
        Color primaryBorderHover = Color.WHITE;

        Color secondaryBorderDefault = Color.TRANSPARENT;
        Color secondaryBorderHover = Color.TRANSPARENT;

        Color tertiaryBorderDefault = Color.TRANSPARENT;
        Color tertiaryBorderHover = Color.web("#235480");

        ThemeState<LinearGradient> backgroundOne =
                new ThemeState<>(
                    primaryBackgroundDefault,
                    primaryBackgroundHover
                );

        ThemeState<Color> backgroundTwo = new ThemeState<>(
                secondaryBackgroundDefault,
                secondaryBackgroundHover
        );

        ThemeState<Color> backgroundThree = new ThemeState<>(
                tertiaryBackgroundDefault,
                tertiaryBackgroundHover
        );

        ThemeState<Color> textOne = new ThemeState<>(
                primaryTextDefault,
                primaryTextHover
        );

        ThemeState<Color> textTwo = new ThemeState<>(
                secondaryTextDefault,
                secondaryTextHover
        );

        ThemeState<Color> textThree = new ThemeState<>(
                tertiaryTextDefault,
                tertiaryTextHover
        );

        ThemeState<Color> borderOne = new ThemeState<>(
                primaryBorderDefault,
                primaryBorderHover
        );

        ThemeState<Color> borderTwo = new ThemeState<>(
                secondaryBorderDefault,
                secondaryBorderHover
        );

        ThemeState<Color> borderThree = new ThemeState<>(
                tertiaryBorderDefault,
                tertiaryBorderHover
        );

        values()[0].setConfiguration(
                backgroundOne,
                backgroundTwo,
                backgroundThree,
                textOne,
                textTwo,
                textThree,
                borderOne,
                borderTwo,
                borderThree
        );

        //
        // Dark Mode Configurations.
        //

        primaryBackgroundDefault = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#161618")),
                new Stop(1, Color.web("#161618"))
        );
        primaryBackgroundHover = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#20262e")),
                new Stop(1, Color.web("#163b79"))
        );

        secondaryBackgroundDefault = Color.web("#3e5728");
        secondaryBackgroundHover = Color.web("#354a22");

        tertiaryBackgroundDefault = Color.web("#2d3133");
        tertiaryBackgroundHover = Color.rgb(45, 49, 51, 0.09);

        primaryTextDefault = primaryTextHover = Color.web(
                "#babcc0"
        );

        secondaryTextDefault = secondaryTextHover = Color.web(
                "#c9cbc1"
        );

        tertiaryTextHover = tertiaryTextDefault = Color.web(
                "#adadad"
        );

        primaryBorderDefault = Color.TRANSPARENT;
        primaryBorderHover = Color.WHITE;

        secondaryBorderDefault = Color.TRANSPARENT;
        secondaryBorderHover = Color.TRANSPARENT;

        tertiaryBorderDefault = Color.TRANSPARENT;
        tertiaryBorderHover = Color.rgb(173, 173, 173);

        backgroundOne =
                new ThemeState<>(
                        primaryBackgroundDefault,
                        primaryBackgroundHover
                );

        backgroundTwo = new ThemeState<>(
                secondaryBackgroundDefault,
                secondaryBackgroundHover
        );

        backgroundThree = new ThemeState<>(
                tertiaryBackgroundDefault,
                tertiaryBackgroundHover
        );

        textOne = new ThemeState<>(
                primaryTextDefault,
                primaryTextHover
        );

        textTwo = new ThemeState<>(
                secondaryTextDefault,
                secondaryTextHover
        );

        textThree = new ThemeState<>(
                tertiaryTextDefault,
                tertiaryTextHover
        );

        borderOne = new ThemeState<>(
                primaryBorderDefault,
                primaryBorderHover
        );

        borderTwo = new ThemeState<>(
                secondaryBorderDefault,
                secondaryBorderHover
        );

        borderThree = new ThemeState<>(
                tertiaryBorderDefault,
                tertiaryBorderHover
        );

        values()[1].setConfiguration(
                backgroundOne,
                backgroundTwo,
                backgroundThree,
                textOne,
                textTwo,
                textThree,
                borderOne,
                borderTwo,
                borderThree
        );

        //
        // High Contrast mode configurations.
        //

        primaryBackgroundDefault = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(1, Color.TRANSPARENT)
        );
        primaryBackgroundHover = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(1, Color.TRANSPARENT)
        );

        secondaryBackgroundDefault = Color.TRANSPARENT;
        secondaryBackgroundHover = Color.TRANSPARENT;

        tertiaryBackgroundDefault = Color.TRANSPARENT;
        tertiaryBackgroundHover = Color.rgb(0, 255, 255, 0.09);

        primaryTextDefault = Color.web("#ff7600");
        primaryTextHover = Color.web("#00ffff");

        secondaryTextDefault = Color.web("#ff7600");
        secondaryTextHover = Color.web("#00ffff");

        tertiaryTextDefault = Color.web("#ff7600");
        tertiaryTextHover = Color.web("#00ffff");

        primaryBorderDefault = Color.web("#ff7600");
        primaryBorderHover = Color.web("#00ffff");

        secondaryBorderDefault = Color.web("#ff7600");
        secondaryBorderHover = Color.web("#00ffff");

        tertiaryBorderDefault = Color.web("#ff7600");
        tertiaryBorderHover = Color.web("#00ffff");

        backgroundOne = new ThemeState<>(
                        primaryBackgroundDefault,
                        primaryBackgroundHover
        );

        backgroundTwo = new ThemeState<>(
                secondaryBackgroundDefault,
                secondaryBackgroundHover
        );

        backgroundThree = new ThemeState<>(
                tertiaryBackgroundDefault,
                tertiaryBackgroundHover
        );

        textOne = new ThemeState<>(
                primaryTextDefault,
                primaryTextHover
        );

        textTwo = new ThemeState<>(
                secondaryTextDefault,
                secondaryTextHover
        );

        textThree = new ThemeState<>(
                tertiaryTextDefault,
                tertiaryTextHover
        );

        borderOne = new ThemeState<>(
                primaryBorderDefault,
                primaryBorderHover
        );

        borderTwo = new ThemeState<>(
                secondaryBorderDefault,
                secondaryBorderHover
        );

        borderThree = new ThemeState<>(
                tertiaryBorderDefault,
                tertiaryBorderHover
        );

        values()[2].setConfiguration(
                backgroundOne,
                backgroundTwo,
                backgroundThree,
                textOne,
                textTwo,
                textThree,
                borderOne,
                borderTwo,
                borderThree
        );
    }

    private ThemeState<LinearGradient> primaryBackground;
    private ThemeState<Color> secondaryBackground;
    private ThemeState<Color> tertiaryBackground;

    private ThemeState<Color> primaryText;
    private ThemeState<Color> secondaryText;
    private ThemeState<Color> tertiaryText;

    private ThemeState<Color> primaryBorder;
    private ThemeState<Color> secondaryBorder;
    private ThemeState<Color> tertiaryBorder;

    private void setConfiguration(
            ThemeState<LinearGradient> primaryBackground,
            ThemeState<Color> secondaryBackground,
            ThemeState<Color> tertiaryBackground,
            ThemeState<Color> primaryText,
            ThemeState<Color> secondaryText,
            ThemeState<Color> tertiaryText,
            ThemeState<Color> primaryBorder,
            ThemeState<Color> secondaryBorder,
            ThemeState<Color> tertiaryBorder
    ) {
        this.primaryBackground = primaryBackground;
        this.secondaryBackground = secondaryBackground;
        this.tertiaryBackground = tertiaryBackground;

        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.tertiaryText = tertiaryText;

        this.primaryBorder = primaryBorder;
        this.secondaryBorder = secondaryBorder;
        this.tertiaryBorder = tertiaryBorder;
    }

    public ThemeState<LinearGradient> getPrimaryBackground() {
        return primaryBackground;
    }

    public ObjectProperty<LinearGradient> primaryBackgroundStartProperty() {
        return this.primaryBackground.startProperty();
    }

    public ObjectProperty<LinearGradient> primaryBackgroundEndProperty() {
        return this.primaryBackground.endProperty();
    }

    public ThemeState<Color> getSecondaryBackground() {
        return secondaryBackground;
    }

    public ObjectProperty<Color> secondaryBackgroundStartProperty() {
        return this.secondaryBackground.startProperty();
    }

    public ObjectProperty<Color> secondaryBackgroundEndProperty() {
        return this.secondaryBackground.endProperty();
    }

    public ThemeState<Color> getTertiaryBackground() {
        return tertiaryBackground;
    }

    public ObjectProperty<Color> tertiaryBackgroundStartProperty() {
        return this.tertiaryBackground.startProperty();
    }

    public ObjectProperty<Color> tertiaryBackgroundEndProperty() {
        return this.tertiaryBackground.endProperty();
    }

    public ThemeState<Color> getPrimaryText() {
        return this.primaryText;
    }

    public ObjectProperty<Color> primaryTextStartProperty() {
        return this.primaryText.startProperty();
    }

    public ObjectProperty<Color> primaryTextEndProperty() {
        return this.primaryText.endProperty();
    }

    public ThemeState<Color> getSecondaryText() {
        return this.secondaryText;
    }

    public ObjectProperty<Color> secondaryTextStartProperty() {
        return this.secondaryText.startProperty();
    }

    public ObjectProperty<Color> secondaryTextEndProperty() {
        return this.secondaryText.endProperty();
    }

    public ThemeState<Color> getTertiaryText() {
        return this.tertiaryText;
    }

    public ObjectProperty<Color> tertiaryTextStartProperty() {
        return this.tertiaryText.startProperty();
    }

    public ObjectProperty<Color> tertiaryTextEndProperty() {
        return this.tertiaryText.endProperty();
    }

    public ThemeState<Color> getPrimaryBorder() {
        return this.primaryBorder;
    }

    public ObjectProperty<Color> primaryBorderStartProperty() {
        return this.primaryBorder.startProperty();
    }

    public ObjectProperty<Color> primaryBorderEndProperty() {
        return this.primaryBorder.endProperty();
    }

    public ThemeState<Color> getSecondaryBorder() {
        return this.secondaryBorder;
    }


    public ObjectProperty<Color> secondaryBorderStartProperty() {
        return this.secondaryBorder.startProperty();
    }

    public ObjectProperty<Color> secondaryBorderEndProperty() {
        return this.secondaryBorder.endProperty();
    }

    public ThemeState<Color> getTertiaryBorder() {
        return this.tertiaryBorder;
    }

    public ObjectProperty<Color> tertiaryBorderStartProperty() {
        return this.tertiaryBorder.startProperty();
    }

    public ObjectProperty<Color> tertiaryBorderEndProperty() {
        return this.tertiaryBorder.endProperty();
    }


    public void update(ThemeConfiguration newConfiguration) {
        primaryBackground.setStart(
                newConfiguration.getPrimaryBackground().getStart()
        );
        primaryBackground.setEnd(
                newConfiguration.getPrimaryBackground().getEnd()
        );
        secondaryBackground.setStart(
                newConfiguration.getSecondaryBackground()
                        .getStart()
        );
        secondaryBackground.setEnd(
                newConfiguration.getSecondaryBackground()
                        .getEnd()
        );
        tertiaryBackground.setStart(
                newConfiguration.getTertiaryBackground()
                        .getStart()
        );
        tertiaryBackground.setEnd(
                newConfiguration.getTertiaryBackground()
                        .getEnd()
        );

        primaryBorder.setStart(
                newConfiguration.getPrimaryBorder()
                        .getStart()
        );
        primaryBorder.setEnd(
                newConfiguration.getPrimaryBorder()
                        .getEnd()
        );
        secondaryBorder.setStart(
                newConfiguration.getSecondaryBorder()
                        .getStart()
        );
        secondaryBorder.setEnd(
                newConfiguration.getSecondaryBorder()
                        .getEnd()
        );
        tertiaryBorder.setStart(
                newConfiguration.getTertiaryBorder()
                        .getStart()
        );
        tertiaryBorder.setEnd(
                newConfiguration.getTertiaryBorder()
                        .getEnd()
        );

        primaryText.setStart(
                newConfiguration.getPrimaryText().getStart()
        );
        primaryText.setEnd(
                newConfiguration.getPrimaryText().getEnd()
        );
        secondaryText.setStart(
                newConfiguration.getSecondaryText().getStart()
        );
        secondaryText.setEnd(
                newConfiguration.getSecondaryText().getEnd()
        );
        tertiaryText.setStart(
                newConfiguration.getTertiaryText().getStart()
        );
        tertiaryText.setEnd(
                newConfiguration.getTertiaryText().getEnd()
        );
    }
}
