import lejos.hardware.lcd.LCD;

enum States implements State {
	Initial {
		@Override
		public State next(Input input) {
			return WaitForButton;
		}
	},
	
	Rest {
		@Override
		public State next(Input input) {
			//ask user to press enter if tube is empty
			//LCD.drawString("Resting now", 0, 0);
			if (input.buttonEnterDown()) {
				return Waiting;
			} else {
			return Rest;
			}
		}
	},
	
	Waiting {
		@Override
		public State next(Input input) {
			//display nr of inserted disks and enter to start the sorting
			if (input.touchDown()) {
				//count++
				return DiskAdd;
			}
			if (input.buttonEnterDown()) {
				return AcceptDisk2;
			}
			return Waiting;
		}
	},
	
	DiskAdd {
		@Override
		public State next(Input input) {
			if (!input.touchDown()) {
				return Waiting;
			} else {
				return DiskAdd;
			}
			
		}
	},
	
	AcceptDisk2 {
		@Override
		public State next(Input input) {
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
		public State next(Input input) {
			//input from the color sensor
			//display nr of disks, plus nr of black en white
			if (colorSensorWhite() && !colorSensorBlack() && !colorSensorGrey()) {
				//do count--
				//do motor angle++
				//do black counter++
				//wait until done
				return AcceptDisk2;
			} else if (colorSensorBlack() &&)
			
		}
	},
	
	WaitForButton { //sample
		@Override
		public State next(Input input) {
			if (input.buttonSSDown()) {
				return Rest;
			}
			
			LCD.drawString("Waiting for a button", 0, 0);
			return WaitForButton; 
		}
	},
	
	Exit {
		@Override 
		public State next(Input input) {
			return Exit;
		}
	}
}
