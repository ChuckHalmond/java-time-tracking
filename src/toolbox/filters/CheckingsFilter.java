package toolbox.filters;

import java.time.LocalDate;

import company.Department;
import company.Employee;
import time.Checking;

/**
 * A filter for the checkings, based on spatial (staff member, department) and temporal (period, date) criteria.
 *
 * @author Charles MECHERIKI
 *
 */
public class CheckingsFilter {
	private Employee staffMember;
	private Department department;
	private LocalDate fromDate;
	private LocalDate toDate;
	
	public CheckingsFilter(Employee _staffMember, Department _department, LocalDate _fromDate, LocalDate _toDate) {
		staffMember = _staffMember;
		department = _department;
		fromDate = _fromDate;
		toDate = _toDate;
	}
	
	public boolean accept(Checking _checking) {
        if (staffMember.equals(Employee.ALL_STAFF_PLACEHOLDER)) {
	        if (department.equals(Department.ALL_DEPARTMENTS_PLACEHOLDER)) {
				if (toDate.equals(fromDate)) {
					return _checking.isOn(fromDate);
				}
				else {
					return _checking.isBetween(fromDate, toDate);
				}
	        }
	        else {
	        	if (toDate.equals(fromDate)) {
	        		return _checking.isOn(fromDate) && _checking.getStaffMember().getDepartment().equals(department);
				}
	        	else {
	        		return _checking.isBetween(fromDate, toDate) && _checking.getStaffMember().getDepartment().equals(department);
				}
	        }
        }
        else {
        	if (toDate.equals(fromDate)) {
        		return _checking.isOn(fromDate) && _checking.getStaffMember().equals(staffMember);
			}
			else {
				return _checking.isBetween(fromDate, toDate) && _checking.getStaffMember().equals(staffMember);
			}
        }
	}
}
