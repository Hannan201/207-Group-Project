package themes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ThemeState<T> {

    private ObjectProperty<T> start;
    private ObjectProperty<T> end;

    public ThemeState() {
        this(null, null);
    }

    public ThemeState(T regular, T onHover) {
        setStart(regular);
        setEnd(onHover);
    }

    public void setStart(T newStart) {
        if (this.start != null || newStart != null) {
            this.startProperty().set(newStart);
        }
    }

    public T getStart() {
        return this.start == null ? null : this.start.get();
    }

    public ObjectProperty<T> startProperty() {
        if (this.start == null) {
            start = new SimpleObjectProperty<>(this, "start", null);
        }

        return start;
    }

    public void setEnd(T newEnd) {
        if (this.end != null || newEnd != null) {
            this.endProperty().set(newEnd);
        }
    }

    public T getEnd() {
        return this.end == null ? null : this.end.get();
    }

    public ObjectProperty<T> endProperty() {
        if (this.end == null) {
            end = new SimpleObjectProperty<>(this, "end", null);
        }

        return end;
    }
}
