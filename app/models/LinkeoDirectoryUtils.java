package models;

import java.util.ArrayList;
import java.util.List;

public class LinkeoDirectoryUtils {

	/**
	 * Returning all uid's having a given role
	 * 
	 * @param jdao
	 * @param pdao
	 * @param role
	 * @return
	 */
	public static List<String> findUidByRole(JobsDao jdao, PeopleDao pdao, String role) {
		ArrayList<String> list = new ArrayList<String>();
		List<Jobs> jobs = jdao.findByOu(role);
		for (Jobs job : jobs) {
			for (Object o : job.getMembers()) {
				if (o == null)
					continue;

				People p = pdao.findByDn(o.toString());
				if (p == null)
					continue;

				list.add(p.getUid().toString());
			}
		}
		return list;
	}

	/**
	 * Return true is given user has given role
	 * 
	 * @param jdao
	 * @param pdao
	 * @param user
	 * @param role
	 * @return
	 */
	public static boolean isUserInRole(JobsDao jdao, PeopleDao pdao, String user, String role) {
		return findUidByRole(jdao, pdao, role).contains(user);
	}
}