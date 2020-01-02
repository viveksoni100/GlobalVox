package org.springframework.prospring.ticket.domain;

import java.util.Collection;

/**
 *
 */
public class Genre {

	private long id;
	private String name;
	private Collection shows;
	
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
	public Collection getShows() {
		return shows;
	}
	public void setShows(Collection shows) {
		this.shows = shows;
	}
	
	// TODO what about descriptions??? Is there any info on this in the domain model???
}
