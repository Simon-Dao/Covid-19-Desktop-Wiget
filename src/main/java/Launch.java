import datafetch.CovidDataModel;
import datafetch.DataProviderService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launch extends Application {

    private double xOffset;
    private double yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setOpacity(0);
        primaryStage.show();

        Stage secondaryStage = new Stage();
        secondaryStage.initStyle(StageStyle.UNDECORATED);
        secondaryStage.initOwner(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/gui/widget/widget.fxml"));
        Scene scene = new Scene(root);
        secondaryStage.setScene(scene);
        secondaryStage.show();

        setMouseListeners(scene, secondaryStage);
    }
    private void setMouseListeners(Scene scene, Stage secondaryStage) {
        //make it right top aligned
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        secondaryStage.setX(bounds.getMaxX() - scene.getWidth() - (bounds.getMaxX()/10));
        secondaryStage.setY(bounds.getMinY() + (bounds.getMaxY()/10));

        //add support for drag and move
        scene.setOnMousePressed(event -> {
            xOffset = secondaryStage.getX() - event.getScreenX();
            yOffset = secondaryStage.getY() - event.getScreenY();
        });

        scene.setOnMouseDragged(event -> {
            secondaryStage.setX(event.getScreenX() + xOffset);
            secondaryStage.setY(event.getScreenY() + yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
