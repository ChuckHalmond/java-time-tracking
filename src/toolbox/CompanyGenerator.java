package toolbox;

import java.time.LocalDate;
import java.time.LocalDateTime;

import company.Company;
import company.Department;
import company.Employee;
import company.Manager;
import time.CheckingContext;
import time.DateTimeService;
import toolbox.exceptions.CheckingException;
import toolbox.exceptions.CompanyStructureException;

/**
 * Generates a company with a few departments and staff, plus a some checkings.
 * 
 * @author Charles MECHERIKI
 * 
 */
public class CompanyGenerator {
	
	/**
	 * Generates a company with a few departments and staff, plus some checkings.
	 * 
	 * @return Company	the generated company
	 */
	public static Company generate() {
		LocalDateTime now = LocalDateTime.now(DateTimeService.clock);
		int nowDayOfWeekValue = now.getDayOfWeek().getValue();
		
		// Hiring date time (2 working days before now)
		LocalDateTime hiringDateTime = null;
		
		if (nowDayOfWeekValue > 5) {
			hiringDateTime = now.minusDays(nowDayOfWeekValue).plusDays(4);
		}
		else {
			if (nowDayOfWeekValue == 1) {
				hiringDateTime = now.minusDays(4);
			}
			else {
				hiringDateTime = now.minusDays(2);
			}
		}

		DateTimeService.setClock(hiringDateTime);	// Set the clock value for a correct company generation

		// Company
		Company williamPikardInc = new Company("WilliamPikardInc", "William", "Pikard");

		LocalDate hiringDate = hiringDateTime.toLocalDate();
		
		// Next working day
		LocalDate nextWorkingDay = (hiringDate.getDayOfWeek().getValue() < 5) ? hiringDate.plusDays(1) : hiringDate.plusDays(8 - hiringDate.getDayOfWeek().getValue());
		int nextWorkingDayIdx = nextWorkingDay.getDayOfWeek().getValue();
		
		// Next next working day
		LocalDate nextNextWorkingDay = (nextWorkingDayIdx < 5) ? nextWorkingDay.plusDays(1) : nextWorkingDay.plusDays(8 - nextWorkingDayIdx);
		int nextNextWorkingDayIdx = nextNextWorkingDay.getDayOfWeek().getValue();

		try {
			// Departments creation
			Department logisticDepartment = williamPikardInc.addNewDepartment("Logistic");
			Department qualityDepartment = williamPikardInc.addNewDepartment("Quality");
			Department humanRessourcesDepartment = williamPikardInc.addNewDepartment("Human Ressources");

			// Managers creation and assignment
			Manager louisFine = williamPikardInc.assignNewHeadManager("Louis", "Fine", logisticDepartment);
			Manager remiFasol = williamPikardInc.assignNewHeadManager("Remi", "Fasol", qualityDepartment);
			Manager sarahFraichit = williamPikardInc.assignNewManager("Sarah", "Fraichit", logisticDepartment);
			
			// Employees creation and assignment
			Employee jeanBon = williamPikardInc.assignNewEmployee("Jean", "Bon", logisticDepartment);
			Employee guyLigili  = williamPikardInc.assignNewEmployee("Guy", "Ligili", qualityDepartment);
			Employee anneIversaire =  williamPikardInc.assignNewEmployee("Anne", "Iversaire", humanRessourcesDepartment);

			// Simulation of a few checkings
			CheckingContext checkingContext = null;
			
			checkingContext = jeanBon.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jeanBon.getScheduleEntry(nextWorkingDayIdx)[0].plusMinutes(26)));
			williamPikardInc.addNewChecking(jeanBon, checkingContext);
			checkingContext = jeanBon.generateCheckingContext(LocalDateTime.of(nextWorkingDay, jeanBon.getScheduleEntry(nextWorkingDayIdx)[1].minusMinutes(6)));
			williamPikardInc.addNewChecking(jeanBon, checkingContext);
			checkingContext = jeanBon.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, jeanBon.getScheduleEntry(nextNextWorkingDayIdx)[0].plusMinutes(18)));
			williamPikardInc.addNewChecking(jeanBon, checkingContext);

			checkingContext = guyLigili.generateCheckingContext(LocalDateTime.of(nextWorkingDay, guyLigili.getScheduleEntry(nextWorkingDayIdx)[0].plusMinutes(33)));
			williamPikardInc.addNewChecking(guyLigili, checkingContext);
			checkingContext = guyLigili.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, guyLigili.getScheduleEntry(nextNextWorkingDayIdx)[0].plusMinutes(8)));
			williamPikardInc.addNewChecking(guyLigili, checkingContext);
			checkingContext = guyLigili.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, guyLigili.getScheduleEntry(nextNextWorkingDayIdx)[0].plusMinutes(24)));

			checkingContext = anneIversaire.generateCheckingContext(LocalDateTime.of(nextWorkingDay, anneIversaire.getScheduleEntry(nextWorkingDayIdx)[0].plusMinutes(8)));
			williamPikardInc.addNewChecking(anneIversaire, checkingContext);
			checkingContext = anneIversaire.generateCheckingContext(LocalDateTime.of(nextWorkingDay, anneIversaire.getScheduleEntry(nextWorkingDayIdx)[1].plusMinutes(6)));
			williamPikardInc.addNewChecking(anneIversaire, checkingContext);
			
			checkingContext = louisFine.generateCheckingContext(LocalDateTime.of(nextWorkingDay, louisFine.getScheduleEntry(nextWorkingDayIdx)[0].minusMinutes(14)));
			williamPikardInc.addNewChecking(louisFine, checkingContext);
			checkingContext = louisFine.generateCheckingContext(LocalDateTime.of(nextWorkingDay, louisFine.getScheduleEntry(nextWorkingDayIdx)[1].plusMinutes(56)));
			williamPikardInc.addNewChecking(louisFine, checkingContext);
			checkingContext = louisFine.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, louisFine.getScheduleEntry(nextNextWorkingDayIdx)[0].plusMinutes(24)));
			williamPikardInc.addNewChecking(louisFine, checkingContext);

			checkingContext = remiFasol.generateCheckingContext(LocalDateTime.of(nextWorkingDay, remiFasol.getScheduleEntry(nextWorkingDayIdx)[0].minusMinutes(16)));
			williamPikardInc.addNewChecking(remiFasol, checkingContext);
			checkingContext = remiFasol.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, remiFasol.getScheduleEntry(nextNextWorkingDayIdx)[1].plusMinutes(7)));
			williamPikardInc.addNewChecking(remiFasol, checkingContext);
			checkingContext = remiFasol.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, remiFasol.getScheduleEntry(nextNextWorkingDayIdx)[0].plusMinutes(6)));
			williamPikardInc.addNewChecking(remiFasol, checkingContext);

			checkingContext = sarahFraichit.generateCheckingContext(LocalDateTime.of(nextWorkingDay, sarahFraichit.getScheduleEntry(nextWorkingDayIdx)[0].minusMinutes(10)));
			williamPikardInc.addNewChecking(sarahFraichit, checkingContext);
			checkingContext = sarahFraichit.generateCheckingContext(LocalDateTime.of(nextWorkingDay, sarahFraichit.getScheduleEntry(nextWorkingDayIdx)[1].plusMinutes(6)));
			williamPikardInc.addNewChecking(sarahFraichit, checkingContext);
			checkingContext = sarahFraichit.generateCheckingContext(LocalDateTime.of(nextNextWorkingDay, sarahFraichit.getScheduleEntry(nextNextWorkingDayIdx)[0].minusMinutes(4)));
			williamPikardInc.addNewChecking(sarahFraichit, checkingContext);
			
			DateTimeService.setClock(now); 		// Set the clock back to its initial value
		}
		catch (CompanyStructureException _exception) {
			System.out.println("Unexpected CompanyStructureException on company generation : " + _exception.getMessage());
		}
		catch (CheckingException _exception) {
			_exception.printStackTrace();
			System.out.println("Unexpected CheckingException on company generation : " + _exception.getMessage());
		}

		return williamPikardInc;
	}
}
