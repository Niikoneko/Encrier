module org.niikoneko.encrier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.tinylog.api;
    requires org.hsqldb;
    requires java.desktop;
    requires java.net.http;
    requires java.sql;

    opens org.niikoneko.encrier to javafx.fxml;
    opens org.niikoneko.encrier.userInterface.menus to javafx.fxml;
    exports org.niikoneko.encrier;
    exports org.niikoneko.encrier.userInterface;
    opens org.niikoneko.encrier.userInterface to javafx.fxml;
}