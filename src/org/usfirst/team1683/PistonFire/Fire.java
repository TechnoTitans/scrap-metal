package org.usfirst.team1683.PistonFire;

import org.usfirst.frc.team1683.sensors.PressureGauge;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class Fire {
	Solenoid solen;
	Compressor compress;
	PressureGauge pressureSense; 
	public Fire(Solenoid sol, Compressor comp, PressureGauge press){
		solen = sol;
		compress = comp; 
		pressureSense = press;
	}
	
	public void fireTheBall(){
		solen.set(true);
	}
	
	public void stopFiring(){
		solen.set(false);
	}
	
	public void startCompress(){
		compress.start();
	}
	public void endCompress(){
		compress.stop();
	}
	
	public boolean compressState(){
		return compress.enabled();
	}
	
	public double getPress(){
		return pressureSense.GetPressure();
	}
	
}
