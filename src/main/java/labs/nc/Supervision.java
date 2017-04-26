package labs.nc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="SUPERVISION")
@IdClass(SupervisionId.class)
@NamedNativeQueries(value={
	@NamedNativeQuery(name="getManagers", query="WITH Nodes(SUPERVISEE_ID, SUPERVISOR_ID, Depth) AS("
				+ "	SELECT SUPERVISEE_ID, SUPERVISOR_ID, 0 AS DEPTH "
				+ "	FROM SUPERVISION WHERE SUPERVISEE_ID = ?1 "
				+ "	UNION ALL "
				+ "	SELECT pc.SUPERVISEE_ID, pc.SUPERVISOR_ID, n.Depth - 1 "
				+ "	FROM SUPERVISION pc "
				+ "	JOIN Nodes n ON pc.SUPERVISEE_ID = n.SUPERVISOR_ID "
				+ ")"
				+ "SELECT n.SUPERVISOR_ID, COUNT(n.SUPERVISOR_ID) FROM Nodes n "
				+ "GROUP BY n.SUPERVISOR_ID"),
	@NamedNativeQuery(name="getClosestManager", query="SELECT s.SUPERVISOR_ID, COUNT(s.SUPERVISOR_ID) c FROM SUPERVISION s "
				+ "WHERE s.SUPERVISEE_ID IN ("
				+ "SELECT ep.EMPLOYEE_ID FROM EMPLOYEE_PROJECT ep WHERE ep.PROJECT_ID = ?1) "
				+ "GROUP BY s.SUPERVISOR_ID ORDER BY c DESC LIMIT 1")
})
public class Supervision {
	@Id
	@Column(name="SUPERVISOR_ID")
	private long supervisorId;
	@Id
	@Column(name="SUPERVISEE_ID")
	private long superviseeId;

	@ManyToOne
	@PrimaryKeyJoinColumn(name="SUPERVISOR_ID", referencedColumnName="ID")
	private Employee supervisor;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="SUPERVISEE_ID", referencedColumnName="ID")
	private Employee supervisee;
	
	public Supervision() {}
	public Supervision(long supervisorId, long superviseeId) {
		this.supervisorId = supervisorId;
		this.superviseeId = superviseeId;
	}

	public long getSupervisorId() {
		return supervisorId;
	}
	public long getSuperviseeId() {
		return superviseeId;
	}

}
