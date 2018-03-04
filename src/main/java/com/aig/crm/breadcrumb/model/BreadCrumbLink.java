package com.aig.crm.breadcrumb.model;

import lombok.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class BreadCrumbLink implements Serializable {

	private static final long serialVersionUID = -1734182996388561350L;

	private BreadCrumbLink previous;
	private List<BreadCrumbLink> next = new LinkedList<BreadCrumbLink>();
	private String url;
	private String family;
	private String label;
	boolean currentPage;
	private String parentKey;
	private BreadCrumbLink parent;

	public BreadCrumbLink(String family, String label, boolean currentPage, String parentKey) {
		this.family = family;
		this.label = label;
		this.currentPage = currentPage;
		this.parentKey = parentKey;
	}

}
