module org.example.korkiedp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.korkiedp to javafx.fxml;
    exports org.example.korkiedp.app;
    opens org.example.korkiedp.app to javafx.fxml;
}