import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Input {
	EV3 ev3 = (EV3) BrickFinder.getLocal();
	TextLCD lcd = ev3.getTextLCD();
	Keys keys = ev3.getKeys();

	EV3ColorSensor color;
	SampleProvider colorRGB, colorRed;
	float[] RGB = new float[3];
	float[] redArray = new float[1];
	float red;
	float RGBAvg, redPer, bluePer, greenPer;
	
	public Input() {
		// Initialize color objects.
		
		color = new EV3ColorSensor(SensorPort.S1);
		colorRGB = color.getRGBMode();
		colorRed = color.getRedMode();
	}
	
	// Handle color input
	
	/*
	 * Fetches new colors from the sensor which replace the old values.
	 * 
	 * Note: Call them before getting color input which relies on this data,
	 * else old information will be used.
	 */
	public void updateColor() { // Change to boolean to return if success full?
		colorRGB.fetchSample(RGB, 0); // Get the RGB values.
		colorRed.fetchSample(redArray, 0);
		red = redArray[0];
		
		RGBAvg = (RGB[0] + RGB[1] + RGB[2]) / 3;
		redPer = 100*RGB[0] / (3 * RGBAvg);
		bluePer = 100*RGB[1] / (3 * RGBAvg);
		greenPer = 100*RGB[2] / (3 * RGBAvg);
	}
	
	public boolean colorSensorRed() {
		return redPer > 30;
	}
	
	public boolean colorSensorWhite() {
		return RGBAvg >= .2;
	}
	
	public boolean colorSensorBlack() {
		return .2 > RGBAvg && RGBAvg >= .03;
	}
	
	public boolean colorSensorGrey() {
		return RGBAvg < .03;
	}
	
	/*
	 * Checks if the touch sensor is pressed.
	 * @output	True if the sensor is pressed, false if it isn't.
	 */
	public boolean touchDown() {
		return false;
	}
	
	// Handle all button input.
	public boolean buttonSSDown() {
		return Button.ENTER.isDown();
	}
	
	public boolean buttonYesDown() {
		return Button.RIGHT.isDown();
	}
	
	public boolean buttonNoDown() {
		return Button.LEFT.isDown();
	}
}
