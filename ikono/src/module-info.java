module asdf.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens asdf.demo1 to javafx.fxml;
    exports asdf.demo1;
}