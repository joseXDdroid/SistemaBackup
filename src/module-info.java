module MeuSistema {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens application to javafx.fxml, javafx.base;
    opens application.model to javafx.base;

    exports application;
}
