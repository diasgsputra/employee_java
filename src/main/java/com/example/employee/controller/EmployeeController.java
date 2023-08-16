package com.example.employee.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/employees")
public class EmployeeController {
  private final EmployeeService employeeService;

  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public List<Employee> getAllEmployees() {
    return employeeService.getAllEmployees();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    return employeeService.getEmployeeById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/search")
  public List<Employee> getEmployeesByName(@RequestParam(name = "search", required = false) String search) {
    if (search != null && !search.isEmpty()) {
      return employeeService.searchEmployees(search);
    } else {
      return employeeService.getAllEmployees();
    }
  }


  @PostMapping
  public Employee addEmployee(@RequestBody Employee employee) {
    return employeeService.saveEmployee(employee);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
    return employeeService.getEmployeeById(id)
        .map(employee -> {
          employee.setName(employeeDetails.getName());
          employee.setEmail(employeeDetails.getEmail());
          employee.setDepartment(employeeDetails.getDepartment());
          return ResponseEntity.ok(employeeService.saveEmployee(employee));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }
}
