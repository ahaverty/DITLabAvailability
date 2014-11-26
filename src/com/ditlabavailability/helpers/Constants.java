package com.ditlabavailability.helpers;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

final public class Constants {

	public static final DateTimeFormatter FMT = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	
	private Constants(){
	}

}
