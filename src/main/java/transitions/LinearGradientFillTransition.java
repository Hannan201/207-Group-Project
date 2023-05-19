package transitions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.*;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LinearGradientFillTransition extends BaseFillTransition {

    @Override
    public final ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>() {
                public void invalidated() {
                    try {
                        LinearGradientFillTransition.this.setCycleDuration(LinearGradientFillTransition.this.getDuration());
                    } catch (IllegalArgumentException e) {
                        if (this.isBound()) {
                            this.unbind();
                        }

                        this.set(LinearGradientFillTransition.this.getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return LinearGradientFillTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }

        return this.duration;
    }

    public LinearGradientFillTransition(Duration duration,
                                    Region region,
                                    LinearGradient from,
                                    LinearGradient to
    ) {
        super(duration, region, from, to);
    }

    public LinearGradientFillTransition(
            Duration duration,
            LinearGradient from,
            LinearGradient to
    ) {
        super(duration, (Region) null, from, to);
    }

    public LinearGradientFillTransition(Duration duration, Region region) {
        super(duration, region, (Paint) null, (Paint) null);
    }

    public LinearGradientFillTransition(Duration duration) {
        this(duration, (Region) null);
    }

    public LinearGradientFillTransition() {
        this(DEFAULT_DURATION, (Region) null);
    }

    @Override
    protected void interpolate(double v) {
        List<Stop> fromValueStops = ((LinearGradient) this.getFromValue()).getStops();
        List<Stop> toValueStops = ((LinearGradient) this.getToValue()).getStops();

        List<Stop> newStops = new ArrayList<>();
        int length = ((LinearGradient) this.getFromValue()).getStops().size();
        for (int i = 0; i < length; i++) {
            Color afterShift = fromValueStops.get(i)
                    .getColor()
                    .interpolate(
                            toValueStops.get(i)
                                    .getColor(),
                            v
                    );
            newStops.add(
                    new Stop(
                            fromValueStops.get(i)
                                    .getOffset(),
                            afterShift
                    )
            );
        }

        LinearGradient linearGradient = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                newStops
        );

        this.getRegion().setBackground(
                new Background(
                        new BackgroundFill(
                                linearGradient,
                                new CornerRadii(4),
                                Insets.EMPTY
                        )
                )
        );
    }
}
