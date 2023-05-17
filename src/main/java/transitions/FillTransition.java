package transitions;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class FillTransition extends Transition {

    private Color start;

    private Color end;

    private ObjectProperty<Node> node;

    private static final Node DEFAULT_NODE = null;

    private Node cachedNode;

    private ObjectProperty<Duration> duration;

    private static final Duration DEFAULT_DURATION = Duration.ofMillis(400);

    private ObjectProperty<Color> fromValue;

    private static final Color DEFAULT_FROM_VALUE = null;

    private ObjectProperty<Color> toValue;

    private static final Color DEFAULT_TO_VALUE = null;

    public final void setNode(Node node) {
        if (this.node != null || node != null) {
            this.nodeProperty().set(node);
        }
    }

    public final Node getNode() {
        return this.node == null ? DEFAULT_NODE : (Node) this.node.get();
    }

    public final ObjectProperty<Node> nodeProperty() {
        if (this.node == null) {
            this.node = new SimpleObjectProperty<>(this, "node", DEFAULT_NODE);
        }

        return this.node;
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
                        FillTransition.this.setCycleDuration(FillTransition.this.getDuration());
                    } catch (IllegalArgumentException e) {
                        if (this.isBound()) {
                            this.unbind();
                        }

                        this.set(FillTransition.this.getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return FillTransition.this;
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
        if (this.fromValue != null || var1 != null) {
            this.fromValueProperty().set(var1);
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

    public FillTransition(Duration duration,
                          Node node,
                          Color from,
                          Color to
                          ) {
        this.setDuration(duration);
        this.setNode(node);
        this.setFromValue(from);
        this.setToValue(to);
        this.setCycleDuration(duration);
    }

    public FillTransition(
            Duration duration,
            Color from,
            Color to
    ) {
        this(duration, (Node) null, from, to);
    }

    public FillTransition(Duration duration, Node node) {
        this(duration, node, (Color) null, (Color) null);
    }

    public FillTransition(Duration duration) {
        this(duration, (Node) null);
    }

    public FillTransition() {
        this(DEFAULT_DURATION, (Node) null);
    }

    @Override
    protected void interpolate(double v) {

    }

    private Node getTargetNode() {
        Node node = this.getNode();
        if (node == null) {
            Node targetNode = this.getParentTargetNode();
            if (targetNode instanceof Node) {
                node = (Shape) targetNode;
            }
        }

        return node;
    }

    boolean startable(boolean bool) {
        if (!super.startable(bool)) {
            return false;
        } else if (!bool && this.cachedNode != null) {
            return true;
        } else {
            Node targetNode = this.getTargetNode();
            return targetNode != null && (this.getFromValue() != null) && this.getToValue() != null;
        }
    }

    void sync(boolean bool) {
        super.sync(bool);
        if (bool || this.cachedNode == null) {
            this.cachedNode = this.getTargetNode();
            Color fromValue = this.getFromValue();
            this.start = fromValue != null ? fromValue : (Color) null;
            this.end = this.getToValue();
        }
    }
}
