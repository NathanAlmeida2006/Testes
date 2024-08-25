import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que representa um usuário e realiza a validação de seus atributos.
 */
public class Usuario {
    private final String nome;
    private final String email;
    private final String cpf;
    private final LocalDate dataNascimento;

    // Padrões para validação
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern nomePattern = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern cpfPattern = Pattern.compile("^\\d{11}$|^(\\d{3}\\.){2}\\d{3}-\\d{2}$");

    /**
     * Construtor da classe Usuario que inicializa os atributos do usuário e realiza as validações.
     *
     * @param nome           Nome do usuário.
     * @param email          Email do usuário.
     * @param cpf            CPF do usuário.
     * @param dataNascimento Data de nascimento do usuário em formato String.
     * @param erros          Lista de erros onde serão adicionadas as mensagens de erro encontradas durante a validação.
     */
    public Usuario(String nome, String email, String cpf, String dataNascimento, List<String> erros) {
        this.nome = validarNome(nome, erros);
        this.email = validarEmail(email, erros);
        this.cpf = validarCpf(cpf, erros);
        this.dataNascimento = validarDataNascimento(dataNascimento, erros);
    }

    /**
     * Valida o nome do usuário.
     *
     * @param nome  Nome a ser validado.
     * @param erros Lista onde os erros de validação serão adicionados.
     * @return Nome validado ou null se inválido.
     */
    private String validarNome(String nome, List<String> erros) {
        if (nome.isEmpty()) {
            erros.add("Nome está em branco.");
            return null;
        } else if (!nomePattern.matcher(nome).matches()) {
            erros.add("Nome inválido. Por favor, digite um nome contendo apenas letras e espaços simples.");
            return null;
        }
        return nome;
    }

    /**
     * Valida o email do usuário.
     *
     * @param email Email a ser validado.
     * @param erros Lista onde os erros de validação serão adicionados.
     * @return Email validado ou null se inválido.
     */
    private String validarEmail(String email, List<String> erros) {
        if (email.isEmpty()) {
            erros.add("Email está em branco.");
            return null;
        } else if (!isEmailValido(email)) {
            erros.add("Email inválido. O email deve seguir um formato válido, como 'nathan@email.com'.");
            return null;
        }
        return email;
    }

    /**
     * Valida o CPF do usuário.
     *
     * @param cpf   CPF a ser validado.
     * @param erros Lista onde os erros de validação serão adicionados.
     * @return CPF formatado e validado ou null se inválido.
     */
    private String validarCpf(String cpf, List<String> erros) {
        if (cpf.isEmpty()) {
            erros.add("CPF está em branco.");
            return null;
        } else if (!isCpfValido(cpf)) {
            erros.add("CPF inválido. Por favor, digite um CPF válido.");
            return null;
        }
        return formatarCpf(cpf);
    }

    /**
     * Valida a data de nascimento do usuário.
     *
     * @param dataNascimento Data de nascimento a ser validada.
     * @param erros          Lista onde os erros de validação serão adicionados.
     * @return Data de nascimento validada ou null se inválida.
     */
    private LocalDate validarDataNascimento(String dataNascimento, List<String> erros) {
        if (dataNascimento.isEmpty()) {
            erros.add("Data de nascimento está em branco.");
            return null;
        }

        LocalDate nascimento = null;
        boolean formatoValido = false;
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("ddMMyyyy"),
                DateTimeFormatter.ofPattern("ddMMyy")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                nascimento = LocalDate.parse(dataNascimento, formatter);
                formatoValido = true;
                break;
            } catch (DateTimeParseException ignored) {
            }
        }

        if (!formatoValido) {
            erros.add("Formato de data inválido. Use o formato 'dd/MM/yyyy', 'ddMMyyyy' ou 'ddMMyy'.");
            return null;
        }

        // Verifica se a data é inexistente
        if (!dataNascimento.equals(nascimento.format(formatters[0]))
                && !dataNascimento.equals(nascimento.format(formatters[1]))
                    && !dataNascimento.equals(nascimento.format(formatters[2]))) {
            erros.add("Data inexistente. Por favor, insira uma data válida.");
            return null;
        }

        // Verificar se o usuário tem 18 anos ou mais e menos de 130 anos
        LocalDate hoje = LocalDate.now();
        if (hoje.minusYears(18).isBefore(nascimento)) {
            erros.add("Você deve ter 18 anos ou mais para continuar.");
        } else if (hoje.minusYears(130).isAfter(nascimento)) {
            erros.add("Data de nascimento inválida. Por favor, insira uma data de até 130 anos atrás.");
        }

        return nascimento;
    }


    /**
     * Verifica se o email está em um formato válido.
     *
     * @param email Email a ser verificado.
     * @return true se o email for válido, caso contrário false.
     */
    private boolean isEmailValido(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Verifica se o CPF está em um formato válido.
     *
     * @param cpf CPF a ser verificado.
     * @return true se o CPF for válido, caso contrário false.
     */
    private boolean isCpfValido(String cpf) {
        Matcher matcher = cpfPattern.matcher(cpf);
        if (!matcher.matches()) {
            return false;
        }

        // Remove os caracteres de formatação se estiverem presentes
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

        // Verifica os dígitos verificadores
        return cpf.charAt(9) - '0' == primeiroDigitoVerificador && cpf.charAt(10) - '0' == segundoDigitoVerificador;
    }

    /**
     * Formata o CPF para o padrão XXX.XXX.XXX-XX.
     *
     * @param cpf CPF a ser formatado.
     * @return CPF formatado.
     */
    private String formatarCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
