package com.devsu.luis.pita.management.classes;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.devsu.luis.pita.management.controllers.ClienteController;
import com.devsu.luis.pita.management.models.ClienteModel;
import com.devsu.luis.pita.management.models.PersonaModel;
import com.devsu.luis.pita.management.servicies.ClienteService;
import com.devsu.luis.pita.management.servicies.PersonaService;

public class ClienteControllerTest {
    
    private MockMvc mockMvc;

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PersonaService personaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    public void testGetAllClientes() throws Exception {
        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setContrasenia("password");
        cliente.setEstado(true);
        cliente.setPersona(new PersonaModel());

        when(clienteService.findAll()).thenReturn(Collections.singletonList(cliente));

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].contrasenia").value("password"));
    }

    @Test
    public void testGetClienteById() throws Exception {
        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setContrasenia("password");
        cliente.setEstado(true);
        cliente.setPersona(new PersonaModel());

        when(clienteService.findById(anyLong())).thenReturn(Optional.of(cliente));

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contrasenia").value("password"));
    }

    @Test
    public void testCreateCliente() throws Exception {
        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setContrasenia("password");
        cliente.setEstado(true);
        PersonaModel persona = new PersonaModel();
        cliente.setPersona(persona);

        when(personaService.findById(anyLong())).thenReturn(Optional.of(persona));
        when(clienteService.save(any(ClienteModel.class))).thenReturn(cliente);
        when(personaService.save(any(PersonaModel.class))).thenReturn(persona);

        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType("application/json")
                .content("{\"contrasenia\":\"password\",\"estado\":true,\"persona\":{\"personaId\":1}}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contrasenia").value("password"));
    }

    @Test
    public void testUpdateCliente() throws Exception {
        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setContrasenia("password");
        cliente.setEstado(true);
        PersonaModel persona = new PersonaModel();
        cliente.setPersona(persona);

        when(clienteService.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(personaService.findById(anyLong())).thenReturn(Optional.of(persona));
        when(clienteService.save(any(ClienteModel.class))).thenReturn(cliente);
        when(personaService.save(any(PersonaModel.class))).thenReturn(persona);

        mockMvc.perform(MockMvcRequestBuilders.put("/clientes/1")
                .contentType("application/json")
                .content("{\"contrasenia\":\"newpassword\",\"estado\":false,\"persona\":{\"personaId\":1}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contrasenia").value("newpassword"));
    }

    @Test
    public void testDeleteCliente() throws Exception {
        ClienteModel cliente = new ClienteModel();
        cliente.setId(1L);
        cliente.setContrasenia("password");
        cliente.setEstado(true);
        PersonaModel persona = new PersonaModel();
        cliente.setPersona(persona);

        when(clienteService.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(personaService.findById(anyLong())).thenReturn(Optional.of(persona));

        mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
