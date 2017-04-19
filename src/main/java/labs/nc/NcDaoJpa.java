package labs.nc;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class NcDaoJpa implements NcDao {
	
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("labs-nc");
	private final EntityManager em = emf.createEntityManager();
	private final EntityTransaction tx = em.getTransaction();
	
	public static final NcDaoJpa INSTANCE = new NcDaoJpa();
	private NcDaoJpa() {}

	@Override
	public List<String> getTeammates(String employeeName) {
		Query query = em.createNativeQuery("SELECT ep1.EMPLOYEE_NAME FROM EMPLOYEE_PROJECT ep1 WHERE ep1.PROJECT_ID IN ("
				+ "SELECT ep2.PROJECT_ID FROM EMPLOYEE_PROJECT ep2 WHERE ep2.EMPLOYEE_NAME = ?) "
				+ "AND ep1.EMPLOYEE_ID <> ep2.EMPLOYEE_ID");
		query.setParameter("ep2.EMPLOYEE_NAME", employeeName);
		List<String> result = query.getResultList();
		return result;
	}

	@Override
	public long getFirstCommonManager(String superviseeId1, String superviseeId2) {
		Query query = em.createNativeQuery("SELECT s1.SUPERVISOR_ID FROM SUPERVISION s1, SUPERVISION s2 "
				+ "WHERE s1.SUPERVISOR_ID = s2.SUPERVISOR_ID AND s1.SUPERVISEE_ID = ? AND s2.SUPERVISEE_ID = ? LIMIT 1");
		query.setParameter("s1.SUPERVISEE_ID", superviseeId1);
		query.setParameter("s2.SUPERVISEE_ID", superviseeId2);
		long supervisorId = (Long) query.getSingleResult();
		return supervisorId;
	}

	@Override
	public long getClosestManager(long projectId) {
		Query query = em.createNativeQuery("SELECT s.SUPERVISOR_ID, COUNT(s.SUPERVISOR_ID) c FROM SUPERVISION s "
				+ "WHERE s.SUPERVISEE_ID IN ("
				+ "SELECT ep.EMPLOYEE_ID FROM EMPLOYEE_PROJECT ep WHERE ep.PROJECT_ID = ?) "
				+ "GROUP BY s.SUPERVISOR_ID ORDER BY c LIMIT 1");
		query.setParameter("ep.PROJECT_ID", projectId);
		query.getSingleResult();
		return 0;
	}

}
