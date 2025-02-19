package conta_bancaria.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective.conta_bancaria.controller.TransacaoController;
import com.objective.conta_bancaria.model.Conta;
import com.objective.conta_bancaria.model.Transacao;
import com.objective.conta_bancaria.service.ContaService;


public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private TransacaoController transacaoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
    }

    @Test
    void testRealizarTransacao_Sucesso() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(50.00);
        transacao.setFormaPagamento("D"); // Débito

        Conta contaAtualizada = new Conta();
        contaAtualizada.setNumeroConta(123);
        contaAtualizada.setSaldo(450.00); // Saldo após transação

        when(contaService.realizarTransacao(any(Transacao.class))).thenReturn(contaAtualizada);

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero_conta").value(123))
                .andExpect(jsonPath("$.saldo").value(450.00));
    }

    @Test
    void testRealizarTransacao_SaldoInsuficiente() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(1000.00); // Valor maior que saldo disponível
        transacao.setFormaPagamento("D"); // Débito

        when(contaService.realizarTransacao(any(Transacao.class)))
                .thenThrow(new Exception("Saldo insuficiente"));

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente"));
    }

    @Test
    void testRealizarTransacao_ContaNaoEncontrada() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setNumeroConta(999); // Conta inexistente
        transacao.setValor(50.00);
        transacao.setFormaPagamento("C"); // Crédito

        when(contaService.realizarTransacao(any(Transacao.class)))
                .thenThrow(new Exception("Conta não encontrada"));

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Conta não encontrada"));
    }

    @Test
    void testRealizarTransacao_FormaPagamentoInvalida() throws Exception {
        Transacao transacao = new Transacao();
        transacao.setNumeroConta(123);
        transacao.setValor(50.00);
        transacao.setFormaPagamento("X"); // Forma inválida

        when(contaService.realizarTransacao(any(Transacao.class)))
                .thenThrow(new IllegalArgumentException("Forma de pagamento inválida: X"));

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Forma de pagamento inválida: X"));
    }
}