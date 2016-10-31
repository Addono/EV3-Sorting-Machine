InsertedEarly {
   call output function that outputs a message on th screen that notifies the user that disks were inserted too early;
   if (no button is pressed || yes button is pressed || start/stop button is pressed) {
    // check return values specific condition functions
    return CheckDiskPresent; // proceed to CheckDiskPresent state
   }
   return InsertedEarly; // remain in the InsertedEarly state
  }
 }

