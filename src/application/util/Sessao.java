package application.util;

public class Sessao {
	private static String emailUsuarioLogado;

	public static String getEmailUsuarioLogado() {
		return emailUsuarioLogado;
	}

	public static void setEmailUsuarioLogado(String email) {
		emailUsuarioLogado = email;
	}

	public static boolean isAdmin() {
		return "admin@2025".equalsIgnoreCase(emailUsuarioLogado);
	}
}
