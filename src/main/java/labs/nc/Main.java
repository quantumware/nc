package labs.nc;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		NcDao ncDao = NcDaoJpa.INSTANCE;
		Employee John = new Employee("John");
		ncDao.addObject(John);
		Employee Lisa = new Employee("Lisa");
		ncDao.addObject(Lisa);
		Employee Jack = new Employee("Jack");
		ncDao.addObject(Jack);
		Employee James = new Employee("James");
		ncDao.addObject(James);
		Employee Leonard = new Employee("Leonard");
		ncDao.addObject(Leonard);
		Employee Lucy = new Employee("Lucy");
		ncDao.addObject(Lucy);
		Employee Sam = new Employee("Sam");
		ncDao.addObject(Sam);
		Employee Simon = new Employee("Simon");
		ncDao.addObject(Simon);
		
		ncDao.addObject(new Supervision(Lisa.getId(), John.getId()));
		ncDao.addObject(new Supervision(Lisa.getId(), Jack.getId()));
		ncDao.addObject(new Supervision(Leonard.getId(), James.getId()));
		ncDao.addObject(new Supervision(Leonard.getId(), Lucy.getId()));
		ncDao.addObject(new Supervision(Leonard.getId(), Sam.getId()));
		ncDao.addObject(new Supervision(Simon.getId(), Lisa.getId()));
		ncDao.addObject(new Supervision(Simon.getId(), Leonard.getId()));
		
		Project proA = new Project("Project A");
		ncDao.addObject(proA);
		Project proB = new Project("Project B");
		ncDao.addObject(proB);
		Project proC = new Project("Project C");
		ncDao.addObject(proC);
		
		ncDao.addObject(new EmployeeProject(John.getId(), proA.getId()));
		ncDao.addObject(new EmployeeProject(John.getId(), proB.getId()));
		ncDao.addObject(new EmployeeProject(Jack.getId(), proA.getId()));
		ncDao.addObject(new EmployeeProject(James.getId(), proA.getId()));
		ncDao.addObject(new EmployeeProject(James.getId(), proC.getId()));
		ncDao.addObject(new EmployeeProject(Lucy.getId(), proB.getId()));
		ncDao.addObject(new EmployeeProject(Lucy.getId(), proC.getId()));
		ncDao.addObject(new EmployeeProject(Sam.getId(), proB.getId()));
		
		long employeeId = ncDao.findFirstEmployeeId(James.getName());
		List<String> teammates = ncDao.findTeammates(employeeId);
		System.out.println(James.getName() + " is teammates of " + teammates);
		
		long superviseeId1 = ncDao.findFirstEmployeeId(John.getName());
		long superviseeId2 = ncDao.findFirstEmployeeId(Jack.getName());
		long supervisorId = ncDao.getFirstCommonManager(superviseeId1, superviseeId2);
		String supervisorName = ncDao.getEmployeeName(supervisorId);
		System.out.println("John and Jack's common manager is " + supervisorName);
		//TODO: graph
		System.out.println("John and Sam's common manager is " + ncDao.getEmployeeName(ncDao.getFirstCommonManager(ncDao.findFirstEmployeeId(John.getName()), ncDao.findFirstEmployeeId(Sam.getName()))));
		
		long managerId = ncDao.getClosestManager(proA.getId());
		String managerName = ncDao.getEmployeeName(managerId);
		System.out.println(proA.getName() + " is likely the responsibility of " + managerName + " as the majority of team members work for " + managerName);
	}
	
	private static void loadData() {
		
	}

}
