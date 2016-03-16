import lejos.internal.ev3.EV3LED;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.BrickFinder;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.ConfigurationPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3SensorConstants;

public class Output implements EV3SensorConstants {
	private StateVariables sv;	// Store the state variables object.
	
	private String currentMessage = ""; // Contains the message which currently should be drawn, initially empty.
	
	private int turndegrees = 216; // 360 degrees * (24 gear teeth / 8 gear teeth) gear multiplier / 5 teeth = 216 degree / wheel teeth
	private int smallStepSize = 1; // Define the size of a small step as an angle in degrees.
	
	private LED led; // Create led object to control it
	private GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
	
	private int lastLEDSpeed = -1;
	private String lastLEDColor = null;

	// Import motor related things.
	private Port[] ports = new Port[4];
	private ConfigurationPort[] configPorts = new ConfigurationPort[4];
	private int motorPort;					// Stores the number of the motor port on which the motor is detected.
	private NXTRegulatedMotor motor;		// The motor object which is connected to the wheel.
	
	// The constructor of this class.
	public Output(StateVariables sv) {
		this.sv = sv;
		
		// Disable the auto refresh of the screen, we will take care of it ourself.
		LCD.setAutoRefresh(false);
		
		initializeMotor();
	}

	/**
	 * Initialize the motor by first looking in which port it is.
	 */
	private void initializeMotor() {
		// Define all ports.
		ports[0] = MotorPort.A;
		ports[1] = MotorPort.B;
		ports[2] = MotorPort.C;
		ports[3] = MotorPort.D;
		
		// Open all configuration ports.
		for(int i = 0; i < ports.length; i++) {
			configPorts[i] = ports[i].open(ConfigurationPort.class);
		}
		
		// Wait until a motor is detected.
		do {
			updateSensors();
		} while(motorPort == -1);
		
		// Close all ports, to allow to open motor ports on it.
		for(int i = 0; i < configPorts.length; i++) {
			configPorts[i].close();
		}
		
		// Select - and therefore open - the appropriate motor port.
		switch(motorPort) {
		case 0:
			motor = Motor.A;
			break;
			
		case 1:
			motor = Motor.B;
			break;
			
		case 2:
			motor = Motor.C;
			break;
			
		case 3:
			motor = Motor.D;
			break;
		}
		
		// Maximize the motor speed for faster sorting.
		motor.setSpeed(motor.getMaxSpeed());
	}
	
	/**
	 * Update sensor position. 
	 */
	private boolean updateSensors() {
		motorPort = -1; // Reset all values.
		
		// Go through all ports and read the port type from them.
		for(int i = 0; i < configPorts.length; i++) {
			int portType = configPorts[i].getPortType();
			
			// Find sensor currently is in this port.
			switch(portType) {
			case TYPE_TACHO:
			case TYPE_MINITACHO:
			case TYPE_NEWTACHO:
			case CONN_OUTPUT_TACHO:
				if(motorPort != -1) {
					// Throw error: Two motors connected?
					return false;
				}
				
				motorPort = i;
				break;

			case CONN_ERROR:
				// Throw error: Unexpected sensor for this port, motor in sensor port or vice versa?
				return false;
			}
		}
		
		return true; // Return true if no errors where detected.
	}
	
	// 				HANDLE MESSAGE CONTROL

	public void tubeEmpty() {
		currentMessage = "Tube should be empty! Press Enter to resume.";
		setLEDState("userInput");
	}

	public void waitForInput() {
		currentMessage = "No disk detected, inserted disks will now be counted. Press Enter to cancel.";
		setLEDState("userInput");
	}

	public void askIfEmpty() {
		currentMessage = "Prior to sorting check if the tube is empty, press Yes or No. Enter: Calibrate";
		setLEDState("userInput");
	}

	public void tubeNotEmpty() {
		currentMessage = "Tube is not empty. Sorting disks now.";
		setLEDState("busy");
	}
	
	public void sorting() {
		currentMessage = "Sorting.";
		setLEDState("busy");
	}

	public void askUnexpectedDisk() {
		currentMessage = "Unexpected disk detected, press Yes to sort it, No to reset and go back to the main menu.";
		setLEDState("error");
	}

	public void breakMachine() {
		currentMessage = "Break. Resting..";
		setLEDState("busy");
	}

	public void notBreak() {
		currentMessage = "No break. Sorting..";
		setLEDState("busy");
	}
	
	public void aborted() {
		currentMessage = "Machine stopped, to start sorting: press Yes if the tube is empty, else press No.";
		setLEDState("error");
	}

	public void start() {
		currentMessage = "Starting..";
		setLEDState("busy");
	}

	public void noDisk() {
		currentMessage = "No disk detected";
		setLEDState("error");
	}
	
