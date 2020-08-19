package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import toolbox.exceptions.CompanyStructureException;
import company.Company;
import company.Department;
import company.Employee;
import company.Manager;

/**
 * A few tests on the company components : departments, managers and employees
 * The goal is to attest the coherence of the structures' behavior with the initial instructions
 * 
 * @author Charles MECHERIKI
 * 
 */
public class CompanyTest {
	private Company williamPikardInc;
	
	private Department logisticDepartment;
	private Department qualityDepartment;
	
	private Manager louis;
	private Manager sarah;
	private Manager remi;

	private Employee anne;
	
	/**
	 * Initializes the environment for the different tests
	 * 
	 * @throws CompanyStructureException an attempted action was illegal
	 */
    @Before
    public void initialization() throws CompanyStructureException {
    	try {
			williamPikardInc = new Company("WilliamPikardInc", "William", "Pikard");

			logisticDepartment = williamPikardInc.addNewDepartment("Logistic");
			qualityDepartment = williamPikardInc.addNewDepartment("Quality");
			
			williamPikardInc.assignNewEmployee("Jean", "Bon", qualityDepartment);
			anne = williamPikardInc.assignNewEmployee("Anne", "Iversaire", qualityDepartment);
			williamPikardInc.assignNewEmployee("Guy", "Ligili", logisticDepartment);
			
			louis = williamPikardInc.assignNewHeadManager("Louis", "Fine", logisticDepartment);
			sarah = williamPikardInc.assignNewHeadManager("Sarah", "Fraîchit", qualityDepartment);
			remi = williamPikardInc.assignNewManager("Rémi", "Fasollasido", logisticDepartment);
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }

    /**
     * Add a department to the company which already exists : impossible, a CompanyStructureException is thrown
     */
  	@Test
	public void testAddExistingDepartment_companyStructureExceptionThrown() {
  		try {
			williamPikardInc.addNewDepartment("Logistic");
			
			Assert.assertTrue(false);
		}
  		catch (CompanyStructureException e) {
			Assert.assertTrue(true);
		}
	}
  	
    /**
     * Assign a manager as head manager of another department : this manager should no longer be part of his ex department, and should 
     * replace the current head manager of the department
     */
  	@Test
	public void testAssignManagerAsHeadManager_replacesCurrentManager() {
  		try {
			williamPikardInc.assignHeadManager(remi, logisticDepartment);
			
	        Assert.assertTrue(
        		!louis.isHead()
        		&& logisticDepartment.containsManager(louis)
        		&& logisticDepartment.isManagedBy(remi)
        		&& logisticDepartment.containsManager(remi)
            );
		}
  		catch (CompanyStructureException e) {
			Assert.assertTrue(false);
		}
	}
  	
    /**
     * Assign an head manager as head manager of another department : his new assignment prioritize on his current position,
     * he will replace the current department active manager and leave his current position
     */
  	@Test
	public void testAssignHeadManagerAsHeadManager_replacesCurrentManager() {
  		try {
			williamPikardInc.assignHeadManager(sarah, logisticDepartment);
			
	        Assert.assertTrue(
	        	!louis.isHead()
	        	&& logisticDepartment.isManagedBy(sarah)
	        	&& qualityDepartment.isManagedBy(null)
	        );
		}
  		catch (CompanyStructureException e) {
  			System.out.println(e.getMessage());
			Assert.assertTrue(false);
		}
	}
  	
    /**
     * Promote an employee to manager : he is now a manager (in the company and his department)
     * he will replace the current department active manager and leave his current position
     */
  	@Test
	public void testPromoteEmployee_employeeIsNowManager() {
  		try {
			Manager annieManager = williamPikardInc.promoteEmployee(anne);
			
	        Assert.assertTrue(
	        	annieManager.isWorkingIn(qualityDepartment)
	        	&& !qualityDepartment.containsEmployee(anne)
	        	&& !williamPikardInc.containsEmployee(anne)
	        	&& qualityDepartment.containsManager(annieManager)
	        );
		}
  		catch (CompanyStructureException e) {
  			System.out.println(e.getMessage());
			Assert.assertTrue(false);
		}
	}
  	
    /**
     * Assign an head manager as an employee of another department : impossible, CompanyStructureException thrown
     */
  	@Test
	public void testAssignHeadManagerAsEmployee_companyStructureExceptionThrown() {
  		try {
			williamPikardInc.assignManager(louis, logisticDepartment);
			
	        Assert.assertTrue(false);
		}
  		catch (CompanyStructureException e) {
			Assert.assertTrue(true);
		}
	}
  	
    /**
     * Demote a manager : he is now an employee
     */
  	@Test
	public void testDemoteManager_managerIsNowEmployee() {
  		try {
			Employee remiEmployee = williamPikardInc.demoteManager(remi);
			
	        Assert.assertTrue(
	        		remiEmployee.isWorkingIn(logisticDepartment)
	        	&& logisticDepartment.containsEmployee(remiEmployee)
	        	&& !williamPikardInc.containsManager(remi)
	        	&& !logisticDepartment.containsManager(remi)
	        );
		}
  		catch (CompanyStructureException e) {
  			System.out.println(e.getMessage());
			Assert.assertTrue(false);
		}
	}
  	
    /**
     * Demote a head manager : impossible, CompanyStructureException thrown
     */
  	@Test
	public void testDemoteHeadManager_companyStructureExceptionThrown() {
  		try {
  			williamPikardInc.demoteManager(louis);
	        Assert.assertTrue(false);
		}
  		catch (CompanyStructureException e) {
			Assert.assertTrue(true);
		}
	}
  	
    /**
     * Dismiss an employee from the company : this employee is no longer in his department and the company
     */
  	@Test
	public void testDismissEmployeeFromCompany_employeeNoLongerInCompany() {
  		
  		try {
			williamPikardInc.dismissEmployee(anne);
			
			Assert.assertTrue(
				!williamPikardInc.containsEmployee(anne)
				&& !qualityDepartment.containsEmployee(anne)
			);
  		}
  		catch (CompanyStructureException e) {
  			Assert.assertTrue(false);
		} 
	}
  	
    /**
     * Dismiss a head manager : this is not allowed, a CompanyStructureException is thrown
     */
  	@Test
	public void testDismissHeadManagerFromCompany_companyStructureExceptionThrown() {
  		
  		try {
			williamPikardInc.dismissManager(louis);
			
			Assert.assertTrue(false);
  		}
  		catch (CompanyStructureException e) {
  			Assert.assertTrue(true);
		} 
	}
  	
    /**
     * Dismiss an employee who is already dismissed : impossible, a CompanyStructureException is thrown
     */
  	@Test
	public void testDismissEmployeeAlreadyDismissed_companyStructureExceptionThrown() {
  		try {
  	  		williamPikardInc.dismissManager(sarah);
  	  		williamPikardInc.dismissManager(sarah);
  	  		
  	  		Assert.assertTrue(false);
  		}
  		catch (CompanyStructureException e) {
  			Assert.assertTrue(true);
  		}
  	}
}
