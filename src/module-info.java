module JBlockCreator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.jfxtras.styles.jmetro;
    requires java.prefs;

    exports jblockmain;
    opens jblockui to javafx.fxml;
    exports jblockui;
}