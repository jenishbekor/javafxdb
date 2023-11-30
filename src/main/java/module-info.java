module taskmn.taskmanagedb{
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens taskmn.taskmanagedb to javafx.fxml;
    exports taskmn.taskmanagedb;
}