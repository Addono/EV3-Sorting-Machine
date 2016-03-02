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
	
	public Input() throws Exception {
		// Initialize color objects.
		color = new EV3ColorSensor(SensorPort.S1);
		colorRGB = color.getRGBMode();
		colorRed = color.getRedMode();
	}
		
	public void readColor() { // Change to boolean to return if success full?
		colorRGB.fetchSample(RGB, 0); // Get the RGB values.
		colorRed.fetchSample(redArray, 0);
		red = redArray[0];
	}
	
	
	
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
