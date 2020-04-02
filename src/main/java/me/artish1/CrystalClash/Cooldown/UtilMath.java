package me.artish1.CrystalClash.Cooldown;

import java.text.DecimalFormat;

public class UtilMath {
	
	
	 public static double trim(double untrimmeded, int decimal) {
	        String format = "#.#";
	 
	        for(int i = 1; i < decimal; i++) {
	            format = format + "#";
	        }
	        DecimalFormat twoDec = new DecimalFormat(format);
	        return Double.valueOf(twoDec.format(untrimmeded)).doubleValue();
	    }
	 
	 public static double convert(long time, TimeUnit unit, int decPoint) {
	        if(unit == TimeUnit.BEST) {
	            if(time < 60000L) unit = TimeUnit.SECONDS;
	            else if(time < 3600000L) unit = TimeUnit.MINUTES;
	            else if(time < 86400000L) unit = TimeUnit.HOURS;
	            else unit = TimeUnit.DAYS;
	        }
	        if(unit == TimeUnit.SECONDS) return UtilMath.trim(time / 1000.0D, decPoint);
	        if(unit == TimeUnit.MINUTES) return UtilMath.trim(time / 60000.0D, decPoint);
	        if(unit == TimeUnit.HOURS) return UtilMath.trim(time / 3600000.0D, decPoint);
	        if(unit == TimeUnit.DAYS) return UtilMath.trim(time / 86400000.0D, decPoint);
	        return UtilMath.trim(time, decPoint);
	    }
	 
}
