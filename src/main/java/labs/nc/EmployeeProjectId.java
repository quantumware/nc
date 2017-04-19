package labs.nc;

import java.io.Serializable;

public class EmployeeProjectId implements Serializable {
	
	private long employeeId;
	
	private long projectId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (employeeId ^ (employeeId >>> 32));
		result = prime * result + (int) (projectId ^ (projectId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeProjectId other = (EmployeeProjectId) obj;
		if (employeeId != other.employeeId)
			return false;
		if (projectId != other.projectId)
			return false;
		return true;
	}

}
