import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class Output {
	private StateVariables sv;	// Store the state variables object.

	private String currentMessage = ""; // Contains the message which currently should be drawn.
	
	private int turndegrees = 216; // 360 degrees * (24 teeth / 8 teeth) gear multiplier / 5 teeth  
	
	public Output(StateVariables sv) {
		this.sv = sv;
	}
	
	// 				HANDLE MESSAGE CONTROL
	
	public void tubeEmpty() {
		currentMessage = "Tube is empty! Press any button";
	}
	
	public void waitForInput() {
		currentMessage = "The tube is empty, waiting for input";
	}
	
	public void askIfEmpty() {
		currentMessage = "Is the tube empty? Yes or No?";
	}
	
	public void tubeNotEmpty() {
		currentMessage = "Tube not empty.Sorting";
	}
	
	public void askUser() {
		currentMessage = "Tube should be empty but disk detected, should the machine stop?";
	}
	
	public void breakMachine() {
		currentMessage = "Break. Resting..";
	}
	
	public void notBreak() {
		currentMessage = "No break. Sorting..";
	}
	
	public void start() {
		currentMessage = "Starting..";
	}
	
	public void noDisk() {
		currentMessage = "No disk detected";
	}
	
	public void anotherColor() {
		currentMessage = "Different color than expected, wrong type of disk?";
	}
	
	public void stuckInTube() {
		currentMessage = "Earlier done than expected, disk stuck?";
	}
	
	/*
	 * Draw the currently active message on the screen.
	 */
	public void setMessage() {
		LCD.clearDisplay();						// Clear the screen prior drawing.
		LCD.drawString(currentMessage, 0, 0);   // Draw the message.
	}

	// 				HANDLE MOTOR CONTROL
	
	/*
	 * When the color sensor detects a black disk, turn one right.
	 */
	public void motorSortBlack() {
		Motor.A.rotate(turndegrees, false);
	}
	
	/*
	 * When the color sensor detects a white disk, turn one left.
	 */
	public void motorSortWhite() {
		Motor.A.rotate(-turndegrees, false);
	}
	
	// 				HANDLE STATE VARIABLES
	
	public void decreaseCounter() {
		sv.decreaseCounter();
	}
	public void increaseBlackCounter() {
		sv.increaseBlackCounter();
	}
	public void increaseWhiteCounter() {
		sv.increaseWhiteCounter();
	}
	public void increaseCounter() {
		sv.increaseCounter();
	}
	
}
