package org.springframework.prospring.ticket.domain;

import java.util.Date;

public class Performance {
	
	/** 
	 * Properties
	 */
	private long id;
	private Date dateAndTime;
	private PriceStructure priceStructure;
	private Show show;

	public Performance() {
		super();
	}
	/**
	 * @param id
	 * @param dateAndTime
	 * @param priceStructure
	 * @param show
	 */
	public Performance(long id, Date dateAndTime,
			PriceStructure priceStructure, Show show) {
		super();
		this.id = id;
		this.dateAndTime = dateAndTime;
		this.priceStructure = priceStructure;
		this.show = show;
	}
	
	/** 
	 * Setters and getters for the properties
	 */
	public Date getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public PriceStructure getPriceStructure() {
		return priceStructure;
	}
	public void setPriceStructure(PriceStructure priceStructure) {
		this.priceStructure = priceStructure;
	}
	public Show getShow() {
		return show;
	}
	public void setShow(Show show) {
		this.show = show;
	}

}
