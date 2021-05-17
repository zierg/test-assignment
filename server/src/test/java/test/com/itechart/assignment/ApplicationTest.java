package test.com.itechart.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.assignment.model.EmployeeRequest;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.itechart.assignment.Application;
import com.itechart.assignment.model.Employee;
import com.itechart.assignment.model.EmployeeState;
import com.itechart.assignment.service.EmployeeService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeService employeeService;

    @Test
    @SneakyThrows
    public void whenPostEmployeeThenItIsCreatedWithAddedStatus() {
        var employee = new EmployeeRequest()
                .setFirstName("test-name")
                .setLastName("test-lastname")
                .setPassport("test-passport");

        mvc.perform(post("/employees")
                .content(toJson(employee))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(EmployeeState.ADDED.name()));
    }

    @Test
    @SneakyThrows
    public void whenStateIsUpdatedThenUpdateItOnEmployee() {
        Integer id = employeeService.addEmployee(new Employee()
                .setFirstName("name")
                .setLastName("lastname")
                .setPassport("passport"))
                .getId();

        mvc.perform(patch("/employees/{id}/state", id)
                .content(toJson(EmployeeState.IN_CHECK))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mvc.perform(get("/employees/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(EmployeeState.IN_CHECK.name()));
    }

    @Test
    @SneakyThrows
    public void whenEmployeeIsAbsentThenReturn404() {
        mvc.perform(patch("/employees/{id}/state", -1)
                .content(toJson(EmployeeState.IN_CHECK))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }


    @Test
    @SneakyThrows
    public void whenStateTransitionIsForbiddenThenReturn400AndStateIsNotUpdated() {
        Integer id = employeeService.addEmployee(new Employee()
                .setFirstName("name")
                .setLastName("lastname")
                .setPassport("passport"))
                .getId();

        mvc.perform(patch("/employees/{id}/state", id)
                .content(toJson(EmployeeState.IN_CHECK))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mvc.perform(patch("/employees/{id}/state", id)
                .content(toJson(EmployeeState.ADDED))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        mvc.perform(get("/employees/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(EmployeeState.IN_CHECK.name()));
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    private static String toJson(Object o) {
        return mapper.writeValueAsString(o);
    }
}
