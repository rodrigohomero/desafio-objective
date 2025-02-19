package conta_bancaria.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective.conta_bancaria.controller.ContaController;
import com.objective.conta_bancaria.model.Conta;
import com.objective.conta_bancaria.service.ContaService;

public class ContaControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ContaController contaController;

    @Mock
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contaController).build();
    }

    @Test
    void testCriarConta_Sucesso() throws Exception {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(500.00);

        when(contaService.criarConta(any(Conta.class))).thenReturn(conta);

        mockMvc.perform(post("/conta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(conta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero_conta").value(123))
                .andExpect(jsonPath("$.saldo").value(500.00));
    }

    @Test
    void testConsultarConta_Sucesso() throws Exception {
        Conta conta = new Conta();
        conta.setNumeroConta(123);
        conta.setSaldo(500.00);

        when(contaService.buscarConta(123)).thenReturn(Optional.of(conta));

        mockMvc.perform(get("/conta?numero_conta=123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero_conta").value(123))
                .andExpect(jsonPath("$.saldo").value(500.00));
    }

    @Test
    void testConsultarConta_NaoEncontrada() throws Exception {
        when(contaService.buscarConta(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/conta?numero_conta=999"))
                .andExpect(status().isNotFound());
    }
}
