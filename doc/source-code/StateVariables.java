public class StateVariables {

	private int diskCounter = 0; // A counter that stores the amount of disks currently in the tube.
	private int whiteDiskCounter = 0; // A counter that stores the amount of sorted white disks.
	private int blackDiskCounter = 0; // A counter that stores the amount of sorted black disks.
	
	private float firstCaliPoint;
	private float secondCaliPoint;
	
	private boolean motorStalled = false;
	
	public StateVariables() {}
	
	// 				INPUT
	
	/*
	 * Return if disks are expected in the tube.
	 * @return	True if more disks where expected, else false.
	 */
	public boolean counterGreaterThanZero() {  // boolean function that returns true when diskcounter is greater than 0
		if (diskCounter > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean getMotorStalled() {
		return motorStalled;
	}
	
	// 				OUTPUT
	
	/**
	 * Set first calibration point
	 */
	public void setFirstCaliPoint(float point) {
		firstCaliPoint = point;
	}
	
	/**
	 * Set second calibration point
	 */
	public void setSecondCaliPoint(float point) {
		secondCaliPoint = point;
	}
	
	/**
	 * Returns the first calibration point
	 */
	public float getFirstCaliPoint() {
		return firstCaliPoint;
	}
	
	/**
	 * Returns the second calibration point
	 */
	public float getSecondCaliPoint() {
		return secondCaliPoint;
	}
	
	/*
	 * Increment the disk counter by one.
	 */
	public void increaseCounter() { // increase the diskcounter
		diskCounter++;
	}

	/*
	 * Decrement the black disk counter by one.
	 */
	public void decreaseCounter() {
		diskCounter--;
	}

	/*
	 * Increment the white disk counter by one.
	 */
	public void increaseWhiteCounter() { // increase the whitecounter
		whiteDiskCounter++;
	}

	/*
	 * Increment the black disk counter by one.
	 */
	public void increaseBlackCounter() { // increase the blackcounter
		blackDiskCounter++;
	}
	
	/*
	 * Reset the disk counter.
	 */
	public void setCounterToZero() { // set diskcounter to 0 again
		diskCounter = 0;
	}
	
	/*
	 * Reset the white disk counter.
	 */
	public void setWhiteCounterToZero() {
		whiteDiskCounter = 0;
	}
	
	/*
	 * Reset the black disk counter.
	 */
	public void setBlackCounterToZero(){ // set blackcounter to 0 again
		blackDiskCounter = 0;
	}
	
	// 				SCREEN OUTPUT
	/*
	 * Return the value of the white disk counter.
	 */
	public int getWhiteDiskCount() {
		return whiteDiskCounter;
	}
	
	/*
	 * Return the value of the black disk counter.
	 */
	public int getBlackDiskCount() {
		return blackDiskCounter;
	}
	
	/*
	 * Return the amout of disks.
	 */
	public int getDiskCount() {
		return diskCounter;
	}
	
	//				MOTOR
	
	public void motorStalled(boolean isStalled) {
		motorStalled = isStalled;
	}
}	
	
