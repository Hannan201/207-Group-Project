package transitions;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;

import javafx.scene.paint.Paint;
import javafx.util.Duration;

public abstract class BaseFillTransition extends Transition {

    private ObjectProperty<Region> region;

    private static final Region DEFAULT_REGION = null;

    protected ObjectProperty<Duration> duration;

    protected static final Duration DEFAULT_DURATION = Duration.millis(400);

    private ObjectProperty<Paint> fromValue;

    private static final Paint DEFAULT_FROM_VALUE = null;

    private ObjectProperty<Paint> toValue;

    private static final Paint DEFAULT_TO_VALUE = null;

    public final void setRegion(Region region) {
        if (this.region != null || region != null) {
            this.regionProperty().set(region);
        }
    }

    public final Region getRegion() {
        return this.region == null ? DEFAULT_REGION : (Region) this.region.get();
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

    public abstract ObjectProperty<Duration> durationProperty();

    protected void configureDuration(BaseFillTransition transition) {

    }

    public final void setFromValue(Paint paint) {
        if (this.fromValue != null || paint != null) {
            this.fromValueProperty().set(paint);
        }
    }

    public final Paint getFromValue() {
        return this.fromValue == null ? DEFAULT_FROM_VALUE : (Paint)this.fromValue.get();
    }

    public final ObjectProperty<Paint> fromValueProperty() {
        if (this.fromValue == null) {
            this.fromValue = new SimpleObjectProperty<>(this, "fromValue", DEFAULT_FROM_VALUE);
        }

        return this.fromValue;
    }

    public final void setToValue(Paint paint) {
        if (this.toValue != null || paint != null) {
            this.toValueProperty().set(paint);
        }
    }

    public final Paint getToValue() {
        return this.toValue == null ? DEFAULT_TO_VALUE : (Paint)this.toValue.get();
    }

    public final ObjectProperty<Paint> toValueProperty() {
        if (this.toValue == null) {
            this.toValue = new SimpleObjectProperty<>(this, "toValue", DEFAULT_TO_VALUE);
        }

        return this.toValue;
    }

    public BaseFillTransition(Duration duration,
                              Region region,
                              Paint from,
                              Paint to
    ) {
        this.setDuration(duration);
        this.setRegion(region);
        this.setFromValue(from);
        this.setToValue(to);
        this.setCycleDuration(duration);
    }

    public BaseFillTransition(
            Duration duration,
            Paint from,
            Paint to
    ) {
        this(duration, (Region) null, from, to);
    }

    public BaseFillTransition(Duration duration, Region region) {
        this(duration, region, (Paint) null, (Paint) null);
    }

    public BaseFillTransition(Duration duration) {
        this(duration, (Region) null);
    }

    public BaseFillTransition() {
        this(DEFAULT_DURATION, (Region) null);
    }
}
