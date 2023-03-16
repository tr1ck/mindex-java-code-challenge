package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    CompensationRepository compensationRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        // It wasn't abundantly clear to me what that effectiveDate was meant to represent.
        // Without the means to ask on the matter I operated under the assumption that it would
        // be when the compensation "went into effect" and assumed that at creation would be that time.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        compensation.setEffectiveDate(formatter.format(date));

        // Retrieve Employee to fill in Compensation object
        String employeeId = compensation.getEmployee().getEmployeeId();
        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if(employee != null) {
            compensation.setEmployee(employee);
        } else {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        return compensationRepository.insert(compensation);
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation for employee id: [{}]", id);

        // First get employee so we can retrieve compensation
        Employee employee = employeeRepository.findByEmployeeId(id);

        if(employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        // Retrieve compensation using employee
        Compensation compensation = compensationRepository.findCompensationByEmployee(employee);

        if (compensation == null) {
            throw new RuntimeException("No compensation linked to employeeId: " + id);
        }

        return compensation;
    }
}
