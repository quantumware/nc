package labs.nc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="SUPERVISION")
@IdClass(SupervisionId.class)
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
