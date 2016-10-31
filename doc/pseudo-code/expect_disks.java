ExpectsDisk {
   if (the color sensor detects white) {
   // check return value specific condition function
    call the state variable function that decreases the disk counter;
    call the output function that rotates the motor in the right direction (white disk);
    call the state variable function that increases the white counter;
    return AcceptDisk2; // proceed to the AcceptDisk2 state
   } else if (the color sensor detects black) {
   // check return value specific condition function
    call the state variable function that decreases the disk counter;
    call the output function that rotates the motor in the right direction (black disk);
    call the state variable function that increases the black counter;
    return AcceptDisk2; // proceed to the AcceptDisk2 state
   } else if (the color sensor detects no black and no white) {
    call the output function that notifies the user that there is a disk stuck in the tube;
    if (the  start/stop button is pressed) {
      // check return value specific condition function
     call the output function that asks the user if the tube is empty;
     return Rest; // go back to the resting state
    }
   } else {  // If the color sensor is indecisive.
    call the output function that notifies the user that another color is detected;
    if (the start/stop button is pressed) {
      // check return value specific condition function
     call the output function that asks the user if the tube is empty;
     return Rest; // go back to the resting state
    }
   }
   
   return ExpectsDisk; // remain in the current state
  }
 }