import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		State s = States.Initial;     // Set the initial state.
		StateVariables sv = new StateVariables(); // Create new state variables object.
		Input input = new Input(sv);    // Create the input object.
		Output output = new Output(sv); // Create the output object.
		
		while(!Button.ESCAPE.isDown()) {
			// Update the state.
			s = s.next(input, output);
			
			// Update the message drawn on the screen.
			output.setMessage(s);
			
			// Implement the STOP button.
			if(Button.UP.isDown()) {
				s = States.Rest;
				output.aborted();
			}
		}
	}
}
