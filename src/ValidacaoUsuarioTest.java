import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Classe de testes para validar as informações de um usuário, como nome, email e data de nascimento.
 * Utiliza o framework JUnit 5 para realizar os testes unitários.
 */
public class ValidacaoUsuarioTest {

    /**
     * Testa se o nome fornecido é considerado válido.
     * Um nome válido não deve ser nulo ou vazio.
     */
    @Test
    public void testNomeValido() {
        String nomeValido = "Nathan Almeida";
        boolean resultado = validarNome(nomeValido);
        assertTrue(resultado, "Falha: Nome válido deveria ser aceito.");
    }

    /**
     * Testa se o email fornecido é considerado válido.
     * Um email válido deve seguir um formato padrão (ex: "exemplo@dominio.com").
     */
    @Test
    public void testEmailValido() {
        String emailValido = "nathan.almeida@example.com";
        boolean resultado = validarEmail(emailValido);
        assertTrue(resultado, "Falha: Email válido deveria ser aceito.");
    }

    /**
     * Testa se a data de nascimento fornecida é válida.
     * A data deve estar no formato ISO (ex: "YYYY-MM-DD") e o usuário deve ter pelo menos 18 anos.
     */
    @Test
    public void testDataNascimentoValida() {
        String dataNascimentoValida = "2000-09-30";
        boolean resultado = validarDataNascimento(dataNascimentoValida);
        assertTrue(resultado, "Falha: Data de nascimento válida deveria ser aceita.");
    }

    /**
     * Testa se um nome inválido (nulo ou vazio) é corretamente rejeitado.
     */
    @Test
    public void testNomeInvalido() {
        String nomeInvalido = ""; // Nome vazio ou inválido
        boolean resultado = validarNome(nomeInvalido);
        assertFalse(resultado, "Falha: Nome inválido não deveria ser aceito.");
    }

    /**
     * Testa se um email inválido é corretamente rejeitado.
     */
    @Test
    public void testEmailInvalido() {
        String emailInvalido = "nathan@.com"; // Email inválido
        boolean resultado = validarEmail(emailInvalido);
        assertFalse(resultado, "Falha: Email inválido não deveria ser aceito.");
    }

    /**
     * Testa se uma data de nascimento inválida (data inexistente) é corretamente rejeitada.
     */
    @Test
    public void testDataNascimentoInvalida() {
        String dataNascimentoInvalida = "2023-02-30"; // Data inexistente
        boolean resultado = validarDataNascimento(dataNascimentoInvalida);
        assertFalse(resultado, "Falha: Data de nascimento inválida não deveria ser aceita.");
    }

    /**
     * Testa se uma data de nascimento que indica menor de idade é corretamente rejeitada.
     */
    @Test
    public void testDataNascimentoMenorIdade() {
        String dataNascimentoMenorIdade = LocalDate.now().minusYears(17).toString(); // 17 anos atrás
        boolean resultado = validarDataNascimento(dataNascimentoMenorIdade);
        assertFalse(resultado, "Falha: Data de nascimento de menor de idade não deveria ser aceita.");
    }

    /**
     * Valida o nome do usuário.
     * Um nome válido não pode ser nulo ou vazio.
     *
     * @param nome o nome do usuário a ser validado
     * @return true se o nome for válido, false caso contrário
     */
    private boolean validarNome(String nome) {
        return nome != null && !nome.trim().isEmpty();
    }

    /**
     * Valida o email do usuário.
     * Um email válido deve seguir um formato padrão (ex: "exemplo@dominio.com").
     *
     * @param email o email do usuário a ser validado
     * @return true se o email for válido, false caso contrário
     */
    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Valida a data de nascimento do usuário.
     * A data de nascimento deve estar no formato ISO (ex: "YYYY-MM-DD") e o usuário
     * deve ter pelo menos 18 anos de idade.
     *
     * @param dataNascimento a data de nascimento do usuário a ser validada
     * @return true se a data for válida e o usuário tiver mais de 18 anos, false caso contrário
     */
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
