public class StateVariables {

	private int diskcounter = 0; //counter that will get increased when you put in a disk
	private int whitecounter = 0; // counter that will get increased when a white disk is sorted
	private int blackcounter = 0; // counter that will get increased when a black disk is sorted

	private void increaseCounter() { // increase diskcounter
		diskcounter++;
	}

	private void increaseWhiteCounter() { // increase whitecounter
		whitecounter++;
	}

	private void increaseBlackCounter() { // increase blackcounter
		blackcounter++;
	}
}	
	
