package labs.nc;

import java.util.List;

public interface NcDao {
	
	public List<String> getTeammates(String employeeId);
	
	public long getFirstCommonManager(String superviseeId1, String superviseeId2);
	
	public long getClosestManager(long projectId);

}
