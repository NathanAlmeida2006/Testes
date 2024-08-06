import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Classe principal para a coleta de informações do usuário via entrada padrão.
 * A classe solicita ao usuário que digite seu nome, email, CPF e data de nascimento,
 * valida a data de nascimento, verifica se o usuário tem 18 anos ou mais, e exibe esses dados formatados.
 * @version 1.3
 */
public class Main {
    // Padrão de email genérico
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern nomePattern = Pattern.compile("^[a-zA-Z\\s]+$");

    /**
     * Método principal que executa o programa de coleta de informações do usuário.
     * Ele solicita que o usuário insira seu nome, email, CPF e data de nascimento,
     * valida a data de nascimento, verifica se o usuário tem 18 anos ou mais,
     * e exibe os dados formatados.
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> erros = new ArrayList<>();

        // Solicita e coleta os atributos do usuário
        System.out.println("Digite seu nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) {
            erros.add("Nome está em branco.");
        } else if (!nomePattern.matcher(nome).matches()) {
            erros.add("Nome inválido. Por favor, digite um nome contendo apenas letras e espaços simples.");
        }

        // Solicita e coleta o email do usuário
        System.out.println("Digite seu email: ");
        String email = sc.nextLine().trim();
        if (email.isEmpty()) {
            erros.add("Email está em branco.");
        } else if (!isEmailValido(email)) {
            erros.add("Email inválido. O email deve seguir um formato válido, como 'nathan@email.com'.");
        }

        // Solicita e coleta o CPF do usuário
        System.out.println("Digite seu CPF: ");
        String cpf = sc.nextLine().trim();
        if (cpf.isEmpty()) {
            erros.add("CPF está em branco.");
        } else if (!isCpfValido(cpf)) {
            erros.add("CPF inválido. Por favor, digite um CPF válido.");
        }

        // Solicita e valida a data de nascimento do usuário
        System.out.println("Digite sua data de nascimento (dd/MM/yyyy, ddMMyyyy ou ddMMyy): ");
        String dataNascimento = sc.nextLine().trim();
        LocalDate nascimento = validarDataNascimento(dataNascimento, erros);

        // Fechar o scanner
        sc.close();

        // Exibir erros ou dados do usuário
        if (erros.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Dados do usuário:");
            System.out.println("Nome: " + nome);
            System.out.println("Email: " + email);
            System.out.println("CPF: " + formatarCpf(cpf));
            assert nascimento != null;
            System.out.println("Data de Nascimento: " + nascimento.format(formatter));
        } else {
            System.out.println("Erros encontrados:");
            for (String erro : erros) {
                System.out.println("- " + erro);
            }
        }
    }

    /**
     * Método para verificar se o email está no formato correto.
     * O email deve seguir um padrão geral.
     * @param email O email a ser verificado.
     * @return true se o email for válido, false caso contrário.
     */
    private static boolean isEmailValido(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Método para verificar se um CPF é válido.
     * @param cpf O CPF a ser verificado.
     * @return true se o CPF for válido, false caso contrário.
     */
    private static boolean isCpfValido(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 000.000.000-00)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int primeiroDigitoVerificador = 11 - (soma % 11);
        if (primeiroDigitoVerificador >= 10) {
            primeiroDigitoVerificador = 0;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        soma += primeiroDigitoVerificador * 2;
        int segundoDigitoVerificador = 11 - (soma % 11);
        if (segundoDigitoVerificador >= 10) {
            segundoDigitoVerificador = 0;
        }

        // Verifica se os dígitos calculados são iguais aos dígitos verificadores fornecidos
        return cpf.charAt(9) - '0' == primeiroDigitoVerificador && cpf.charAt(10) - '0' == segundoDigitoVerificador;
    }

    /**
     * Método para formatar um CPF para o formato 000.000.000-00.
     * @param cpf O CPF a ser formatado.
     * @return O CPF formatado.
     */
    private static String formatarCpf(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    /**
     * Método para validar a data de nascimento e verificar se o usuário tem 18 anos ou mais.
     * Aceita os formatos dd/MM/yyyy, ddMMyyyy e ddMMyy.
     * @param dataNascimento A data de nascimento em formato de string.
     * @param erros A lista de erros para adicionar mensagens de erro, se houver.
     * @return A data de nascimento como um objeto LocalDate, ou null se inválida.
     */
    private static LocalDate validarDataNascimento(String dataNascimento, List<String> erros) {
        if (dataNascimento.isEmpty()) {
            erros.add("Data de nascimento está em branco.");
            return null;
        }

        LocalDate nascimento = null;
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("ddMMyyyy"),
                DateTimeFormatter.ofPattern("ddMMyy")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                nascimento = LocalDate.parse(dataNascimento, formatter);
                break;
            } catch (DateTimeParseException ignored) {
            }
        }

        if (nascimento == null) {
            erros.add("Formato de data inválido. Use o formato 'dd/MM/yyyy', 'ddMMyyyy' ou 'ddMMyy'.");
            return null;
        }

        // Verificar se o usuário tem 18 anos ou mais
        if (LocalDate.now().minusYears(18).isBefore(nascimento)) {
            erros.add("Você deve ter 18 anos ou mais para continuar.");
        }

        return nascimento;
    }
}