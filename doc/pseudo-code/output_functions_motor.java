// When the color sensor detects a black disk, turn one teeth right.
output function motorSortBlack{
  motor.rotate turn one teeth right
 }

// When the color sensor detects a white disk, turn one teeth left.
output function motorSortWhite{
  motor.rotate turn one teeth left
 }
 
 // Let the motor turn a small step.
 output function motorTurnSmallStep(boolean left) {
  if(left) {
   motor.rotate.right (smallStepSize)
  } else {
   motor.rotate.left (smallStepSize)
 }

 // Let the motor run for half a teeth.
 output function motorTurnHalfTeeth(boolean left) {
  if(left) {
   motor.rotate turn half a teeth right
  } else {
   motor.rotate turn half a teeth left
  }
 }