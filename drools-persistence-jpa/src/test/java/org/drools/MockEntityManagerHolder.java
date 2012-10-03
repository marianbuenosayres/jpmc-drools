package org.drools;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.orm.jpa.EntityManagerHolder;

public class MockEntityManagerHolder extends EntityManagerHolder {

	private final EntityManagerFactory emf;
	
	public MockEntityManagerHolder(EntityManagerFactory emf) {
		super(emf.createEntityManager());
		this.emf = emf;
	}
	
	@Override
	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	@Override
	public int hashCode() {
		return super.hashCode() + ((emf == null) ? 0 : emf.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MockEntityManagerHolder other = (MockEntityManagerHolder) obj;
		if (emf == null) {
			if (other.emf != null) return false;
		} else if (!emf.equals(other.emf)) return false;
		return true;
	}
}
