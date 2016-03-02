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
	SampleProvider colorRGB, reflectedRed;
	
	public Input() {
		color = new EV3ColorSensor(SensorPort.S1);
		colorRGB = color.getRGBMode();
		reflectedRed = color.getRedMode();
	}
		
	public void readColor() { // To boolean to return if success full?
		
	}
	
	public boolean buttonEnterDown() {
		return Button.ENTER.isDown();
	}
	
	public boolean buttonRightDown() {
		return Button.RIGHT.isDown();
	}
	
	public boolean buttonLeftDown() {
		return Button.LEFT.isDown();
	}
}
