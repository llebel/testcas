/**
 * 
 */
package controllers;

import javax.inject.Inject;

import models.JobsDao;
import models.LinkeoDirectoryUtils;
import models.People;
import models.PeopleDao;
import play.Logger;

/**
 * @author Loic
 * 
 */
public class Security extends controllers.modules.cas.Security {
	@Inject
	private static JobsDao jobsDao;

	@Inject
	private static PeopleDao peopleDao;

	static boolean check(String profile) {
		Logger.debug("check: %s for %s", profile, (String) connected());

		return LinkeoDirectoryUtils.isUserInRole(jobsDao, peopleDao, (String) connected(), profile);
	}

}
