package org.usfirst.frc.team1683.robot;


import java.util.Date;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;
import org.usfirst.team1683.PistonFire.Fire;
import org.usfirst.team1683.PistonFire.Reload;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/**
 * Handles all joystick inputs
 */
public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];
	
	Timer timer;
	
	DriveTrain drive;
	PiVisionReader piReader;
	
	Reload wheel; 

	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;
	private double maxPower = 1.0;

	private final double MAX_JOYSTICK_SPEED = 0.5;
	private final double SECOND_JOYSTICK_SPEED = 0.35;
	private Fire firing;

	private InputFilter rightFilter, leftFilter;
	
	boolean isFire;
	boolean isReload;
 

	private double p = 0.74;
	
	private double i = 0.0;
	private double d = 0.0;

	
	public Controls(DriveTrain drive, Reload fly, Fire firingMech) {
		this.drive = drive;
		timer = new Timer(); 
		firing = firingMech;
		timer.reset();
		
		isFire = false;
		isReload = false;

		SmartDashboard.prefDouble("ap", p);
		SmartDashboard.prefDouble("ai", i);
		SmartDashboard.prefDouble("ad", d);

		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
		
		
		wheel = fly; 
	}

	public void run() {
		SmartDashboard.sendData("Drive Power", maxPower, true);
		if (frontMode) {
			lSpeed = -maxPower * DriverSetup.leftStick.getRawAxis(DriverSetup.YAxis);
			rSpeed = -maxPower * DriverSetup.rightStick.getRawAxis(DriverSetup.YAxis);
		} else {
			lSpeed = maxPower * DriverSetup.rightStick.getRawAxis(DriverSetup.YAxis);
			rSpeed = maxPower * DriverSetup.leftStick.getRawAxis(DriverSetup.YAxis);
		}

		// Input filtering to avoid electrical failure
		if (maxPower == MAX_JOYSTICK_SPEED) {
			lSpeed = leftFilter.filterInput(Math.pow(lSpeed, 3));
			rSpeed = rightFilter.filterInput(Math.pow(rSpeed, 3));
		} else if (maxPower == SECOND_JOYSTICK_SPEED) {
			lSpeed = leftFilter.filterInput(lSpeed);
			rSpeed = rightFilter.filterInput(rSpeed);
		}

		if (DriverSetup.rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = MAX_JOYSTICK_SPEED;
		else if (DriverSetup.leftStick.getRawButton(HWR.SECOND_POWER))
			maxPower = SECOND_JOYSTICK_SPEED;
		
		if(DriverSetup.leftStick.getRawButton(3)){
			wheel.ReloadFly();
		} else if(DriverSetup.rightStick.getRawButton(3) && !isFire){
			isFire = true;
			timer.reset();
			timer.start();
			firing.fireTheBall();
		} 
		
		if(isFire){
			if(timer.get() < 2){
				firing.stopFiring();
				isFire = false; 
			} 
		}
		
		if(firing.getPress() < 80){
			firing.startCompress();
		} else if(firing.getPress() >= 80){
			firing.endCompress();
		} 
		drive.driveMode(lSpeed, rSpeed);
	}

	public static boolean checkToggle(int joystick, int button) {
		boolean pressed = false;

		switch (joystick) {
			case HWR.AUX_JOYSTICK:
				pressed = DriverSetup.auxStick.getRawButton(button);
				break;
			case HWR.RIGHT_JOYSTICK:
				pressed = DriverSetup.rightStick.getRawButton(button);
				break;
			case HWR.LEFT_JOYSTICK:
				pressed = DriverSetup.leftStick.getRawButton(button);
				break;
			default:
				break;
		}

		if (pressed && !joystickCheckToggle[joystick][button - 1]) {
			joystickCheckToggle[joystick][button - 1] = true;
			return true;
		} else if (pressed && joystickCheckToggle[joystick][button - 1]) {
			return false;
		} else {
			joystickCheckToggle[joystick][button - 1] = false;
			return false;
		}
	}
}
