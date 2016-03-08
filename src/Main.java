import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		State s = States.Initial;     // Set the initial state.
		StateVariables sv = new StateVariables(); // Create new state variables object.
		Input input = new Input(sv);    // Create the input object.
		Output output = new Output(sv); // Create the output object.
		
		for(;!(s instanceof FinalState) && !Button.ESCAPE.isDown();) {
			// Update the state.
			s = s.next(input, output);
			
			// Update the message drawn on the screen.
			output.setMessage();
			
			// Step for step mode.
			// Button.UP.waitForPress();
			
			// Draw the current state on the screen.
			LCD.drawString("Current State: ", 0, 6);
			LCD.drawString((States)s + "", 0 , 7);
			
			Delay.msDelay(100);
		};
	}
}
