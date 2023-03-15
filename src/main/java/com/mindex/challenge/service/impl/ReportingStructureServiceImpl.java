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

        int numberOfReports = recursiveSomethingOrOther(employee, true);

        reportingStructure.setNumberOfReports(numberOfReports);

        return reportingStructure;
    }

    private int recursiveSomethingOrOther(Employee employee, boolean isPopulated) {
        int numberOfReports = 0;

        if(!isPopulated) {
            employee = employeeRepository.findByEmployeeId(employee.getEmployeeId());
        }

        if (!CollectionUtils.isEmpty(employee.getDirectReports())) {
            numberOfReports += employee.getDirectReports().size();
            for(Employee underling : employee.getDirectReports()) {
                numberOfReports += recursiveSomethingOrOther(underling, false);
            }
        }

        return numberOfReports;
    }
}
