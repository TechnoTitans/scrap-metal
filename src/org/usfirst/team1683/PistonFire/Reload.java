package org.usfirst.team1683.PistonFire;
import org.usfirst.frc.team1683.motor.TalonSRX;

public class Reload{
	TalonSRX flyWheel; 
	public Reload(TalonSRX talon){
		flyWheel = talon; 
	}
	
	public void ReloadFly(){
		flyWheel.set(-1);
	}
	
}