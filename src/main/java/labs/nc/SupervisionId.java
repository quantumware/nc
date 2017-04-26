package labs.nc;

import java.io.Serializable;

public class SupervisionId implements Serializable {
	
	private long supervisorId;
	private long superviseeId;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (superviseeId ^ (superviseeId >>> 32));
		result = prime * result + (int) (supervisorId ^ (supervisorId >>> 32));
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
		SupervisionId other = (SupervisionId) obj;
		if (superviseeId != other.superviseeId)
			return false;
		if (supervisorId != other.supervisorId)
			return false;
		return true;
	}

}
