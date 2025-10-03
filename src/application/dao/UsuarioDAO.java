package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.Usuario;
import application.util.Conexao;

public class UsuarioDAO {

	public boolean autenticar(String email, String senha) {
		try (Connection conn = Conexao.getConnection()) {
			String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, senha);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Busca exata pelo nome
	public Usuario buscarPorNome(String nome) {
		Usuario usuario = null;
		String sql = "SELECT * FROM usuarios WHERE nome = ?";

		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, nome);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setEmail(rs.getString("email"));
				usuario.setSenha(rs.getString("senha"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuario;
	}

	// Busca parcial, retorna lista de usuários cujo nome contenha o texto
	public List<Usuario> buscarPorNomeParcial(String nomeParcial) {
		List<Usuario> lista = new ArrayList<>();
		String sql = "SELECT * FROM usuarios WHERE nome LIKE ?";

		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, "%" + nomeParcial + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setEmail(rs.getString("email"));
				usuario.setSenha(rs.getString("senha"));
				lista.add(usuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	// Lista todos
	public List<Usuario> buscarTodos() {
		List<Usuario> usuarios = new ArrayList<>();
		String sql = "SELECT * FROM usuarios";

		try (Connection conn = Conexao.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setEmail(rs.getString("email"));
				usuario.setSenha(rs.getString("senha"));
				usuarios.add(usuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuarios;
	}

	public void atualizar(Usuario usuario) {
		String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.setInt(4, usuario.getId());

			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean excluir(int id) {
		String sql = "DELETE FROM usuarios WHERE id = ?";

		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			int linhasAfetadas = stmt.executeUpdate();

			return linhasAfetadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean criar(Usuario usuario) {
		String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";

		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());

			int linhasAfetadas = stmt.executeUpdate();

			return linhasAfetadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
