package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public ReportingStructure read(String id) {

        LOG.debug("Reading reporting structure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
           throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);

        // I thought about the best way to get the number of reports and decided to go with
        // a recursive approach. Given that I didn't want to make the assumption that the
        // reporting tree would only be so deep, my thinking was this should be able to continuously
        // operate regardless of depth.
        int numberOfReports = findNumberOfReports(employee, true);

        reportingStructure.setNumberOfReports(numberOfReports);

        return reportingStructure;
    }

    // Recursive method
    private int findNumberOfReports(Employee employee, boolean isPopulated) {
        int numberOfReports = 0;

        if(!isPopulated) {
            employee = employeeRepository.findByEmployeeId(employee.getEmployeeId());
        }

        if (!CollectionUtils.isEmpty(employee.getDirectReports())) {
            numberOfReports += employee.getDirectReports().size();
            for(Employee underling : employee.getDirectReports()) {
                numberOfReports += findNumberOfReports(underling, false);
            }
        }

        return numberOfReports;
    }
}
