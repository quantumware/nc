package labs.nc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NcDaoJpaTest {
	
	private NcDao ncDao;
	
	@Before
	public void loadData() {
		ncDao = new NcDaoJpa();
		
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
	}

	@Test
	public void findTeammates() {
		String james = "James";
		long employeeId = ncDao.getEmployeeByName(james).getId();
		List<String> teammates = ncDao.findTeammates(employeeId);
		assertTrue(teammates.contains("John"));
		assertTrue(teammates.contains("Jack"));
		assertTrue(teammates.contains("Lucy"));
	}
	
	@Test
	public void findFirstEmployeeId() {
		assertEquals("Lisa", ncDao.getCommonManager(ncDao.getEmployeeByName("John").getId(), ncDao.getEmployeeByName("Jack").getId()).getName());
		assertEquals("Simon", ncDao.getCommonManager(ncDao.getEmployeeByName("John").getId(), ncDao.getEmployeeByName("Sam").getId()).getName());
	}
	
	@Test
	public void getClosestManager() {
		long projectId = ncDao.getProjectByName("Project A").getId();
		String managerName = ncDao.getClosestManager(projectId).getName();
		assertEquals("Lisa", managerName);
		
	}

}
