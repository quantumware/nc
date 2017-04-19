package labs.nc;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="EMPLOYEE")
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
	@OneToMany(mappedBy="project")
	private List<EmployeeProject> employeeProjects;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
