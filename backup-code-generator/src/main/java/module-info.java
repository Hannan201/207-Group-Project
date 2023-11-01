module backup.code.generator {
    // Java Desktop (Used for launching the web browser).
    requires java.desktop;

    // JavaFX.
    requires javafx.controls;
    requires javafx.fxml;

    // Logging API.
    requires org.slf4j;

    // Configure logging implementation (logback).
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    // ValidatorFX.
    requires net.synedra.validatorfx;

    // ControlsFX.
    requires org.controlsfx.controls;

    // SQLite support.
    requires java.sql;
    requires java.sql.rowset;
    requires org.xerial.sqlitejdbc;

    // Adds utility functions to work with URLs.
    requires org.apache.commons.io;

    // JavaFX requires access to the controllers.
    opens cypher.enforcers.controllers to javafx.fxml;
    opens cypher.enforcers.controllers.codeViewControllers to javafx.fxml;
    opens cypher.enforcers.views.accountview to javafx.fxml;
    opens cypher.enforcers.views.codeview to javafx.fxml;

    exports cypher.enforcers;
}