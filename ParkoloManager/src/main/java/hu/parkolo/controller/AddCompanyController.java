
package hu.parkolo.controller;

import hu.parkolo.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCompanyController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField parkingSpaceField;

    @FXML
    private TextField disabilitySpaceField;

    @FXML
    private TextField priceField;

    @FXML
    private void saveCompany() {

        String name = nameField.getText().trim();

        try {
            int parkingSpaces = Integer.parseInt(
                    parkingSpaceField.getText().trim()
            );

            int disabilitySpaces = Integer.parseInt(
                    disabilitySpaceField.getText().trim()
            );

            double price = Double.parseDouble(
                    priceField.getText().trim().replace(",", ".")
            );

            if (name.isEmpty()) {
                showAlert("Hiba", "A cég neve kötelező!");
                return;
            }

            if (parkingSpaces <= 0 || disabilitySpaces < 0 || price < 0) {
                showAlert("Hiba", "Érvénytelen számadat!");
                return;
            }

            if (disabilitySpaces > parkingSpaces) {
                showAlert("Hiba",
                        "A mozgáskorlátozott helyek száma nem lehet több az összes parkolóhelynél!");
                return;
            }

            String sql = """
                    INSERT INTO Companies
                    (name, parkingspace_num, disabilityPspace_num, price_p_hour, free)
                    VALUES (?, ?, ?, ?, 0)
                    """;

            try (Connection connection = Database.connect();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, name);
                statement.setInt(2, parkingSpaces);
                statement.setInt(3, disabilitySpaces);
                statement.setDouble(4, price);

                statement.executeUpdate();
            }

            showAlert("Sikeres mentés",
                    "A cég sikeresen hozzáadásra került!");

            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Hiba", "Kérlek, minden számmezőbe érvényes számot írj!");

        } catch (SQLException e) {
            if (e.getMessage() != null &&
                    e.getMessage().contains("UNIQUE constraint failed")) {
                showAlert("Hiba", "Ez a cégnév már létezik!");
            } else {
                e.printStackTrace();
                showAlert("Adatbázishiba", e.getMessage());
            }
        }
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
