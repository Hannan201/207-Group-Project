module cypher.enforcers {
    // Java Desktop (Used for launching the web browser).
    requires java.desktop;

    // JavaFX.
    requires javafx.controls;
    requires javafx.fxml;

    // Logging API.
    requires org.slf4j;

    // ValidatorFX.
    requires net.synedra.validatorfx;

    // ControlsFX.
    requires org.controlsfx.controls;

    // SQL support.
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // Adds utility functions to work with URL's.
    requires org.apache.commons.io;

    // JavaFX requires access to the controllers and any other
    // methods from other packages used inside the controllers.
    opens cypher.enforcers.controllers to javafx.fxml;
    opens cypher.enforcers.controllers.CodeViewControllers to javafx.fxml;
    opens cypher.enforcers.views to javafx.fxml;
    opens cypher.enforcers.views.utilities to javafx.fxml;
    opens cypher.enforcers.views.utilities.codeViewUtilities to javafx.fxml;

    exports cypher.enforcers;
}