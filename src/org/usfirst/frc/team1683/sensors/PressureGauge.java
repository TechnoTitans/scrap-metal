package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

public class PressureGauge{
	
	AnalogInput pressureSensor;
	public PressureGauge(int Channel){
		pressureSensor = new AnalogInput(Channel); 
	}
	
	public double GetPressure(){
		return pressureSensor.getVoltage()*20;
		
	}
}