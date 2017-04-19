package labs.nc;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="EMPLOYEE")
@NamedNativeQueries(value={
	@NamedNativeQuery(name="findTeammates", query="SELECT e.NAME FROM EMPLOYEE e, EMPLOYEE_PROJECT ep1 WHERE ep1.PROJECT_ID IN ("
			+ "SELECT ep2.PROJECT_ID FROM EMPLOYEE_PROJECT ep2 WHERE ep2.EMPLOYEE_ID = ?1) "
			+ "AND e.ID = ep1.EMPLOYEE_ID AND ep1.EMPLOYEE_ID <> ?2"),
	@NamedNativeQuery(name="findFirstEmployeeIdByName", query="SELECT ID FROM EMPLOYEE WHERE NAME = ?1 LIMIT 1"),
	@NamedNativeQuery(name="getEmployeeName", query="SELECT NAME FROM EMPLOYEE WHERE ID = ?1")
})
public class Employee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(name="NAME", length=255)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="supervisor")
    private List<Supervision> supervisors;

    @JsonIgnore
    @OneToMany(mappedBy="supervisee")
    private List<Supervision> supervisees;

	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private List<EmployeeProject> projects;
	
//	@ManyToMany(cascade = CascadeType.PERSIST)
//    @JoinTable(name = "EMPLOYEE_PROJECT", joinColumns = @JoinColumn(name = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "PROJECT_ID"))
//    private List<Project> projects;
	public Employee() {}
    public Employee(String name) {
    	this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
}
