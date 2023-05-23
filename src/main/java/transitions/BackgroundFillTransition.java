package transitions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class BackgroundFillTransition extends BaseFillTransition {

    // To maintain the background fill radius.
    private CornerRadii backgroundRadius;

    // To maintain the background fill insets.
    private Insets backgroundInsets;

    @Override
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

    public BackgroundFillTransition(Duration duration,
                                    Region region,
                                    Color from,
                                    Color to
                          ) {
        super(duration, region, from, to);
    }

    public BackgroundFillTransition(
            Duration duration,
            Color from,
            Color to
    ) {
        super(duration, (Region) null, from, to);
    }

    public BackgroundFillTransition(Duration duration, Region region) {
        super(duration, region, (Paint) null, (Paint) null);
        this.backgroundRadius = region.getBackground()
                .getFills()
                .get(0)
                .getRadii();
        this.backgroundInsets = region.getBackground()
                .getFills()
                .get(0)
                .getInsets();
    }

    public BackgroundFillTransition(Duration duration) {
        this(duration, (Region) null);
    }

    public BackgroundFillTransition() {
        this(DEFAULT_DURATION, (Region) null);
    }

    @Override
    public void prepareTransition() {
        this.setFromValue(
                this.getRegion()
                        .getBackground()
                        .getFills()
                        .get(0)
                        .getFill()
        );
    }

    @Override
    protected void interpolate(double v) {
        Color afterShift = ((Color) this.getFromValue()).interpolate(
                (Color) this.getToValue(), v
        );
        this.getRegion().setBackground(
                new Background(
                        new BackgroundFill(
                                afterShift,
                                this.backgroundRadius,
                                this.backgroundInsets
                        )
                )
        );
    }
}
