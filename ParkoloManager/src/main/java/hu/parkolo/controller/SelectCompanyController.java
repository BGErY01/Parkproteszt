
package hu.parkolo.controller;

import hu.parkolo.Database;
import hu.parkolo.model.Company;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

public class SelectCompanyController {

    @FXML
    private ListView<Company> companyList;

    @FXML
    public void initialize() {
        loadCompanies();
    }

    private void loadCompanies() {

        String sql = """
                SELECT id, name, parkingspace_num,
                       disabilityPspace_num, price_p_hour, free
                FROM Companies
                ORDER BY name
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            var companies = FXCollections.<Company>observableArrayList();

            while (result.next()) {
                Company company = new Company(
                        result.getString("name"),
                        result.getInt("parkingspace_num"),
                        result.getInt("disabilityPspace_num"),
                        result.getDouble("price_p_hour")
                );

                company.setId(result.getInt("id"));
                companies.add(company);
            }

            companyList.setItems(companies);

            companyList.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
                @Override
                protected void updateItem(Company company, boolean empty) {
                    super.updateItem(company, empty);

                    if (empty || company == null) {
                        setText(null);
                    } else {
                        setText(company.getName());
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Adatbázishiba", e.getMessage());
        }
    }

    @FXML
    private void enterCompany() throws IOException {

        Company selected = companyList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Figyelem", "Először válassz ki egy céget!");
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/hu/parkolo/dashboard.fxml")
        );

        Scene scene = new Scene(loader.load());

        DashboardController controller = loader.getController();
        controller.setCompany(selected);

        Stage stage = (Stage) companyList.getScene().getWindow();
        stage.setTitle("ParkolóManager - " + selected.getName());
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) companyList.getScene().getWindow();
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
