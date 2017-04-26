package labs.nc;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="PROJECT")
@NamedQueries(value={
	@NamedQuery(name="projectByName", lockMode=LockModeType.NONE, query="SELECT p FROM Project p WHERE p.name = :name")
})
public class Project {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(name="NAME", length=255)
    private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy="project")
	private List<EmployeeProject> employees;
	
	public Project() {}
	public Project(String name) {
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
