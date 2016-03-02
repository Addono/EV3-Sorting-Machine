import lejos.hardware.Button;

public class Main {

	public static void main(String[] args) {
		State s = States.Initial;
		Input input = new Input();
		
		for(;!(s instanceof FinalState) && !Button.ESCAPE.isDown(); s = s.next(input));
	}

}
