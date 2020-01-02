package org.springframework.prospring.ticket.domain;

import java.util.Collection;

public class Show {
	private long id;
    private String name;
    private Collection performances;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection getPerformances() {
		return performances;
	}
	public void setPerformances(Collection performances) {
		this.performances = performances;
	}
}
