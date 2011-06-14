package controllers;

import java.util.List;

import javax.inject.Inject;

import models.Jobs;
import models.JobsDao;
import models.People;
import models.PeopleDao;
import play.data.validation.Required;
import play.modules.cas.annotation.Check;
import play.mvc.Controller;
import play.mvc.With;
import controllers.modules.cas.SecureCAS;
import controllers.modules.cas.Security;

@With(SecureCAS.class)
public class Application extends Controller {

	@Inject
	private static PeopleDao peopleDao;

	@Inject
	private static JobsDao jobsDao;

	@Check("MGA_ADMIN")
	public static void index() {
		// Connected is the username
		String uid = (String)Security.connected();
		
		// Getting LDAP info
		People person = peopleDao.findByUid(uid);
		List<Jobs> jobs = jobsDao.findByPeople(person);
		
		// Render
		render(person, jobs);
	}

	@Check("MGA_ADMIN")
	public static void showPeople(String uid) {
		flash("uid", uid);
		
		// Getting LDAP info
		People person = peopleDao.findByUid(uid);
		List<Jobs> jobs = jobsDao.findByPeople(person);

		// Render
		render(person, jobs);
	}
}
