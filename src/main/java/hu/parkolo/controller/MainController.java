
package hu.parkolo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void openAddCompany() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/hu/parkolo/add-company.fxml")
        );

        Stage stage = new Stage();
        stage.setTitle("Új cég hozzáadása");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    private void openSelectCompany() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/hu/parkolo/select-company.fxml")
        );

        Stage stage = new Stage();
        stage.setTitle("Cég kiválasztása");
        stage.setScene(new Scene(loader.load()));
        stage.setResizable(false);
        stage.show();
    }
}
