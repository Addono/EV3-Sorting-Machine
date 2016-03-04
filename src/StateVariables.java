public class StateVariables {

	private int diskcounter = 0; //counter that will get increased when you put in a disk
	private int whitecounter = 0; // counter that will get increased when a white disk is sorted
	private int blackcounter = 0; // counter that will get increased when a black disk is sorted
	
	// Constructor
	public StateVariables() {
		
	}
	
	public void increaseCounter() { // increase the diskcounter
		diskcounter++;
	}

	public void increaseWhiteCounter() { // increase the whitecounter
		whitecounter++;
	}

	public void increaseBlackCounter() { // increase the blackcounter
		blackcounter++;
	}
	
	public void setCounterToZero() { // set diskcounter to 0 again
		diskcounter = 0;
	}
	
	public void setWhiteCounterToZero() { //set whitecounter to 0 again
		whitecounter = 0;
	}
	
	public void setBlackCounterToZero(){ // set blackcounter to 0 again
		blackcounter = 0;
	}
	
	public boolean CounterGreaterThanZero() {  // boolean function that returns true when diskcounter is greater than 0
		if (diskcounter > 0){
			return true;
		}
		else {
			return false;
		}
	}
}	
	
