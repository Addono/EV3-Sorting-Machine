import lejos.utility.Delay;
import lejos.internal.ev3.EV3LED;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.ConfigurationPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3SensorConstants;

public class Output implements EV3SensorConstants {
	private StateVariables sv;	// Store the state variables object.
	private String currentMessage = ""; // Contains the message which currently should be drawn.
	private int turndegrees = 216; // 360 degrees * (24 gear teeth / 8 gear teeth) gear multiplier / 5 teeth = 216 degree / wheel teeth
	
	LED led; //create led object to control it
	
	private int lastLEDSpeed;
	private String lastLEDColor;

	// Import motor related things.
	private Port[] ports = new Port[4];
	ConfigurationPort[] configPorts = new ConfigurationPort[4];
	int motorPort;
	NXTRegulatedMotor motor;
	
	// The constructor of this class.
	public Output(StateVariables sv) {
		this.sv = sv;
		
		initializeMotor();
	}

	/**
	 * Initialize the motor by first looking in which port it is connected to.
	 */
	void initializeMotor() {
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
		updateSensors();
		while(motorPort == -1) {
			updateSensors();
		}
		
		// Close all ports, to allow the data of the sensors to be read.
		for(int i = 0; i < configPorts.length; i++) {
			configPorts[i].close();
		}
		
		NXTRegulatedMotor[] motors = new NXTRegulatedMotor[4];
		motors[0] = Motor.A;
		motors[1] = Motor.B;
		motors[2] = Motor.C;
		motors[3] = Motor.D;
		
		motor = motors[motorPort];
	}
	
	/**
	 * Update sensor position. 
	 */
	private boolean updateSensors() {
		motorPort = -1; // Reset all values.
		
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
		
		// Loop through all words, until all words are drawn or the screen is segment if full.
		for(int i = 0; i < words.length && line < height; i++) {
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
		motor.rotate(turndegrees, false);

		Delay.msDelay(300);
	}

	/*
	 * When the color sensor detects a white disk, turn one left.
	 */
	public void motorSortWhite() {
		motor.rotate(-turndegrees, false);

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
