package com.example.employee;
import com.example.employee.controller.EmployeeController;
import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EmployeeService employeeService;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testGetAllEmployees() throws Exception {
    when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/employees"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void testSearchEmployees() throws Exception {
    Employee employee = new Employee();
    employee.setId(1L);
    employee.setName("John");
    employee.setEmail("john@example.com");
    employee.setDepartment("HR");

    when(employeeService.searchEmployees("John")).thenReturn(Collections.singletonList(employee));

    mockMvc.perform(get("/api/employees?search=John"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  public void testSearchEmployees_NoResults() throws Exception {
    when(employeeService.searchEmployees("Unknown")).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/employees?search=Unknown"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void testAddEmployee() throws Exception {
    Employee employee = new Employee();
    employee.setId(1L);
    employee.setName("John Doe");
    employee.setEmail("john@example.com");
    employee.setDepartment("HR");

    when(employeeService.saveEmployee(ArgumentMatchers.any())).thenReturn(employee);

    mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee))) // Serialize employee object to JSON
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.email").value("john@example.com"))
        .andExpect(jsonPath("$.department").value("HR"));
  }

  @Test
  public void testUpdateEmployee() throws Exception {
    Employee existingEmployee = new Employee();
    existingEmployee.setId(1L);
    existingEmployee.setName("Jane Smith");
    existingEmployee.setEmail("jane@example.com");
    existingEmployee.setDepartment("IT");

    Employee updatedEmployee = new Employee();
    updatedEmployee.setId(1L);
    updatedEmployee.setName("Updated Name");
    updatedEmployee.setEmail("updated@example.com");
    updatedEmployee.setDepartment("Updated Department");

    when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(existingEmployee));
    when(employeeService.saveEmployee(ArgumentMatchers.any())).thenReturn(updatedEmployee);

    mockMvc.perform(put("/api/employees/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.email").value("updated@example.com"))
        .andExpect(jsonPath("$.department").value("Updated Department"));
  }

  @Test
  public void testDeleteEmployee() throws Exception {
    mockMvc.perform(delete("/api/employees/1"))
        .andExpect(status().isNoContent());
  }
}

