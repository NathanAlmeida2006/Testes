import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidacaoUsuarioTest {

    @Test
    public void testNomeValido() {
        String nomeValido = "Nathan Almeida";
        boolean resultado = validarNome(nomeValido);
        assertTrue(resultado, "Falha: Nome válido deveria ser aceito.");
    }

    @Test
    public void testEmailValido() {
        String emailValido = "nathan.almeida@example.com";
        boolean resultado = validarEmail(emailValido);
        assertTrue(resultado, "Falha: Email válido deveria ser aceito.");
    }

    @Test
    public void testDataNascimentoValida() {
        String dataNascimentoValida = "2000-09-30"; // Use o formato correto esperado
        boolean resultado = validarDataNascimento(dataNascimentoValida);
        assertTrue(resultado, "Falha: Data de nascimento válida deveria ser aceita.");
    }

    // Testes para valores inválidos
    @Test
    public void testNomeInvalido() {
        String nomeInvalido = ""; // Nome vazio ou inválido
        boolean resultado = validarNome(nomeInvalido);
        assertFalse(resultado, "Falha: Nome inválido não deveria ser aceito.");
    }

    @Test
    public void testEmailInvalido() {
        String emailInvalido = "nathan@.com"; // Email inválido
        boolean resultado = validarEmail(emailInvalido);
        assertFalse(resultado, "Falha: Email inválido não deveria ser aceito.");
    }

    @Test
    public void testDataNascimentoInvalida() {
        String dataNascimentoInvalida = "2023-02-30"; // Data inexistente
        boolean resultado = validarDataNascimento(dataNascimentoInvalida);
        assertFalse(resultado, "Falha: Data de nascimento inválida não deveria ser aceita.");
    }

    @Test
    public void testDataNascimentoMenorIdade() {
        String dataNascimentoMenorIdade = LocalDate.now().minusYears(17).toString(); // 17 anos atrás
        boolean resultado = validarDataNascimento(dataNascimentoMenorIdade);
        assertFalse(resultado, "Falha: Data de nascimento de menor de idade não deveria ser aceita.");
    }

    // Funções de validação
    private boolean validarNome(String nome) {
        // Implementar lógica de validação do nome
        return nome != null && !nome.trim().isEmpty();
    }

    private boolean validarEmail(String email) {
        // Implementar lógica de validação do email
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean validarDataNascimento(String dataNascimento) {
        try {
            LocalDate dataNasc = LocalDate.parse(dataNascimento);
            LocalDate hoje = LocalDate.now();
            int idade = Period.between(dataNasc, hoje).getYears();
            return idade >= 18;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}