package com.mindex.challenge.service.impl;


import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testReadWithMultipleDirectReports() {
        ReportingStructure testReportingStructure = new ReportingStructure();
        Employee testEmployee = new Employee();

        testEmployee.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        testReportingStructure.setEmployee(testEmployee);
        testReportingStructure.setNumberOfReports(4);

        ReportingStructure retrievedReportingStructure = restTemplate.getForEntity(
                reportingStructureIdUrl,
                ReportingStructure.class,
                testReportingStructure.getEmployee().getEmployeeId())
                .getBody();

        assertNotNull(retrievedReportingStructure.getEmployee().getEmployeeId());
        assertReportingStructureEquivalence(testReportingStructure, retrievedReportingStructure);
    }

    @Test
    public void testReadWithNoDirectReports() {
        ReportingStructure testReportingStructure = new ReportingStructure();
        Employee testEmployee = new Employee();

        testEmployee.setEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");
        testReportingStructure.setEmployee(testEmployee);
        testReportingStructure.setNumberOfReports(0);

        ReportingStructure retrievedReportingStructure = restTemplate.getForEntity(
                        reportingStructureIdUrl,
                        ReportingStructure.class,
                        testReportingStructure.getEmployee().getEmployeeId())
                .getBody();

        assertNotNull(retrievedReportingStructure.getEmployee().getEmployeeId());
        assertReportingStructureEquivalence(testReportingStructure, retrievedReportingStructure);
    }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
