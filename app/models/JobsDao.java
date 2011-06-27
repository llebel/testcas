package models;

import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;

/**
 * Classe DAO d'accès aux entités LDAP Jobs
 * @author Loic
 *
 */
public class JobsDao {
	private LdapTemplate ldapTemplate;

	/**
	 * Classe d'aide pour mapper les attributs LDAP vers les entités Jobs
	 * 
	 */
	private static class JobsAttributMapper implements AttributesMapper {
		public Jobs mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
			Jobs j = new Jobs();
			j.setMembers(attrs.get("member").getAll());
			j.setOu(attrs.get("ou").getAll());
			j.setCn(attrs.get("cn").get().toString());
			return j;
		}
	}

	/**
	 * Injecté par configuration
	 * 
	 * @param ldapTemplate
	 */
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	/**
	 * Retourn un Jobs par son nom canonique
	 * 
	 * @param cn
	 * @return
	 */
	public Jobs findByPrimaryKey(String cn) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", "Jobs");
		dn.add("cn", cn);
		return (Jobs) ldapTemplate.lookup(dn, new JobsAttributMapper());
	}


	/**
	 * Liste des Jobs contenant au moins l'<em>ou</em> donné
	 * 
	 * @param ou
	 * @return
	 */
	public List<Jobs> findByOu(String ou) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "groupOfNames"));
		filter.and(new LikeFilter("ou", ou));
		return ldapTemplate.search("", filter.encode(), new JobsAttributMapper());
	}

	/**
	 * Liste des jobs contenant au moins la personne donnée
	 * 
	 * @param people
	 * @return
	 */
	public List<Jobs> findByPeople(People person) {
		// Can't find jobs for NULL people
		if(person == null) {
			return new ArrayList<Jobs>();
		}
		
		// Base DN
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", "Jobs");

		// Building filter
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "groupOfNames"));
		filter.and(new EqualsFilter("member", "uid=" + person.getUid() + ",ou=People,dc=linkeo,dc=com"));
		
		// Return result
		return ldapTemplate.search(dn, filter.encode(), new JobsAttributMapper());
	}
}
