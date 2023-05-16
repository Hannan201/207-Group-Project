package themes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Paint;

public class ThemeState<T> {

    private T start;
    private T end;

    public ThemeState() {
    }

    public ThemeState(T regular, T onHover) {
        this.start = regular;
        this.end = onHover;
    }

    public T getStart() {
        return this.start;
    }

    public T getEnd() {
        return this.end;
    }
}
