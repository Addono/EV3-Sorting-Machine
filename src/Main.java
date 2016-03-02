import lejos.hardware.Button;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		State s = States.Initial;
		Input input = new Input();
		
		for(;!(s instanceof FinalState) && !Button.ESCAPE.isDown(); s = s.next(input)) {
			Delay.msDelay(100);
		};
	}

}
