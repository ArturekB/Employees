package com.example.employees;

import com.example.employees.domain.Salary;
import com.example.employees.domain.dto.EmployeeRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeesApplicationTests {
	
	private static final String PASSED_ID = "testId";
	private static final String TEST_NAME = "testName";
	private static final String TEST_SURNAME = "testSurname";
	private static final String TEST_GRADE = "testGrade";
	private static final String TEST_CURRENCY = "PLN";
	private static final Double TEST_SALARY = 1243.32;

	@LocalServerPort
	private int port;

	private String baseUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	@BeforeEach
	public void setUp() {
		baseUrl = String.format("http://localhost:%d/employees", port);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void shouldCreateEmployee() {
		var employeeRepresentation = prepareTestEmployee();
		employeeRepresentation.setId(PASSED_ID);

		var result = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);

		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		var representation = result.getBody();
		assertThat(representation).isNotNull();
		assertThat(representation.getId()).isNotEmpty();
		assertThat(representation.getId()).isNotEqualTo(PASSED_ID);
		assertThat(representation.getName()).isEqualTo(TEST_NAME);
		assertThat(representation.getSurname()).isEqualTo(TEST_SURNAME);
		assertThat(representation.getGrade()).isEqualTo(TEST_GRADE);
	}

	@Test
	void shouldGetEmployee() {
		var employeeRepresentation = prepareTestEmployee();

		var postResult = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);
		var id = postResult.getBody().getId();

		var result = restTemplate.getForEntity(baseUrl + "/" + id, EmployeeRepresentation.class);
		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		var representation = result.getBody();
		assertThat(representation).isNotNull();
		assertThat(representation.getId()).isNotEmpty();
		assertThat(representation.getId()).isEqualTo(id);
		assertThat(representation.getName()).isEqualTo(TEST_NAME);
		assertThat(representation.getSurname()).isEqualTo(TEST_SURNAME);
		assertThat(representation.getGrade()).isEqualTo(TEST_GRADE);
	}

	@Test
	void shouldUpdateEmployee() {
		var salary = new Salary(BigDecimal.valueOf(TEST_SALARY), TEST_CURRENCY);
		var employeeRepresentation = prepareTestEmployee();

		var createResult = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);

		var id= createResult.getBody().getId();
		employeeRepresentation.setId(id);
		employeeRepresentation.setSalary(salary);

		restTemplate.put(baseUrl+"/"+id, employeeRepresentation);
		var result = restTemplate.getForEntity(baseUrl + "/" + id, EmployeeRepresentation.class);

		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		var representation = result.getBody();
		assertThat(representation).isNotNull();
		assertThat(representation.getId()).isNotEmpty();
		assertThat(representation.getId()).isEqualTo(id);
		assertThat(representation.getName()).isEqualTo(TEST_NAME);
		assertThat(representation.getSurname()).isEqualTo(TEST_SURNAME);
		assertThat(representation.getGrade()).isEqualTo(TEST_GRADE);
		assertThat(representation.getSalary().getCurrency()).isEqualTo(TEST_CURRENCY);
		assertThat(representation.getSalary().getAmount()).isEqualTo(BigDecimal.valueOf(TEST_SALARY));
	}

	@Test
	void shouldGetListOfEmployees() {
		var employeeRepresentation = prepareTestEmployee();

		var postResult = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);
		var firstId = postResult.getBody().getId();

		var secondPostResult = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);
		var secondId = postResult.getBody().getId();

		var result = restTemplate.getForEntity(baseUrl, EmployeeRepresentation[].class);
		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotEmpty();
		assertThat(result.getBody()).hasSize(2);
	}

	@Test
	void shouldRemoveEmployee() {
		var employeeRepresentation = prepareTestEmployee();

		var postResult = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);
		var firstId = postResult.getBody().getId();

		postResult = restTemplate.postForEntity(baseUrl, employeeRepresentation, EmployeeRepresentation.class);
		var secondId = postResult.getBody().getId();

		restTemplate.delete(baseUrl+"/"+firstId);
		var result = restTemplate.getForEntity(baseUrl, EmployeeRepresentation[].class);

		assertThat(result).isNotNull();
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotEmpty();
		assertThat(result.getBody()).hasSize(1);
		assertThat(result.getBody()[0].getId()).isEqualTo(secondId);
	}

	private EmployeeRepresentation prepareTestEmployee() {
		return EmployeeRepresentation.builder()
				.name(TEST_NAME)
				.surname(TEST_SURNAME)
				.grade(TEST_GRADE)
				.build();
	}
}
