package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerMain {

	@FXML
	private AnchorPane controllerPane;

	@FXML
	private MenuItem Sair;

	@FXML
	private MenuItem Loggout;

	@FXML
	public void Sair() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmação de Saída");
		alert.setHeaderText("Deseja realmente sair do sistema?");
		alert.setContentText("Escolha uma opção:");

		ButtonType buttonSim = new ButtonType("Sim");
		ButtonType buttonNao = new ButtonType("Não");

		alert.getButtonTypes().setAll(buttonSim, buttonNao);

		alert.showAndWait().ifPresent(resposta -> {
			if (resposta == buttonSim) {
				System.exit(0);
			}
		});
	}

	@FXML
	public void Loggout() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmação de Logout");
		alert.setHeaderText("Deseja realmente voltar à tela de login?");
		alert.setContentText("Escolha uma opção:");

		ButtonType buttonSim = new ButtonType("Sim");
		ButtonType buttonNao = new ButtonType("Não");

		alert.getButtonTypes().setAll(buttonSim, buttonNao);

		alert.showAndWait().ifPresent(resposta -> {
			if (resposta == buttonSim) {
				try {
					Stage stageAtual = (Stage) controllerPane.getScene().getWindow();
					stageAtual.close();

					Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

					Stage stageLogin = new Stage();
					stageLogin.setScene(new Scene(root));
					stageLogin.initStyle(StageStyle.TRANSPARENT);
					stageLogin.centerOnScreen();
					stageLogin.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	private void cadUsuario() {
		carregarTela("cadUsuario.fxml", "Cadastro de Usuários");

	}

	private void carregarTela(String fxmlFile, String tituloFuncionalidade) {
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource(fxmlFile));
			controllerPane.getChildren().clear();
			controllerPane.getChildren().add(fxml);

			AnchorPane.setTopAnchor(fxml, 0.0);
			AnchorPane.setBottomAnchor(fxml, 0.0);
			AnchorPane.setLeftAnchor(fxml, 0.0);
			AnchorPane.setRightAnchor(fxml, 0.0);

			Scene cena = controllerPane.getScene();

			if (cena != null) {
				Stage stage = (Stage) cena.getWindow();
				stage.setTitle("Sistema - by Rodrigo Faro | " + tituloFuncionalidade);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
