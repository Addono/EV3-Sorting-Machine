output function stuckInTube{
  output message: "Earlier done than expected, disk stuck? Press enter to dismiss.";
  setLEDState: error;  // red light + high frequency blinking
}

output function enterToSort {
 output message: "Press Enter to start sorting.";
  setLEDState: userInput; // orange light + blinking
}

output functon isCalibrating {
  output message = "Calibration in process."; 
  setLEDState: busy // green light }