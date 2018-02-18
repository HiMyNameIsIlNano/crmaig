package com.aig.crm.breadcrumb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface Link {

	String label();
	String family();
	String parent();
	
}