	public void tooEarly() {
		currentMessage = "Disk inserted too early, press enter to flush.";
		setLEDState("error");
	}

	public void anotherColor() {
		currentMessage = "Color not detected, press enter to flush as white disk?";
		setLEDState("error");
	}

	public void stuckInTube() {
		currentMessage = "Earlier done than expected, disk stuck? Press enter to dismiss.";
		setLEDState("error");
	}

	public void enterToSort() {
		currentMessage = "Press Enter to start sorting.";
		setLEDState("userInput");
	}
	
	public void isCalibrating() {
		currentMessage = "Calibration in process.";
		setLEDState("busy");
	}
	
	/**
	 * Draws a string in a specific area.
	 * @param	The string to be drawn.
	 * @param	x start coordinate of the segment.
	 * @param	y start coordinate of the segment.
	 * @param	Width of the segment.
	 * @param	Height of the segment.
	 */
	private void textSegment(String input, int x, int y, int width, int height) {
		String[] words = input.split(" ");
		int line = y;
		int lineLength = 0;
		String currentLine = "";
		
		// Loop through all words, until all words are drawn or the screen is segment if full.
		for(int i = 0; i < words.length && line + 1 < height; i++) {
			if(lineLength + words[i].length() >= width) { // Check if the next word first on the screen, if it doesn't then draw the previous line and resume on the next one.
				LCD.drawString(currentLine, x, line);
				
				line++;
				currentLine = "";
				lineLength = 0;
			}
			
			lineLength += words[i].length() + 1;
			currentLine += words[i] + " ";
		}
		
		LCD.drawString(currentLine, x, line); // Draw the last line.
	}

	/**
	 * Draw the currently active message on the screen.
	 */
	public void setMessage(State s) {
		LCD.clear();								// Clear the screen prior drawing.
		textSegment(currentMessage, 0, 0, 19, 5);  	// Draw the message.

		int barWidth = 92;
		int barHeight = 10;
		int x = 84;
		int y = 82;
		
		g.drawRect(x, y, barWidth, barHeight);
		g.fillRect(x, y, sv.getDiskCount() * barWidth / 12, barHeight); // One letter has size of +- 9 x 16 pixels
		
		// Draw the disk counters on the screen.
		LCD.drawString("Disks:" + sv.getDiskCount(), 0, 5);
		LCD.drawString("#Sorted W:" + sv.getWhiteDiskCount(), 0, 6);
		LCD.drawString("B:" + sv.getBlackDiskCount(), 13, 6);
		
		// Draw the current state on the screen.
		LCD.drawString("CS: " + (States)s, 0, 7);
		
		LCD.refresh(); // Refresh the screen to update the content on it.
	}

	public void setLEDState(String state) {
		switch(state) {
		case "error":
			setLED("red", 2);
			break;
		case "userInput":
			setLED("orange", 1);
			break;
		case "busy":
			setLED("green", 0);
			break;
		}
	}
	
	public void setLED(String color, int speed) {// Requires a color and a speed 0, 1 or 2
		led = LocalEV3.ev3.getLED();
		speed *= 3; // Is needed for the right number
		
		if (speed != lastLEDSpeed && !color.equals(lastLEDColor)){ // To make sure it's only called the first time
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
	
	// 				HANDLE MOTOR CONTROL

	/**
	 * When the color sensor detects a black disk, turn one teeth right.
	 */
	
	public void motorSortBlack() {
		motor.rotate(turndegrees, false);
	}

	/**
	 * When the color sensor detects a white disk, turn one teeth left.
	 */
	public void motorSortWhite() {
		motor.rotate(-turndegrees, false);
	}
	
	/**
	 * Let the motor turn a small step.
	 * @param True if it should turn left, false if it should turn right.
	 */
	public void motorTurnSmallStep(boolean left) {
		if(left) {
			motor.rotate(smallStepSize, true);
		} else {
			motor.rotate(-smallStepSize, true);
		}
	}

	/**
	 * Let the motor run for half a teeth.
	 * @param True if it should turn left, false if it should turn right.
	 */
	public void motorTurnHalfTeeth(boolean left) {
		if(left) {
			motor.rotate(turndegrees / 2, false);
		} else {
			motor.rotate(-turndegrees / 2, false);
		}
	}
	
	/**
	 * Moves the wheel in between the two calibrated points.
	 */
	public void motorMoveInBetweenCaliPoints() {
		int targetAngle = (int) ((sv.getFirstCaliPoint() + sv.getSecondCaliPoint()) / 2f);
		motor.rotateTo(targetAngle, false);
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
	
	public void setFirstCaliPoint() {
		sv.setFirstCaliPoint(motor.getPosition());
	}
	
	public void setSecondCaliPoint() {
		sv.setSecondCaliPoint(motor.getPosition());
	}
}
