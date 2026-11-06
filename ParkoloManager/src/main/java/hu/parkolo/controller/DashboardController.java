
package hu.parkolo.controller;

import hu.parkolo.Database;
import hu.parkolo.model.Company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {

    @FXML
    private Label companyNameLabel;

    @FXML
    private Label totalSpacesLabel;

    @FXML
    private Label disabilitySpacesLabel;

    @FXML
    private Label occupiedSpacesLabel;

    @FXML
    private Label freeSpacesLabel;

    @FXML
    private Label priceLabel;

    private Company company;

    public void setCompany(Company company) {
        this.company = company;
        loadDashboard();
    }

    @FXML
    private void openEntry() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/hu/parkolo/entry.fxml")
        );

        Scene scene = new Scene(loader.load());

        EntryController controller = loader.getController();
        controller.setCompany(company);

        Stage stage = new Stage();
        stage.setTitle("Autó behajtása");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(companyNameLabel.getScene().getWindow());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

        // Frissítjük az adatokat az ablak bezárása után
        loadDashboard();
    }

    private void loadDashboard() {

        if (company == null) {
            return;
        }

        companyNameLabel.setText(company.getName());

        totalSpacesLabel.setText(
                String.valueOf(company.getParkingSpaceNum())
        );

        disabilitySpacesLabel.setText(
                String.valueOf(company.getDisabilityPspaceNum())
        );

        priceLabel.setText(
                company.getPricePerHour() + " Ft / óra"
        );

        String sql = """
                SELECT COUNT(*)
                FROM parking
                WHERE company_id = ?
                AND check_out IS NULL
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, company.getId());

            try (ResultSet result = statement.executeQuery()) {

                int occupied = result.getInt(1);
                int free = company.getParkingSpaceNum() - occupied;

                occupiedSpacesLabel.setText(
                        String.valueOf(occupied)
                );

                freeSpacesLabel.setText(
                        String.valueOf(free)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            occupiedSpacesLabel.setText("Hiba");
            freeSpacesLabel.setText("Hiba");
        }
    }
}
