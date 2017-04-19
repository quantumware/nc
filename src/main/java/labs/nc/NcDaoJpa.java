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
	
	public static final NcDaoJpa INSTANCE = new NcDaoJpa();
	private NcDaoJpa() {}

	@Override
	public void addObject(Object e) {
		tx.begin();
		em.persist(e);
		tx.commit();
	}

	@Override
	public String getEmployeeName(long employeeId) {
		Query query = em.createNamedQuery("getEmployeeName");
		query.setParameter(1, employeeId);
		List<String> employeeIds = (List<String>)query.getResultList();
		String name = "";
		if (!employeeIds.isEmpty()) {
			name = employeeIds.get(0);
		}
		return name;
	}

	@Override
	public long findFirstEmployeeId(String employeeName) {
		Query query = em.createNamedQuery("findFirstEmployeeIdByName");
		query.setParameter(1, employeeName);
		List<Long> employeeIds = (List<Long>)query.getResultList();
		long employeeId = 0;
		if (!employeeIds.isEmpty()) {
			employeeId = employeeIds.get(0);
		}
		return employeeId;
	}

	@Override
	public List<String> findTeammates(long employeeId) {
		Query query = em.createNamedQuery("findTeammates");
		query.setParameter(1, employeeId);
		query.setParameter(2, employeeId);
		List<String> result = query.getResultList();
		return result;
	}

	@Override
	public long getFirstCommonManager(long superviseeId1, long superviseeId2) {
		Query query = em.createNativeQuery("SELECT s1.SUPERVISOR_ID FROM SUPERVISION s1, SUPERVISION s2 "
				+ "WHERE s1.SUPERVISOR_ID = s2.SUPERVISOR_ID AND s1.SUPERVISEE_ID = ?1 AND s2.SUPERVISEE_ID = ?2 LIMIT 1");
		query.setParameter(1, superviseeId1);
		query.setParameter(2, superviseeId2);
		List<Long> employeeIds = (List<Long>)query.getResultList();
		long supervisorId = 0;
		if (!employeeIds.isEmpty()) {
			supervisorId = employeeIds.get(0);
		}
		return supervisorId;
	}
	
	public long getCommonManager(long superviseeId1, long superviseeId2) {
//		LOG.info(superviseeId1 + ", " + superviseeId2);
		Query q1 = em.createNativeQuery("WITH Nodes(SUPERVISEE_ID, SUPERVISOR_ID, Depth) AS("
				+ "	SELECT SUPERVISEE_ID, SUPERVISOR_ID, 0 AS DEPTH "
				+ "	FROM SUPERVISION WHERE SUPERVISEE_ID = " + superviseeId1
				+ "	UNION ALL "
				+ "	SELECT pc.SUPERVISEE_ID, pc.SUPERVISOR_ID, n.Depth - 1 "
				+ "	FROM SUPERVISION pc "
				+ "	JOIN Nodes n ON pc.SUPERVISEE_ID = n.SUPERVISOR_ID "
				+ ")"
				+ "SELECT n.SUPERVISOR_ID, COUNT(n.SUPERVISOR_ID) FROM Nodes n "
				+ "GROUP BY n.SUPERVISOR_ID");
//		q1.setParameter(1, superviseeId1);
		List<Object[]> superviseeIds1 = q1.getResultList();
//		LOG.info("superviseeIds1:" + superviseeIds1);
		
		Query q2 = em.createNativeQuery("WITH Nodes(SUPERVISEE_ID, SUPERVISOR_ID, Depth) AS("
				+ "	SELECT SUPERVISEE_ID, SUPERVISOR_ID, 0 AS DEPTH "
				+ "	FROM SUPERVISION WHERE SUPERVISEE_ID = " + superviseeId2
				+ "	UNION ALL "
				+ "	SELECT pc.SUPERVISEE_ID, pc.SUPERVISOR_ID, n.Depth - 1 "
				+ "	FROM SUPERVISION pc "
				+ "	JOIN Nodes n ON pc.SUPERVISEE_ID = n.SUPERVISOR_ID "
				+ ")"
				+ "SELECT n.SUPERVISOR_ID, COUNT(n.SUPERVISOR_ID) FROM Nodes n "
				+ "GROUP BY n.SUPERVISOR_ID");
//		q2.setParameter(1, superviseeId2);
		List<Object[]> superviseeIds2 = q2.getResultList();
//		LOG.info("superviseeIds2:" + superviseeIds2);
		
		long supervisorId = 0;
		for (Object[] obj : superviseeIds1) {
//			LOG.info(obj[0] + ":" + obj[1]);
			for (Object[] obj2 : superviseeIds2) {
//				LOG.info("obj2->" + obj2[0] + ":" + obj2[1]);
				if ((obj[0]).equals(obj2[0])) {
//					LOG.info(obj[0] + ", " + obj2[0]);
					supervisorId = new Long(obj[0].toString());
//					break;
					return supervisorId;
				}
			}
//			if (superviseeIds2.contains(obj)) {
//				supervisorId = (Long) obj[0];
//				break;
//			}
		}
//		superviseeIds1.retainAll(superviseeIds2);
//		LOG.info("" + superviseeIds1);
//		LOG.info("supervisorId:" + supervisorId);
		return supervisorId;
	}

	@Override
	public long getClosestManager(long projectId) {
		Query query = em.createNativeQuery("SELECT s.SUPERVISOR_ID, COUNT(s.SUPERVISOR_ID) c FROM SUPERVISION s "
				+ "WHERE s.SUPERVISEE_ID IN ("
				+ "SELECT ep.EMPLOYEE_ID FROM EMPLOYEE_PROJECT ep WHERE ep.PROJECT_ID = ?1) "
				+ "GROUP BY s.SUPERVISOR_ID ORDER BY c DESC LIMIT 1");
		query.setParameter(1, projectId);
		Object[] objs = (Object[])query.getSingleResult();
		LOG.debug(objs[0] + ":" + objs[1]);
		return (Long) objs[0];
	}

}
