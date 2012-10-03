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
}
