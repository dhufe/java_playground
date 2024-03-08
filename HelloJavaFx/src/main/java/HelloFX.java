import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// Event handling
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class HelloFX extends Application {
    @Override
    public void start(Stage stage) {

        EventHandler<MouseEvent> evHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle ( MouseEvent ev ) {
                System.out.println( ev.toString() );
                ev.consume();
            }
        };

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        // create Pane
        GridPane pane1 = new GridPane();

        // create controls for dummy inputs
        Label lUser = new Label( "Username:" );
        Label lPassword = new Label( "Password:" );
        TextField tUser = new TextField( );
        PasswordField pPassword = new PasswordField();
        // create button and install event handling
        Button btn1 = new Button("Click Me!");
        btn1.setOnMouseClicked( evHandler );

        // Arranging controls manually
        pane1.addRow( 0, lUser, tUser );
        pane1.addRow( 1, lPassword, pPassword );
        pane1.addRow( 2, btn1 );

        // System.out.println("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        Scene scene = new Scene( pane1 );
        stage.setScene(scene);
        stage.setTitle( "Simple Login Wndw");
        stage.show();
    }

    @Override
    public void stop () {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
