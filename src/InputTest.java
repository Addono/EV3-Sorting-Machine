import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class InputTest {

	public static void main(String[] args) {
		StateVariables sv = new StateVariables();
		Input input = new Input(sv);
		int menu = 1;
		
		boolean leftButtonOld = false;
		boolean rightButtonOld = true;
		
		while(!Button.ESCAPE.isDown()) {
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
				LCD.drawString("Touch input", 3, 0);
				LCD.drawString("SS    : " + input.buttonSSDown(), 0, 1);
				LCD.drawString("Yes   : " + input.buttonYesDown(), 0, 2);
				LCD.drawString("No    : " + input.buttonNoDown(), 0, 3);
				LCD.drawString("Touch : " + input.touchDown(), 0, 4);
				break;

			case 1:
				input.updateColor();
				LCD.drawString("Color input", 3, 0);
				LCD.drawString("White : " + input.colorSensorWhite(), 0, 1);
				LCD.drawString("Black : " + input.colorSensorBlack(), 0, 2);
				LCD.drawString("Red   : " + input.colorSensorRed(), 0, 3);
				LCD.drawString("Grey  : " + input.colorSensorGrey(), 0, 4);
				break;
			}
			Delay.msDelay(100);
		}
	}

}
