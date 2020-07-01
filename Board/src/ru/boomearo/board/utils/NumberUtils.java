package ru.boomearo.board.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;


public class NumberUtils {
	private static final String k = "§lk";
	private static final String m = "§lM";
	private static final String b = "§lB";
	private static final String t = "§lT";
	private static final String q = "§lQ";
	
    static final NumberFormat PRETTY_FORMAT = NumberFormat.getInstance(Locale.US);
    static {
        PRETTY_FORMAT.setRoundingMode(RoundingMode.FLOOR);
        PRETTY_FORMAT.setGroupingUsed(true);
        PRETTY_FORMAT.setMinimumFractionDigits(2);
        PRETTY_FORMAT.setMaximumFractionDigits(2);
    }

    public static String formatAsPrettyCurrency(BigDecimal value) {
        String str = PRETTY_FORMAT.format(value);
        if (str.endsWith(".00")) {
            str = str.substring(0, str.length() - 3);
        }
        return str;
    }

    public static String displayCurrency(final double value) {
		BigDecimal bd = new BigDecimal(value);
        String currency = formatAsPrettyCurrency(bd);
        if (bd.signum() < 0) {
            currency = currency.substring(1);
        }
        return currency;
    }
    
    
    public static String fixMoney(double d) {
    
      if (d < 1000.0D) {
        return format(d);
      }
      if (d < 1000000.0D) {
        return format(d / 1000.0D) + k;
      }
      if (d < 1.0E9D) {
        return format(d / 1000000.0D) + m;
      }
      if (d < 1.0E12D) {
        return format(d / 1.0E9D) + b;
      }
      if (d < 1.0E15D) {
        return format(d / 1.0E12D) + t;
      }
      if (d < 1.0E18D) {
        return format(d / 1.0E15D) + q;
      }
      return String.valueOf(d);
    }
    
    public static String format(double d) {
    
      NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
      
      format.setMaximumFractionDigits(2);
      
      format.setMinimumFractionDigits(0);
      
      return format.format(d);
    }

}