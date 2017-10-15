package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Dna entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "dna", catalog = "gene")

public class Dna implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String type;
	private String sort;

	// Constructors

	/** default constructor */
	public Dna() {
	}

	/** full constructor */
	public Dna(String type, String sort) {
		this.type = type;
		this.sort = sort;
	}

	// Property accessors
	@Id

	@Column(name = "type", unique = true, nullable = false, length = 30)

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "sort", nullable = false, length = 65535)

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}