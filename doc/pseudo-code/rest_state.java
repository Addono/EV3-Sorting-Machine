Rest {
   if (yes button is pressed) { 
   // check return value specific condition function
    call state variable function that sets the disk-counter to 0;
    return Waiting;
    // proceed to the Waiting state
   }
   else if(no button is pressed) { 
   // check return value specific condition function
    return CheckDiskPresent;
    // proceed to the CheckDiskPresent state
   } 
   else if (the touch sensor is pressed) { 
   // check return value specific condition function
    return InsertedEarly;
    // proceed to the InsertedEarly state
    } 
    
   return Rest;  // remain in the resting state
  }
 }