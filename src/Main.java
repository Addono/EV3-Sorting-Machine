import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		State s = States.Initial;
		Input input = new Input();
		
		for(;!(s instanceof FinalState) && !Button.ESCAPE.isDown(); s = s.next(input)) {
			Delay.msDelay(100);
			LCD.drawString("Current State: ", 0, 6);
			LCD.drawString((States)s + "", 0 , 7);
		};
		
	}
	
}
