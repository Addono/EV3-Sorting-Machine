// return true if the disk in front of the sensor is white.
  boolean function colorSensorWhite {
  return true, if and only if the value given by the color sensor is within a certain range that is specific for the color white
 }
 
// return true if the disk in front of the sensor is black.
  boolean function colorSensorBlack {
  return true, if and only if the value given by the color sensor is within a certain range that is specific for the color black
 }
 
// return true if the start/stop button is pressed, false if it isn't.  
  boolean function buttonSSDown {
  return true, if and only if the enter button is pressed down
 }
 
 // return true if the yes button is pressed, false if it isn't.  
 boolean function buttonYesDown {
   return true, if and only if the right button is pressed down
 }
 
// return true if the no button is pressed, false if it isn't.
 boolean function buttonNoDown {
  return true, if and only if the left button is pressed down
 }