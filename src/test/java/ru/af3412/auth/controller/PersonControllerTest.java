package ru.af3412.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.af3412.auth.AuthApplication;
import ru.af3412.auth.domain.Person;
import ru.af3412.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetPersonThenReturnListPerson() throws Exception {
        var person1 = new Person(1, "1", "1");
        var person2 = new Person(2, "2", "2");
        var person3 = new Person(3, "3", "3");
        when(personRepository.findAll()).thenReturn(List.of(person1, person2, person3));

        this.mockMvc.perform(get("/person"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void whenGetPersonByIdThenReturnPerson() throws Exception {
        var person2 = Optional.of(new Person(2, "2", "2"));
        when(personRepository.findById(2)).thenReturn(person2);

        this.mockMvc.perform(get("/person/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.login", is("2")))
                .andExpect(jsonPath("$.password", is("2")));
    }

    @Test
    void whenGetPersonByIdAndThisPersonNotFoundThenResponseNotFound() throws Exception {
        Optional<Person> person = Optional.empty();
        when(personRepository.findById(404)).thenReturn(person);

        this.mockMvc.perform(get("/person/404"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.login").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.password").value(IsNull.nullValue()));
    }

    @Test
    void whenCreateNewPersonThenReturnStatusCreated() throws Exception {
        Person person = new Person("Person", "pwd");
        when(personRepository.save(person)).thenReturn(new Person(1, "Person", "pwd"));

        this.mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(person)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.login", is("Person")))
                .andExpect(jsonPath("$.password", is("pwd")));

    }

    @Test
    void whenUpdatePersonThenReturnStatusOk() throws Exception {
        Person person = new Person("Person", "pwd");
        when(personRepository.save(person)).thenReturn(new Person(1, "Person", "pwd"));

        this.mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(person)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeletePersonThenReturnStatusOk() throws Exception {
        doNothing().when(personRepository).delete(new Person());

        this.mockMvc.perform(delete("/person/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}