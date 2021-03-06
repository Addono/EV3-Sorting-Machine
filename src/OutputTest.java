import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class OutputTest {

	public static void main(String[] args) {
		StateVariables sv = new StateVariables();
		Output o = new Output(sv);
		
		int menu = 1; // Stores which page is currently on top.
		
		boolean leftButtonOld = false;
		boolean rightButtonOld = false;
		
		boolean enterButtonOld = false;
		boolean enterButtonPressed = false;
		
		while(!Button.ESCAPE.isDown()) {
			// Manage button input.
			if(Button.LEFT.isDown() && !leftButtonOld) {
				menu--;
			}

			leftButtonOld = Button.LEFT.isDown();

			if(Button.RIGHT.isDown() && !rightButtonOld) {
				menu++;
			}

			rightButtonOld = Button.RIGHT.isDown();
			
			if(Button.ENTER.isDown() && !enterButtonOld) {
				enterButtonPressed = true;
			} else {
				enterButtonPressed = false;
			}
			
			// Start drawing on the screen.
			LCD.clear();
			LCD.drawString("Press Enter to run", 0, 5);
			LCD.drawString("Use left and right", 0, 6);
			LCD.drawString("to change current output.", 0, 7);
			
			// Page specific content.
			switch(menu % 2) {
			case 0:
				LCD.drawString("Motor Sort Black", 3, 0);
			
				if(enterButtonPressed) {
					o.motorSortBlack();
				}
				break;

			case 1:
				LCD.drawString("Motor Sort White", 3, 0);
				
				if(enterButtonPressed) {
					o.motorSortWhite();
				}
				break;
			}
			
			// Cap refresh rate (screen can't keep up with processor speed).
			Delay.msDelay(50);
		}
	}
}
