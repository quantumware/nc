package labs.nc;

import java.util.List;

public interface NcDao {
	
	public List<String> findTeammates(long employeeId);
	public long getCommonManager(long superviseeId1, long superviseeId2);
	public long getFirstCommonManager(long superviseeId1, long superviseeId2);
	public long getClosestManager(long projectId);
	
	public void addObject(Object e);
	public String getEmployeeName(long employeeId);
	public long findFirstEmployeeId(String employeeName);

}
