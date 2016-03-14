import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.internal.ev3.EV3LED;
import lejos.utility.Delay;


import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;

public class Output {
	private StateVariables sv;	// Store the state variables object.

	private String currentMessage = ""; // Contains the message which currently should be drawn.
	

	LED led; //create led object to control it
	
	private int lastLEDSpeed;
	private String lastLEDColor;
	
	/*
	 * Draws a string in a specific area.
	 * @input	The string to be drawn.
	 * @input	x start coordinate of the segment.
	 * @input	y start coordinate of the segment.
	 * @input	Width of the segment.
	 * @input	Height of the segment.
	 */
	private void textSegment(String input, int x, int y, int width, int height) {
		String[] words = input.split(" ");
		int line = y;
		int lineLength = 0;
		String currentLine = "";
		
		// Loop through all words, until all words are drawn, or the segment if 
		for(int i = 0; i < words.length && line < height; i++) {
			if(lineLength + words[i].length() >= width) {
				LCD.drawString(currentLine, x, line);
				
				line++;
				currentLine = "";
				lineLength = 0;
			}
			
			lineLength += words[i].length() + 1;
			currentLine += words[i] + " ";
		}
		
		LCD.drawString(currentLine, x, line);
	}

	private int turndegrees = 216; // 360 degrees * (24 gear teeth / 8 gear teeth) gear multiplier / 5 teeth = 216 degree / wheel teeth

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
		currentMessage = "Tube not empty. Sorting";
	}

	public void askUser() {
		currentMessage = "Unexpected disk detected, should we stop?";
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
	
	public void tooEarly() {
		currentMessage = "Disk inserted too early, press enter to flush.";
	}

	public void anotherColor() {
		currentMessage = "Different color, wrong type of disk?";
	}

	public void stuckInTube() {
		currentMessage = "Earlier done than expected, disk stuck?";
	}

	public void enterToSort() {
		currentMessage = "Press Enter to start sorting";
	}

	/*
	 * Draw the currently active message on the screen.
	 */
	public void setMessage(State s) {
		LCD.clearDisplay();							// Clear the screen prior drawing.
		textSegment(currentMessage, 0, 0, 19, 3);  	// Draw the message.

		LCD.drawString("Disks : " + sv.getDiskCount(), 0, 3);
		LCD.drawString("White : " + sv.getWhiteDiskCount(), 0, 4);
		LCD.drawString("Black : " + sv.getBlackDiskCount(), 0, 5);

		// Draw the current state on the screen.
		LCD.drawString("Current State: ", 0, 6);
		LCD.drawString((States)s + "", 0 , 7);
	}

	// 				HANDLE MOTOR CONTROL

	/*
	 * When the color sensor detects a black disk, turn one right.
	 */
	
	public void setLed(String color, int speed) {//requires a color and a speed 0, 1 or 2
		led = LocalEV3.ev3.getLED();
		speed *= 3; //is needed for the right number
		
		if (speed != lastLEDSpeed && !color.equals(lastLEDColor)){ //to make sure it's only called the first time
			lastLEDSpeed = speed;
			lastLEDColor = color;
			if (color.equals("green")) {
				led.setPattern(speed + EV3LED.COLOR_GREEN);
			} else if (color.equals("red")) {
				led.setPattern(speed + EV3LED.COLOR_RED);
			} else if (color.equals("orange")) {
				led.setPattern(speed + EV3LED.COLOR_ORANGE);
			} else {
				led.setPattern(0);
			}
		}
	}
	
	public void motorSortBlack() {
		Motor.A.rotate(turndegrees, false);

		Delay.msDelay(300);
	}

	/*
	 * When the color sensor detects a white disk, turn one left.
	 */
	public void motorSortWhite() {
		Motor.A.rotate(-turndegrees, false);
		
		Delay.msDelay(300);
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

	public void setCounterToZero() {
		sv.setCounterToZero();
	}
}
