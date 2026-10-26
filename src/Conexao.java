import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/controle_financeiro_pessoal_software";

    private static final String USUARIO = "root";

    private static final String SENHA = "";

    public static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    SENHA
            );

            System.out.println("Conexão com o banco realizada com sucesso!");

            return conexao;

        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco!");
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) {
        conectar();
    }
}