package com.ditlabavailability.helpers;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

final public class Constants {

	public static final DateTimeFormatter FMT = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	
	public static final int START_HOUR_OF_DAY = 9;
	public static final int END_HOUR_OF_DAY = 21;
	
	private Constants(){
	}

}
