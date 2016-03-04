	import lejos.hardware.lcd.LCD;

enum States implements State {
	Initial {
		@Override
		public State next(Input i) {
			return WaitForButton;
		}
	},
	
	Rest {
		@Override
		public State next(Input i) {
			// Ask user to press enter if tube is empty
			if (i.buttonSSDown()) {
				return Waiting;
			} else {
			return Rest;
			}
		}
	},
	
	Waiting {
		@Override
		public State next(Input i) {
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
		public State next(Input i) {
			if (!i.touchDown()) {
				return Waiting;
			} else {
				return DiskAdd;
			}
			
		}
	},
	
	AcceptDisk2 {
		@Override
		public State next(Input i) {
			//display the state, count
			//thus needs counter as input
			if (countIsGreaterThanZero()) {
				return ExpectsDisk;
			} else {
				return ExpectsFinished;
			}
		}
	},
	
	ExpectsFinished {
		@Override
		public State next(Input i) {
			i.updateColor();
			if (i.colorSensorBlack() || i.colorSensorWhite()) {
				//unexpected error
				return AskUser;
			} else {
				//ask user if tube is empty
				while (!i.buttonYesDown()) {}
				return Rest;
			}
		}
	},
	
	
	ExpectsDisk {
		@Override
		public State next(Input i) {
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
		public State next(Input i) {
			if (i.buttonSSDown()) {
				return Rest;
			}
			
			LCD.drawString("Waiting for a button", 0, 0);
			return WaitForButton; 
		}
	},
	
	Exit {
		@Override 
		public State next(Input i) {
			return Exit;
		}
	}
}
