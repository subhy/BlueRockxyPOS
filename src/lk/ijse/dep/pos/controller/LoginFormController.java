package lk.ijse.dep.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button btnSearch;

    @FXML
    private Label lblCashSale;

    @FXML
    private JFXPasswordField pf_password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    void btnSearch_OnAction(ActionEvent event) {
        Parent root = null;

        FXMLLoader fxmlLoader = null;

        if (pf_password.getText().equals("19681014")){
            try {
                root =  FXMLLoader.load(this.getClass().getResource("/lk/ijse/dep/pos/view/ViewReportForm.fxml"));

                    Scene subScene = new Scene(root);
                    Stage primaryStage = (Stage) this.root.getScene().getWindow();

                    primaryStage.setScene(subScene);
                    primaryStage.centerOnScreen();

                    TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                    tt.setFromX(-subScene.getWidth());
                    tt.setToX(0);
                    tt.play();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (pf_password.getText().equals("19921104")){

        }else{

        }


    }

    @FXML
    void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/dep/pos/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


}
