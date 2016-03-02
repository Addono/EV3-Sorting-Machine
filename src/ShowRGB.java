import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Example leJOS EV3 Project with an ant build file
 *
 */
public class ShowRGB {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
		SampleProvider colorRGB = color.getRGBMode();
		SampleProvider reflectedRed = color.getRedMode();
		
		float[] RGB = new float[4];
		float[] RR  = new float[1];
		float RGBAvg, redPer, bluePer, greenPer;
		int menu = 1;
		boolean leftButtonOld = false;
		boolean rightButtonOld = false;
		boolean isRed, isBlackDisk, isNoDisk, whiteDisk; // Red part is in front of the color sensor.
		
		// Color threshholds
		int redPerMin = 30;
		int redPerMax = 100;
		
		while(!Button.ESCAPE.isDown()) {
			colorRGB.fetchSample(RGB, 1);
			reflectedRed.fetchSample(RGB, 0);
			
			RGBAvg = (RGB[1] + RGB[2] + RGB[3]) / 3;
			redPer = 100*RGB[1] / (3 * RGBAvg);
			bluePer = 100*RGB[2] / (3 * RGBAvg);
			greenPer = 100*RGB[3] / (3 * RGBAvg);
			
			// Choose the colors.
			isRed = redPerMax > redPer && redPer > redPerMin;
			whiteDisk = RGBAvg >= .2;
			isBlackDisk = .2 > RGBAvg && RGBAvg >= .03;
			isNoDisk = RGBAvg < .03;
			
			if(Button.LEFT.isDown() && !leftButtonOld) {
				menu--;
			}
			
			leftButtonOld = Button.LEFT.isDown();
			
			if(Button.RIGHT.isDown() && !rightButtonOld) {
				menu++;
			}
			
			rightButtonOld = Button.RIGHT.isDown();
			
			LCD.clear();
			switch(menu % 2) {
			case 0:
				LCD.drawString("Red " + RGB[0], 0, 0);
				LCD.drawString(":" + RGB[1], 9, 0);
				LCD.drawString("Green    :" + RGB[2], 0, 1);
				LCD.drawString("Blue     :" + RGB[3], 0, 2);
				LCD.drawString("Intensity:" + RGBAvg, 0, 3);
				LCD.drawString("Red   [%]:" + redPer, 0, 4);
				LCD.drawString("Blue  [%]:" + bluePer, 0, 5);
				LCD.drawString("Green [%]:" + greenPer, 0, 6);
				break;
				
			case 1:
				LCD.drawString("isRed    : " + isRed, 0, 0);
				LCD.drawString("whiteDisk: " + whiteDisk, 0, 1);
				LCD.drawString("blackDisk: " + isBlackDisk, 0, 2);
				LCD.drawString("noDisk   : " + isNoDisk, 0, 3);
				break;
			}
			
			Delay.msDelay(300);
		}
	}
	
}
