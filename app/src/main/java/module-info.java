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

    opens cypher.enforcers.controllers to javafx.fxml;
    opens cypher.enforcers.controllers.CodeViewControllers to javafx.fxml;
    exports cypher.enforcers;
}