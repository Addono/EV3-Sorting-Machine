import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class Output {
	private StateVariables sv;	// Store the state variables object.

	private String currentMessage = ""; // Contains the message which currently should be drawn.

	private int turndegrees = 216; // 360 degrees * (24 teeth / 8 teeth) gear multiplier / 5 teeth

	private int lines = 3; // max lines per output message

	private int linechar = 19; // max characters per lines

	private String[] parts = new String[20];

	private void breakmessage(String message)
	{
		int mlength = message.length;
		if(mlength > linechar)
		{
			parts = message.split(" ");
		}
	}

	public Output(StateVariables sv) {
		this.sv = sv;
	}

	// 				HANDLE MESSAGE CONTROL

	public void tubeEmpty() {
		currentMessage = "Tube is empty! Press any button";
		breakmessage(currentMessage);
	}

	public void waitForInput() {
		currentMessage = "The tube is empty, waiting for input";
		breakmessage(currentMessage);
	}

	public void askIfEmpty() {
		currentMessage = "Is the tube empty? Yes or No?";
		breakmessage(currentMessage);
	}

	public void tubeNotEmpty() {
		currentMessage = "Tube not empty.Sorting";
		breakmessage(currentMessage);
	}

	public void askUser() {
		currentMessage = "Tube should be empty but disk detected, should the machine stop?";
		breakmessage(currentMessage);
	}

	public void breakMachine() {
		currentMessage = "Break. Resting..";
		breakmessage(currentMessage);
	}

	public void notBreak() {
		currentMessage = "No break. Sorting..";
		breakmessage(currentMessage);
	}

	public void start() {
		currentMessage = "Starting..";
		breakmessage(currentMessage);
	}

	public void noDisk() {
		currentMessage = "No disk detected";
		breakmessage(currentMessage);
	}

	public void anotherColor() {
		currentMessage = "Different color than expected, wrong type of disk?";
		breakmessage(currentMessage);
	}

	public void stuckInTube() {
		currentMessage = "Earlier done than expected, disk stuck?";
		breakmessage(currentMessage);
	}

	/*
	 * Draw the currently active message on the screen.
	 */
	public void setMessage() {
		LCD.clearDisplay();						// Clear the screen prior drawing.
	  if(currentMessage.length < linechar)
			LCD.drawString(currentMessage, 0, 0);   // Draw the message.
		else
		{
			int messcount = 0;
			int i = 0;
			for(String mess: parts)
			{
				messcount = messcount = mess.length;
				LCD.drawString(mess + " ", 0, i);
				if(messcount > linechar)
					i++;
			}
		}

		LCD.drawString("Disks : " + sv.getDiskCount(), 0, 3);
		LCD.drawString("White : " + sv.getWhiteDiskCount(), 0, 4);
		LCD.drawString("Black : " + sv.getBlackDiskCount(), 0, 5);
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
