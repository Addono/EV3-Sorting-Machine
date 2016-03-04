import lejos.hardware.lcd.LCD;

enum States implements State {
	Initial {
		@Override
		public State next(Input i, Output o) {
			return WaitForButton;
		}
	},
	
	Rest {
		@Override
		public State next(Input i, Output o) {
			// Ask user to press enter if tube is empty
			//LCD.drawString("Resting now", 0, 0);
			if (i.buttonSSDown()) {
				return Waiting;
			} else {
			return Rest;
			}
		}
	},
	
	Waiting {
		@Override
		public State next(Input i, Output o) {
			// Display nr. of inserted disks and enter to start the sorting
			if (i.touchDown()) {
				//count++
				return DiskAdd;
			}
			if (i.buttonSSDown()) {
				return AcceptDisk2;
			}
			return Waiting;
		}
	},
	
	DiskAdd {
		@Override
		public State next(Input i, Output o) {
			if (!i.touchDown()) {
				return Waiting;
			} else {
				return DiskAdd;
			}
			
		}
	},
	
	AcceptDisk2 {
		@Override
		public State next(Input i, Output o) {
			//display the state, count
			//thus needs counter as input
			if (countIsGreaterThanZero()) {
				return ExpectsDisk;
			} else {
				return ExpectsFinished;
			}
		}
	},
	
	ExpectsDisk {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); //input from the color sensor
			//display nr of disks, plus nr of black and white
			if (i.colorSensorWhite() && !i.colorSensorBlack() && !i.colorSensorGrey()) {
				//do count--
				//do motor angle++
				//do white counter++
				//wait until done
				return AcceptDisk2;
			} else if (i.colorSensorBlack() && !i.colorSensorWhite() && !i.colorSensorGrey()) {
				//do count--
				//do motor angle--
				//do black counter++
				//wait until done
				return AcceptDisk2;
			} else if (!i.colorSensorBlack() && !i.colorSensorWhite() && i.colorSensorGrey()) {
				//say it is done
				return Rest;
			} else {//all three are false
				//say something is wrong
				return Rest;
			}
			
		}
	},
	
	WaitForButton { //sample
		@Override
		public State next(Input i, Output o) {
			if (i.buttonSSDown()) {
				return Rest;
			}
			
			LCD.drawString("Waiting for a button", 0, 0);
			return WaitForButton; 
		}
	},
	
	Exit {
		@Override 
		public State next(Input i, Output o) {
			return Exit;
		}
	}
}
