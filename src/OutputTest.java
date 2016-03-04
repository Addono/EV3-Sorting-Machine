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
			LCD.drawString("Press Enter to run", 1, 5);
			
			// Page specific content.
			switch(menu % 2) {
			case 0:
				LCD.drawString("Motor Sort Black", 3, 0);
			
				if(enterButtonPressed) {
					o.MotorSortBlack();
				}
				break;

			case 1:
				LCD.drawString("Motor Sort White", 3, 0);
				
				if(enterButtonPressed) {
					o.MotorSortWhite();
				}
				break;
			}
			
			// Cap refresh rate (screen can't keep up with processor speed).
			Delay.msDelay(50);
		}
	}
}
