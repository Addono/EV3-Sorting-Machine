// increase the diskcounter
state variable function increaseCounter { 
  diskCounter++;
 }

 // decrement the black disk counter by one.
 state variable function decreaseCounter {
  diskCounter--;
 }

 // Increment the white disk counter by one
 state variable function increaseWhiteCounter{ 
  whiteDiskCounter++;
 }
 
 // Reset the disk counter.
 state variable function setCounterToZero {
  diskCounter = 0;
 }
 
 
 // Return if disks are expected in the tube
 state variable function counterGreaterThanZero{  
 // boolean function that returns true when diskcounter is greater than 0
		if (diskCounter > 0) {
			return true;
		} else {
			return false;
		}
	}
 
 //   SCREEN OUTPUT
 //   Return the value of the white disk counter.
 state variable function getWhiteDiskCount {
  return whiteDiskCounter;
 }
 
 