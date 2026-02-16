module cherry {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens cherry.gui to javafx.fxml;
    opens cherry to javafx.fxml;

    exports cherry;
    exports cherry.gui;
    exports cherry.task;
    exports cherry.ui;
}