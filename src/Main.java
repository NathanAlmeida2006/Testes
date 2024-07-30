import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Classe principal para a coleta de informações do usuário via entrada padrão.
 * A classe solicita ao usuário que digite seu nome, email, CPF e data de nascimento,
 * valida a data de nascimento, verifica se o usuário tem 18 anos ou mais, e exibe esses dados formatados.
 *
 * @version 1.1
 */
public class Main {

    // Padrão de email genérico
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    /**
     * Método principal que executa o programa de coleta de informações do usuário.
     * Ele solicita que o usuário insira seu nome, email, CPF e data de nascimento,
     * valida a data de nascimento, verifica se o usuário tem 18 anos ou mais,
     * e exibe os dados formatados.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Pattern nomePattern = Pattern.compile("^[a-zA-Z\\s]+$");

        // Solicita e coleta os atributos do usuário
        System.out.println("Digite seu nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty() || !nomePattern.matcher(nome).matches() || nome.contains("  ")) {
            System.out.println("Nome inválido. Por favor, digite um nome contendo apenas letras e espaços simples.");
            sc.close();
            return; // Encerra o programa se o nome for inválido
        }

        // Solicita e coleta o email do usuário
        System.out.println("Digite seu email: ");
        String email = sc.nextLine().trim();
        if (!isEmailValido(email)) {
            System.out.println("Email inválido. O email deve seguir um formato válido, como 'nathan@email.com'.");
            sc.close();
            return; // Encerra o programa se o email for inválido
        }

        // Solicita e coleta o CPF do usuário
        System.out.println("Digite seu CPF: ");
        String cpf = sc.nextLine();

        LocalDate nascimento;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Validação da data de nascimento
        try {
            System.out.println("Digite sua data de nascimento (dd/MM/yyyy): ");
            String dataNascimento = sc.nextLine();
            nascimento = LocalDate.parse(dataNascimento, formatter);

            // Verificar se o usuário tem 18 anos ou mais
            if (LocalDate.now().minusYears(18).isBefore(nascimento)) {
                System.out.println("Você deve ter 18 anos ou mais para continuar.");
                sc.close();
                return; // Encerra o programa se o usuário não tiver 18 anos ou mais
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. O programa será encerrado.");
            sc.close();
            return; // Encerra o programa se a data for inválida
        }

        // Fechar o scanner
        sc.close();

        // Exibindo os dados do usuário
        System.out.println("Dados do usuário:");
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("CPF: " + cpf);
        System.out.println("Data de Nascimento: " + nascimento.format(formatter));
    }

    /**
     * Método para verificar se o email está no formato correto.
     * O email deve seguir um padrão geral.
     *
     * @param email O email a ser verificado.
     * @return true se o email for válido, false caso contrário.
     */
    private static boolean isEmailValido(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
