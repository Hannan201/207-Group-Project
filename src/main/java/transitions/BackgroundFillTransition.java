package transitions;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import javafx.util.Duration;

public class BackgroundFillTransition extends Transition {

    private Color start;

    private Color end;

    private ObjectProperty<Region> region;

    private static final Region DEFAULT_REGION = null;

    private ObjectProperty<Duration> duration;

    private static final Duration DEFAULT_DURATION = Duration.millis(400);

    private ObjectProperty<Color> fromValue;

    private static final Color DEFAULT_FROM_VALUE = null;

    private ObjectProperty<Color> toValue;

    private static final Color DEFAULT_TO_VALUE = null;

    public final void setBackgroundFill(Region region) {
        if (this.region != null || region != null) {
            this.regionProperty().set(region);
        }
    }

    public final Region getRegion() {
        return this.region == null ? DEFAULT_REGION : (BackgroundFill) this.backgroundFill.get();
    }

    public final ObjectProperty<Region> regionProperty() {
        if (this.region == null) {
            this.region = new SimpleObjectProperty<>(this, "region", DEFAULT_REGION);
        }

        return this.region;
    }

    public final void setDuration(Duration duration) {
        if (this.duration != null && !DEFAULT_DURATION.equals(duration)) {
            this.durationProperty().set(duration);
        }
    }

    public final Duration getDuration() {
        return this.duration == null ? DEFAULT_DURATION : (Duration) this.duration.get();
    }

    public final ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>() {
                public void invalidated() {
                    try {
                        BackgroundFillTransition.this.setCycleDuration(BackgroundFillTransition.this.getDuration());
                    } catch (IllegalArgumentException e) {
                        if (this.isBound()) {
                            this.unbind();
                        }

                        this.set(BackgroundFillTransition.this.getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return BackgroundFillTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }

        return this.duration;
    }

    public final void setFromValue(Color color) {
        if (this.fromValue != null || color != null) {
            this.fromValueProperty().set(color);
        }
    }

    public final Color getFromValue() {
        return this.fromValue == null ? DEFAULT_FROM_VALUE : (Color)this.fromValue.get();
    }

    public final ObjectProperty<Color> fromValueProperty() {
        if (this.fromValue == null) {
            this.fromValue = new SimpleObjectProperty<>(this, "fromValue", DEFAULT_FROM_VALUE);
        }

        return this.fromValue;
    }

    public final void setToValue(Color color) {
        if (this.toValue != null || color != null) {
            this.toValueProperty().set(color);
        }
    }

    public final Color getToValue() {
        return this.toValue == null ? DEFAULT_TO_VALUE : (Color)this.toValue.get();
    }

    public final ObjectProperty<Color> toValueProperty() {
        if (this.toValue == null) {
            this.toValue = new SimpleObjectProperty<>(this, "toValue", DEFAULT_TO_VALUE);
        }

        return this.toValue;
    }

    public BackgroundFillTransition(Duration duration,
                                    BackgroundFill backgroundFill,
                                    Color from,
                                    Color to
                          ) {
        this.setDuration(duration);
        this.setBackgroundFill(backgroundFill);
        this.setFromValue(from);
        this.setToValue(to);
        this.setCycleDuration(duration);
    }

    public BackgroundFillTransition(
            Duration duration,
            Color from,
            Color to
    ) {
        this(duration, (BackgroundFill) null, from, to);
    }

    public BackgroundFillTransition(Duration duration, BackgroundFill backgroundFill) {
        this(duration, backgroundFill, (Color) null, (Color) null);
    }

    public BackgroundFillTransition(Duration duration) {
        this(duration, (BackgroundFill) null);
    }

    public BackgroundFillTransition() {
        this(DEFAULT_DURATION, (BackgroundFill) null);
    }

    @Override
    protected void interpolate(double v) {
        Color afterShift = this.start.interpolate(
                this.end, v
        );
    }
}
