package ru.boomearo.board.objects.hooks;

import org.bukkit.scheduler.BukkitRunnable;

import ru.boomearo.board.Board;

public class TpsRunnable extends BukkitRunnable {
	
	private long mills = 0;
    private double[] tpsArr = new double[10];
    private int index = 0;
    
    
	public TpsRunnable() {
		runnable();
	}
	
	public void runnable() {
		this.runTaskTimer(Board.getContext(), 20, 20);
	}
	
	public double getTPS() {
	    double tpsSum = 0.0D;
	    
	    for (double d : this.tpsArr) {
	      tpsSum += d;
	    }
	    
	    return Math.round(tpsSum / 10.0D * 100.0D) / 100.0D;
    }
	
	@Override
	public void run() {
	    if (this.mills > 0L) {
	        double tps, diff = (System.currentTimeMillis() - this.mills) - 1000.0D;
	        
	        if (diff < 0.0D) {
		          diff = Math.abs(diff);
	        }
	        if (diff == 0.0D) {
		          tps = 20.0D;
	        } 
	        else {
		          tps = 20.0D - diff / 50.0D;
	        } 
	        
	        if (tps < 0.0D) {
		          tps = 0.0D;
	        }
	        this.tpsArr[this.index++] = tps;
	        
	        if (this.index >= this.tpsArr.length) {
	            this.index = 0;
	        } 
	    } 
	    this.mills = System.currentTimeMillis();
	}
  
}
