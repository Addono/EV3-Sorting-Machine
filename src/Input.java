import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class Input {
	// Store the state variables object.
	private StateVariables sv;
	
	// Initialize variables for the color variables.
	private EV3ColorSensor color;
	private SampleProvider colorRGB;
	private float[] RGB = new float[3];
	private float RGBAvg, redPer, bluePer, greenPer;
	
	// Initialize variables for the touch sensor and buttons. 
	private EV3TouchSensor touchSensor;
	private float[] isTouched = new float[1];
	
	public Input(StateVariables sv) {
		this.sv = sv;
		
		// Initialize color objects.
		color = new EV3ColorSensor(SensorPort.S1);
		colorRGB = color.getRGBMode();
		
		// Initialize touch objects.
		touchSensor = new EV3TouchSensor(SensorPort.S2);
	}
	
	// 				HANDLE COLOR INPUT
	
	/*
	 * Fetches new colors from the sensor which replace the old values.
	 * 
	 * Note: Call them before getting color input which relies on this data,
	 * else old information will be used.
	 */
	public void updateColor() { // Change to boolean to return if success full?
		colorRGB.fetchSample(RGB, 0); // Get the RGB values.
		
		RGBAvg = (RGB[0] + RGB[1] + RGB[2]) / 3;
		redPer = 100*RGB[0] / (3 * RGBAvg);
		bluePer = 100*RGB[1] / (3 * RGBAvg);
		greenPer = 100*RGB[2] / (3 * RGBAvg);
	}
	
	/*
	 * Returns if the red teeth of the sorting wheel are in front of the color sensor.
	 * @input	True if the red part of the sorting wheel is in front of the sensor
	 * 			false if it isn't.
	 */
	public boolean colorSensorRed() {
		return redPer > 30;
	}
	
	/*
	 * Returns if a white disk is in front of the sorting wheel.
	 * @input	True if the disk in front of the sensor is white, else false.
	 */
	public boolean colorSensorWhite() {
		return RGBAvg >= .2;
	}
	
	/*
	 * Returns if a black disk is in front of the sorting wheel.
	 * @input	True if the disk in front of the sensor is black.
	 */
	public boolean colorSensorBlack() {
		return .2 > RGBAvg && !colorSensorGrey();
	}
	
	/*
	 * Returns if no disk is in front of the sorting wheel.
	 * @input	True if no disk is in front of the sensor, else false.
	 */
	public boolean colorSensorGrey() {
		return redPer > 25;
	}
	
	/*
	 * Checks if the touch sensor is pressed.
	 * @input	True if the sensor is pressed, false if it isn't.
	 */
	public boolean touchDown() {
		// Get new values from the touch sensor.
		touchSensor.fetchSample(isTouched, 0);
		
		// Convert result of the touch sensor to a boolean value.
		if(isTouched[0] == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	// 				HANDLE TOUCH INPUT
	
	/*
	 * Returns if the start/stop button is pressed.
	 * @return	True if the start/stop button is pressed, false if it isn't.  
	 */
	public boolean buttonSSDown() {
		return Button.ENTER.isDown();
	}
	
	/*
	 * Returns if the Yes button is pressed.
	 * @return	True if the Yes button is pressed, false if it isn't.  
	 */
	public boolean buttonYesDown() {
		return Button.RIGHT.isDown();
	}
	
	/*
	 * Returns if the No button is pressed.
	 * @return	True if the No button is pressed, false if it isn't.  
	 */
	public boolean buttonNoDown() {
		return Button.LEFT.isDown();
	}
	
	// 				HANDLE STATE VARIABLES
	
	/*
	 * Returns if the disk counter is zero.
	 * @return	True if the disk counter is greater than zero, elso false.
	 */
	public boolean counterGreaterThanZero() {
		return sv.counterGreaterThanZero();
	}
}