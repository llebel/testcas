package models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;

import play.Logger;

@SuppressWarnings("unchecked")
public class PeopleDao {

	private LdapTemplate ldapTemplate;

	private String nameSpace;

	private static class PersonAttributMapper implements AttributesMapper {
		public People mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {
			People p = new People();

			if (attrs.get("uid") != null)
				p.setUid(attrs.get("uid").get().toString());

			if (p.getUid().equals("nobody")) // nobody is not actually an user.
				return null;

			if (attrs.get("givenname") != null)
				p.setFirstName(attrs.get("givenname").get().toString());

			if (attrs.get("sn") != null)
				p.setLastName(attrs.get("sn").get().toString());

			if (attrs.get("employeeNumber") != null)
				p.setEmployerNumber(attrs.get("employeeNumber").get()
						.toString());

			if (attrs.get("uid") != null)
				p.setUid(attrs.get("uid").get().toString());

			if (attrs.get("mail") != null)
				p.setEmail(attrs.get("mail").get().toString());

			if (attrs.get("ou") != null)
				p.setOu(attrs.get("ou").get().toString());

			if (attrs.get("title") != null)
				p.setTitle(attrs.get("title").get().toString());

			if (attrs.get("cn") != null)
				p.setDisplayName(attrs.get("cn").get().toString());

			if (attrs.get(People.ZONE_SCHEMA_ATTRIBUTE) != null)
				p.setEmployeetype(attrs.get(People.ZONE_SCHEMA_ATTRIBUTE).get()
						.toString());

			if (attrs.get("userpassword") != null) {
				try {
					String pass = new String((byte[]) attrs.get("userpassword")
							.get());
					p.setUserpassword(pass);
				}
				catch (Exception e) {
					p.setUserpassword("?userpassword?");
				}

			}

			return p;
		}
	}

	public People findByPrimaryKey(String uid) {
		return findByUid(uid);
	}

	public List<People> findByEmployeeNumber(String employeeNumber) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new EqualsFilter("employeeNumber", employeeNumber));
		return ldapTemplate.search("", filter.encode(),
				new PersonAttributMapper());
	}

	public People findByUid(String uid) {
		Name dn = buildDn(uid);
		return (People) ldapTemplate.lookup(dn, new PersonAttributMapper());
	}

	public People findByDn(String dn) {
		if (dn.endsWith(this.nameSpace)) {
			dn = dn.substring(0, dn.length() - nameSpace.length()
					- ",".length());
		}
		return (People) ldapTemplate.lookup(dn, new PersonAttributMapper());

	}

	private Name buildDn(String uid) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", "People");
		dn.add("uid", uid);
		return dn;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
		try {
			this.nameSpace = ldapTemplate.getContextSource()
					.getReadOnlyContext().getNameInNamespace();
		}
		catch (org.springframework.ldap.NamingException e) {
			Logger.error("Exception retournée lors de l'execution ", e);
		}
		catch (NamingException e) {
			Logger.error("Exception retournée lors de l'execution ", e);
		}
	}

	public List<People> getPersonNamesByLastName(String lastName) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new LikeFilter("sn", lastName));
		return ldapTemplate.search("", filter.encode(),
				new PersonAttributMapper());
	}

	public List findWithNameLike(String name) {
		List l = new ArrayList();
		l.addAll(findByName(name + "*"));
		l.addAll(findByName("*" + name));
		return l;
	}

	public List<People> findByName(String name) {

		AndFilter filter1 = new AndFilter();
		filter1.and(new EqualsFilter("objectclass", "person"));
		filter1.and(new LikeFilter("sn", name));

		AndFilter filter2 = new AndFilter();
		filter2.and(new EqualsFilter("objectclass", "person"));
		filter2.and(new LikeFilter("givenname", name));

		AndFilter filter3 = new AndFilter();
		filter3.and(new EqualsFilter("objectclass", "person"));
		filter3.and(new LikeFilter("name", name));

		AndFilter filter4 = new AndFilter();
		filter4.and(new EqualsFilter("objectclass", "person"));
		filter4.and(new LikeFilter("cn", name));

		OrFilter filter = new OrFilter();
		filter.or(filter1);
		filter.or(filter2);
		filter.or(filter3);
		filter.or(filter4);

		return ldapTemplate.search("", filter.encode(),
				new PersonAttributMapper());

	}

	public List<People> findByAttribute(String attribute, String value) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new EqualsFilter(attribute, value));
		return ldapTemplate.search("", filter.encode(),
				new PersonAttributMapper());
	}

	public List<People> findByDisplayName(String displayName) {
		return findByAttribute(People.DISPLAYNAME_SCHEMA_ATTRIBUTE, displayName);
	}

	public List<People> findByLastName(String displayName) {
		return findByAttribute(People.LASTNAME_SCHEMA_ATTRIBUTE, displayName);
	}

//	public static PeopleDao getFromApplicationContext(ApplicationContext ctx) {
//		return (PeopleDao) ctx.getBean("PeopleDao");
//	}

	public void ChangePassword(People p) {

		Name dn = buildDn(p.getUid());

		Attribute attr = new BasicAttribute("userPassword", p.getUserpassword());
		ModificationItem item = new ModificationItem(
				DirContext.REPLACE_ATTRIBUTE, attr);
		ldapTemplate.modifyAttributes(dn, new ModificationItem[] { item });
	}

}
