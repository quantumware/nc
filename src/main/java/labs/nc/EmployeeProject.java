package labs.nc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEE_PROJECT")
@IdClass(EmployeeProjectId.class)
public class EmployeeProject {
	@Id
	@Column(name="EMPLOYEE_ID")
	private long employeeId;
	@Id
	@Column(name="PROJECT_ID")
	private long projectId;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="EMPLOYEE_ID", referencedColumnName="ID")
	private Employee employee;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="PROJECT_ID", referencedColumnName="ID")
	private Project project;

	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

}
