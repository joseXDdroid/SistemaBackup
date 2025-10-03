package application;

import application.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class ControllerLogin {

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnSair;

    @FXML
    private PasswordField edtSenha;

    @FXML
    private TextField edtUsuario;

    @FXML
    private CheckBox mSenha;

    @FXML
    private TextField senhaVisivel;

    public void close() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText("Deseja realmente sair?");
        alerta.setContentText("Clique em Sim para fechar o sistema.");

        ButtonType botaoSim = new ButtonType("Sim");
        ButtonType botaoNao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(botaoSim, botaoNao);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botaoSim) {
            System.exit(0);
        }
    }

    @FXML
    public void login() {
        String usuario = edtUsuario.getText().trim();
        String senha = mSenha.isSelected() ? senhaVisivel.getText() : edtSenha.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Campos vazios");
            alert.setContentText("Por favor, preencha o usuário e a senha.");
            alert.showAndWait();
            return;
        }

        loginComCredenciais(usuario, senha);
    }

    private void loginComCredenciais(String usuario, String senha) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean autenticado = usuarioDAO.autenticar(usuario, senha);

            if (autenticado) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Bem-Sucedido");
                alert.setHeaderText(null);
                alert.setContentText("Bem-vindo, " + usuario + "! Login realizado com sucesso.");
                alert.showAndWait();

                btnEntrar.getScene().getWindow().hide(); // Fecha a janela de login

                Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.setTitle("Sistema - " + usuario);
                stage.setMaximized(true);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro no Login");
                alert.setHeaderText("Usuário ou senha inválidos");
                alert.setContentText("O login não foi realizado. Verifique suas credenciais.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir a tela principal");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro inesperado");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void mS() {
        boolean mostrar = mSenha.isSelected();

        senhaVisivel.setVisible(mostrar);
        senhaVisivel.setManaged(mostrar);

        edtSenha.setVisible(!mostrar);
        edtSenha.setManaged(!mostrar);

        if (mostrar) {
            senhaVisivel.setText(edtSenha.getText());
        } else {
            edtSenha.setText(senhaVisivel.getText());
        }
    }

    @FXML
    void pe() {
        loginComCredenciais("1", "1");
    }
}