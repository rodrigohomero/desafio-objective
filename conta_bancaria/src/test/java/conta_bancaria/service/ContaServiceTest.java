package conta_bancaria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.objective.conta_bancaria.model.Conta;
import com.objective.conta_bancaria.model.Transacao;
import com.objective.conta_bancaria.repository.ContaRepository;
import com.objective.conta_bancaria.service.ContaService;

public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarConta_Sucesso() throws Exception {
        Conta novaConta = new Conta();
        novaConta.setNumeroConta(123);
        novaConta.setSaldo(500.00);

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.empty());
        when(contaRepository.save(novaConta)).thenReturn(novaConta);

        Conta contaCriada = contaService.criarConta(novaConta);

        assertNotNull(contaCriada);
        assertEquals(123, contaCriada.getNumeroConta());
        assertEquals(500.00, contaCriada.getSaldo());
    }

    @Test
    void testCriarConta_ContaJaExiste() {
        Conta contaExistente = new Conta();
        contaExistente.setNumeroConta(123);

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(contaExistente));

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.criarConta(contaExistente);
        });

        assertEquals("Conta já existe", exception.getMessage());
    }
    
    
    @Test
    void testRealizarTransacao_SucessoDebito() throws Exception {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(100.00);

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(10.00);
        transacao.setFormaPagamento("D"); // Débito

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Conta contaAtualizada = contaService.realizarTransacao(transacao);

        assertNotNull(contaAtualizada);
        assertEquals(89.70, contaAtualizada.getSaldo()); // 10.00 + 3% de taxa
    }

    @Test
    void testRealizarTransacao_SucessoCredito() throws Exception {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(200.00);

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(20.00);
        transacao.setFormaPagamento("C"); // Crédito

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Conta contaAtualizada = contaService.realizarTransacao(transacao);

        assertNotNull(contaAtualizada);
        assertEquals(179.00, contaAtualizada.getSaldo()); // 20.00 + 5% de taxa
    }

    @Test
    void testRealizarTransacao_SucessoPix() throws Exception {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(100.00);

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(30.00);
        transacao.setFormaPagamento("P"); // Pix (Sem taxa)

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Conta contaAtualizada = contaService.realizarTransacao(transacao);

        assertNotNull(contaAtualizada);
        assertEquals(70.00, contaAtualizada.getSaldo()); // Apenas desconta o valor
    }

    @Test
    void testRealizarTransacao_SaldoInsuficienteDebito() {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(10.00);

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(10.00);
        transacao.setFormaPagamento("D"); // Débito

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.realizarTransacao(transacao);
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
    }
    
    @Test
    void testRealizarTransacao_SaldoInsuficientePix() {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(5.00); // Saldo menor que o valor da transação

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(10.00); // Valor maior que o saldo
        transacao.setFormaPagamento("P"); // Pix

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.realizarTransacao(transacao);
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
    }


    @Test
    void testRealizarTransacao_SaldoInsuficienteCredito() {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(5.00);

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(5.00);
        transacao.setFormaPagamento("C"); // Crédito

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.realizarTransacao(transacao);
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    void testRealizarTransacao_ContaNaoEncontrada() {
        Transacao transacao = new Transacao();
        transacao.setNumeroConta(999); // Conta inexistente
        transacao.setValor(50.00);
        transacao.setFormaPagamento("D");

        when(contaRepository.findByNumeroConta(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.realizarTransacao(transacao);
        });

        assertEquals("Conta não encontrada", exception.getMessage());
    }

    @Test
    void testRealizarTransacao_FormaPagamentoInvalida() {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(100.00);

        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(10.00);
        transacao.setFormaPagamento("X"); // Forma inválida

        when(contaRepository.findByNumeroConta(123)).thenReturn(Optional.of(conta));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.realizarTransacao(transacao);
        });

        assertEquals("Forma de pagamento inválida: X", exception.getMessage());
    }

}