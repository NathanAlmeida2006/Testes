import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * Classe principal que executa o programa de coleta, validação e exibição de dados de um usuário.
 * @version 1.4
 */
public class Main {

    /**
     * Metodo principal que solicita e coleta os dados do usuário, valida as informações e exibe os resultados ou erros.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> erros = new ArrayList<>();

        // Solicita e coleta os atributos do usuário
        System.out.println("Digite seu nome: ");
        String nome = sc.nextLine().trim();

        System.out.println("Digite seu email: ");
        String email = sc.nextLine().trim();

        System.out.println("Digite seu CPF: ");
        String cpf = sc.nextLine().trim();

        System.out.println("Digite sua data de nascimento (dd/MM/yyyy, ddMMyyyy ou ddMMyy): ");
        String dataNascimento = sc.nextLine().trim();

        // Cria o objeto Usuario e valida os dados
        Usuario usuario = new Usuario(nome, email, cpf, dataNascimento, erros);

        // Fechar o scanner
        sc.close();

        // Exibir erros ou dados do usuário
        if (erros.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Dados do usuário:");
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("CPF: " + usuario.getCpf());
            System.out.println("Data de Nascimento: " + usuario.getDataNascimento().format(formatter));
        } else {
            System.out.println("Erros encontrados:");
            for (String erro : erros) {
                System.out.println("- " + erro);
            }
        }
    }
}
