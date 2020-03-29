package shape3d;

import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;

public class Test3D extends Application {


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        int[] faces = {0,0, 1,1, 3,3, 3,3, 2,2, 1,1};
        float[] points = {0,0,0, 100,0,0, 100,100,0, 0,100,0};
        float[] texCoords = {0,0, 1,0, 1,1, 0,1};

        AnchorPane an = new AnchorPane();
        TriangleMeshBuilder builder = new TriangleMeshBuilder();
        TriangleMesh triangleMesh = builder.build();
        triangleMesh.getFaces().addAll(faces);
        triangleMesh.getPoints().addAll(points);
        triangleMesh.getTexCoords().addAll(texCoords);
        MeshView meshView = new MeshView(triangleMesh);

        meshView.setMaterial(new PhongMaterial(Color.BLUE));
        an.getChildren().add(meshView);
        AnchorPane.setLeftAnchor(meshView, 300.0);
        AnchorPane.setTopAnchor(meshView, 400.0);

        primaryStage.setScene(new Scene(an, 800, 600));
        primaryStage.show();
    }
}
