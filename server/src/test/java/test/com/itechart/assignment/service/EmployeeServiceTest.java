package test.com.itechart.assignment.service;

import com.itechart.assignment.service.exceptions.EmployeeNotFoundException;
import com.itechart.assignment.service.exceptions.StateTransitionNotAllowedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.itechart.assignment.Application;
import com.itechart.assignment.model.Employee;
import com.itechart.assignment.model.EmployeeState;
import com.itechart.assignment.repositories.EmployeeRepository;
import com.itechart.assignment.service.EmployeeService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureTestDatabase
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService tested;

    @Autowired
    private EmployeeRepository repository;

    @Test
    public void whenEmployeeIsAddedThenItHasAddedStatus() {
        // given
        var employee = new Employee()
                .setFirstName("name")
                .setLastName("lastname")
                .setPassport("passport")
                .setState(EmployeeState.ACTIVE);

        // when
        Employee actual = tested.addEmployee(employee);
        Employee expected = new Employee()
                .setId(actual.getId())
                .setFirstName("name")
                .setLastName("lastname")
                .setPassport("passport")
                .setState(EmployeeState.ADDED);

        // then
        Optional<Employee> found = repository.findById(actual.getId());
        assertThat(found.isPresent()).isEqualTo(true);
        //noinspection OptionalGetWithoutIsPresent
        assertThat(found.get()).isEqualTo(expected);
    }

    @Test
    public void whenStateIsChangedThenItIsUpdatedOnEntity() {
        // given
        var employee = new Employee()
                .setFirstName("name")
                .setLastName("lastname")
                .setPassport("passport")
                .setState(EmployeeState.ADDED);
        Integer id = repository.save(employee).getId();

        // when
        tested.updateState(id, EmployeeState.IN_CHECK);

        // then
        Optional<Employee> found = repository.findById(id);
        //noinspection OptionalGetWithoutIsPresent
        assertThat(found.get().getState()).isEqualTo(EmployeeState.IN_CHECK);
    }

    @Test
    public void whenEmployeeIsAbsentDuringStateUpdateThenThrowException() {
        // when
        var ex = Assert.assertThrows(EmployeeNotFoundException.class
                , () -> tested.updateState(-1, EmployeeState.IN_CHECK));

        // then
        assertThat(ex.getMessage()).isEqualTo("Employee with id -1 was not found");
    }

    @Test
    public void whenUpdateToAddedFromOtherStateThrowException() {
        // given
        var employee = new Employee()
                .setFirstName("name")
                .setLastName("lastname")
                .setPassport("passport")
                .setState(EmployeeState.IN_CHECK);
        Integer id = repository.save(employee).getId();

        // when
        var ex = Assert.assertThrows(StateTransitionNotAllowedException.class,
                () -> tested.updateState(id, EmployeeState.ADDED));

        // then
        assertThat(ex.getMessage()).isEqualTo("Transition to state ADDED is not allowed");
    }
}
