package transitions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class TextFillTransition extends BaseFillTransition {

    public TextFillTransition(Duration duration,
                              Region region,
                              Color from,
                              Color to
    ) {
        super(duration, region, from, to);
    }

    public TextFillTransition(
            Duration duration,
            Color from,
            Color to
    ) {
        super(duration, (Region) null, from, to);
    }

    public TextFillTransition(Duration duration, Region region) {
        super(duration, region, (Paint) null, (Paint) null);
    }

    public TextFillTransition(Duration duration) {
        super(duration, (Region) null);
    }

    public TextFillTransition() {
        super(DEFAULT_DURATION, (Region) null);
    }

    @Override
    public ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>() {
                public void invalidated() {
                    try {
                        TextFillTransition.this.setCycleDuration(TextFillTransition.this.getDuration());
                    } catch (IllegalArgumentException e) {
                        if (this.isBound()) {
                            this.unbind();
                        }

                        this.set(TextFillTransition.this.getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return TextFillTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }

        return this.duration;
    }

    @Override
    protected void interpolate(double v) {
        Color afterShift = ((Color) this.getFromValue()).interpolate(
                (Color) this.getToValue(),
                v
        );

        ((Labeled) this.getRegion()).setTextFill(
                afterShift
        );
    }
}
