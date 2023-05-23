package transitions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.paint.Paint;
import javafx.util.Duration;

/*
 This class is responsible for playing a
 transition that changes the border color
 smoothly over a certain duration.
 */
public class BorderFillTransition extends BaseFillTransition {

    // To maintain the regions border radius.
    private CornerRadii borderRadius;

    // To maintain the regions border insets.
    private Insets borderInsets;

    @Override
    public ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>() {
                public void invalidated() {
                    try {
                        BorderFillTransition.this.setCycleDuration(BorderFillTransition.this.getDuration());
                    } catch (IllegalArgumentException e) {
                        if (this.isBound()) {
                            this.unbind();
                        }

                        this.set(BorderFillTransition.this.getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return BorderFillTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }

        return this.duration;
    }

    public BorderFillTransition(Duration duration,
                                Region region,
                                Color from,
                                Color to
    ) {
        super(duration, region, from, to);
        this.borderRadius = region.getBackground()
                .getFills()
                .get(0)
                .getRadii();
        this.borderInsets = region.getBackground()
                .getFills()
                .get(0)
                .getInsets();
    }

    public BorderFillTransition(
            Duration duration,
            Color from,
            Color to
    ) {
        super(duration, (Region) null, from, to);
    }

    public BorderFillTransition(Duration duration, Region region) {
        super(duration, region, (Paint) null, (Paint) null);
    }

    public BorderFillTransition(Duration duration) {
        super(duration, (Region) null);
    }

    public BorderFillTransition() {
        super(BaseFillTransition.DEFAULT_DURATION, (Region) null);
    }

    @Override
    public void prepareTransition() {
        this.setFromValue(
                this.getRegion()
                        .getBorder()
                        .getStrokes()
                        .get(0)
                        .getTopStroke()
        );
    }

    @Override
    protected void interpolate(double v) {
        Color afterShift = ((Color) this.getFromValue()).interpolate(
                (Color) this.getToValue(), v
        );
        this.getRegion().setBorder(
                new Border(
                        new BorderStroke(
                                afterShift,
                                BorderStrokeStyle.SOLID,
                                this.borderRadius,
                                BorderWidths.DEFAULT,
                                this.borderInsets
                        )
                )
        );
    }
}
