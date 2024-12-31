package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SampleController {

    @FXML
    private Button close;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    private double x = 0;
    private double y = 0;

    public void login() {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connect = database.connect();

        try {
            if (connect == null) {
                showAlert(Alert.AlertType.ERROR, "Bağlantı Hatası", "Veri tabanına bağlanılamadı.");
                return;
            }

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Eksik Bilgi", "Lütfen tüm alanları doldurun.");
                return;
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Başarılı Giriş", "Giriş başarılı!");

                getData.username = username.getText();
                getData.path = username.getText();

                
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                
                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();

                
                Stage currentStage = (Stage) main_form.getScene().getWindow();
                currentStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Giriş Hatası", "Kullanıcı adı veya şifre yanlış.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Bir sorun oluştu: " + e.getMessage());
        } finally {
            try {
                if (result != null) {
					result.close();
				}
                if (prepare != null) {
					prepare.close();
				}
                if (connect != null) {
					connect.close();
				}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void close() {
        System.exit(0);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}