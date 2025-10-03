package application;

import application.dao.UsuarioDAO;
import application.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ControllerCadUsuario {

	@FXML
	private Button BtSalvar, BtSair, BtCancelar, BtBusacr, BtCriar, BtEditar, BtExcluir;
	@FXML
	private TableView<Usuario> TableViewUsuarios;
	@FXML
	private TableColumn<Usuario, Integer> idUsuario;
	@FXML
	private TableColumn<Usuario, String> nomeUsuario;
	@FXML
	private TextField buscarUsuarios, cNome, cEmail, cSenha;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private Usuario usuarioSelecionado;

	private enum ModoOperacao {
		NENHUM, CRIAR, EDITAR, EXCLUIR
	}

	private ModoOperacao modoAtual = ModoOperacao.NENHUM;

	@FXML
	public void initialize() {
		idUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));

		atualizarTabela();
		BtEditar.setDisable(true);

		TableViewUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
			BtEditar.setDisable(newSel == null);
			usuarioSelecionado = newSel;
		});

		buscarUsuarios.setOnAction(event -> realizarBusca());

		TableViewUsuarios.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() == 1 && usuarioSelecionado != null) {
				preencherCampos(usuarioSelecionado);
			}
		});

		habilitarCampos(false);
	}

	@FXML
	public void BtSair() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Confirmação");
		alerta.setHeaderText("Deseja realmente sair do sistema?");
		alerta.setContentText("Essa ação encerrará a aplicação.");

		ButtonType botaoSim = new ButtonType("Sim");
		ButtonType botaoNao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

		alerta.getButtonTypes().setAll(botaoSim, botaoNao);

		ButtonType resultado = alerta.showAndWait().orElse(botaoNao);

		if (resultado == botaoSim) {
			System.exit(0);
		}
	}

	// NOVO método para verificar se operação está em andamento
	private boolean verificarOperacaoEmAndamento(String acaoDesejada) {
		if (modoAtual != ModoOperacao.NENHUM) {
			mostrarErro("Operação pendente", "Finalize a operação atual antes de " + acaoDesejada + ".");
			return false;
		}
		return true;
	}

	@FXML
	private void prepararCriacao() {
		// Verifica se pode iniciar criação
		if (!verificarOperacaoEmAndamento("iniciar uma nova criação"))
			return;

		modoAtual = ModoOperacao.CRIAR;
		limparCampos();
		habilitarCampos(true);
	}

	@FXML
	private void prepararEdicao() {
		if (!verificarOperacaoEmAndamento("iniciar uma edição"))
			return;

		if (usuarioSelecionado == null) {
			mostrarAlerta("Atenção", "Selecione um usuário para editar.");
			return;
		}
		modoAtual = ModoOperacao.EDITAR;
		habilitarCampos(true);
		preencherCampos(usuarioSelecionado);
	}

	@FXML
	private void prepararExclusao() {
		if (!verificarOperacaoEmAndamento("iniciar uma exclusão"))
			return;

		if (usuarioSelecionado == null) {
			mostrarAlerta("Atenção", "Selecione um usuário para excluir.");
			return;
		}
		modoAtual = ModoOperacao.EXCLUIR;
	}

	@FXML
	private void salvar() {
		switch (modoAtual) {
		case CRIAR -> criarUsuarioFinal();
		case EDITAR -> editarUsuarioFinal();
		case EXCLUIR -> excluirUsuarioFinal();
		default -> mostrarAlerta("Aviso", "Nenhuma operação selecionada.");
		}
		modoAtual = ModoOperacao.NENHUM;
		habilitarCampos(false);
		limparCampos();
		atualizarTabela();
	}

	@FXML
	private void cancelar() {
		modoAtual = ModoOperacao.NENHUM;
		habilitarCampos(false);
		limparCampos();
	}

	@FXML
	public void realizarBusca() {
		String nomeBuscado = buscarUsuarios.getText().trim();
		if (nomeBuscado.isEmpty()) {
			atualizarTabela();
		} else {
			ObservableList<Usuario> lista = FXCollections
					.observableArrayList(usuarioDAO.buscarPorNomeParcial(nomeBuscado));
			TableViewUsuarios.setItems(lista);
		}
	}

	private void criarUsuarioFinal() {
		Usuario novo = new Usuario();
		novo.setNome(cNome.getText());
		novo.setEmail(cEmail.getText());
		novo.setSenha(cSenha.getText());

		if (usuarioDAO.criar(novo)) {
			mostrarAlerta("Sucesso", "Usuário criado com sucesso.");
		} else {
			mostrarErro("Erro", "Erro ao criar usuário.");
		}
	}

	private void editarUsuarioFinal() {
		if (usuarioSelecionado == null)
			return;

		usuarioSelecionado.setNome(cNome.getText());
		usuarioSelecionado.setEmail(cEmail.getText());
		usuarioSelecionado.setSenha(cSenha.getText());

		usuarioDAO.atualizar(usuarioSelecionado);
		mostrarAlerta("Sucesso", "Usuário editado com sucesso.");
	}

	private void excluirUsuarioFinal() {
		if (usuarioSelecionado == null)
			return;

		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setTitle("Confirmação");
		confirm.setHeaderText(null);
		confirm.setContentText("Deseja realmente excluir o usuário " + usuarioSelecionado.getNome() + "?");

		if (confirm.showAndWait().get() == ButtonType.OK) {
			if (usuarioDAO.excluir(usuarioSelecionado.getId())) {
				mostrarAlerta("Sucesso", "Usuário excluído com sucesso.");
			} else {
				mostrarErro("Erro", "Erro ao excluir usuário.");
			}
		}
	}

	private void preencherCampos(Usuario u) {
		cNome.setText(u.getNome());
		cEmail.setText(u.getEmail());
		cSenha.setText(u.getSenha());
	}

	private void limparCampos() {
		cNome.clear();
		cEmail.clear();
		cSenha.clear();
	}

	private void habilitarCampos(boolean enable) {
		cNome.setDisable(!enable);
		cEmail.setDisable(!enable);
		cSenha.setDisable(!enable);
	}

	private void atualizarTabela() {
		ObservableList<Usuario> todos = FXCollections.observableArrayList(usuarioDAO.buscarTodos());
		TableViewUsuarios.setItems(todos);
	}

	private void mostrarAlerta(String titulo, String mensagem) {
		Alert alerta = new Alert(Alert.AlertType.INFORMATION);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(mensagem);
		alerta.showAndWait();
	}

	private void mostrarErro(String titulo, String mensagem) {
		Alert alerta = new Alert(Alert.AlertType.ERROR);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(mensagem);
		alerta.showAndWait();
	}
}
