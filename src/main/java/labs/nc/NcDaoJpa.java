package labs.nc;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NcDaoJpa implements NcDao {
	private static final Logger LOG = LoggerFactory.getLogger(NcDaoJpa.class);
	
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("labs-nc");
	private final EntityManager em = emf.createEntityManager();
	private final EntityTransaction tx = em.getTransaction();
	
	@Override
	public void addObject(Object e) {
		tx.begin();
		em.persist(e);
		tx.commit();
	}

	@Override
	public Employee getEmployeeByName(String name) {
		Query query = em.createNamedQuery("employeeByName");
		query.setParameter("name", name);
		return (Employee) query.setMaxResults(1).getSingleResult();
	}

	@Override
	public Project getProjectByName(String name) {
		Query query = em.createNamedQuery("projectByName");
		query.setParameter("name", name);
		return (Project) query.setMaxResults(1).getSingleResult();
	}

	@Override
	public List<String> findTeammates(long employeeId) {
		Query query = em.createNamedQuery("findTeammates");
		query.setParameter(1, employeeId);
		query.setParameter(2, employeeId);
		return (List<String>) query.getResultList();
	}

	@Override
	public Employee getCommonManager(long superviseeId1, long superviseeId2) {
		Query q1 = em.createNamedQuery("getManagers");
		q1.setParameter(1, superviseeId1);
		List<Object[]> managerIds1 = q1.getResultList();

		Query q2 = em.createNamedQuery("getManagers");
		q2.setParameter(1, superviseeId2);
		List<Object[]> managerIds2 = q2.getResultList();
		
		for (Object[] obj : managerIds1) {
			for (Object[] obj2 : managerIds2) {
				if ((obj[0]).equals(obj2[0])) {
					return getEmployeeById(new Long(obj[0].toString()));
				}
			}
		}
		return getEmployeeById(0);
	}

	@Override
	public Employee getClosestManager(long projectId) {
		Query query = em.createNamedQuery("getClosestManager");
		query.setParameter(1, projectId);
		Object[] obj = (Object[])query.getSingleResult();
		return getEmployeeById((Long) obj[0]);
	}

	private Employee getEmployeeById(long id) {
		return em.find(Employee.class, id);
	}

}
