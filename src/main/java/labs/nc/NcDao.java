package labs.nc;

import java.util.List;

public interface NcDao {
	
	public List<String> findTeammates(long employeeId);
	public Employee getCommonManager(long superviseeId1, long superviseeId2);
	public Employee getClosestManager(long projectId);
	
	public void addObject(Object e);
	public Employee getEmployeeByName(String name);
	public Project getProjectByName(String name);

}
