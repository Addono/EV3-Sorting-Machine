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
			o.AskIfEmpty(); //is the time empty?
			if (i.buttonYesDown()) {
				return Waiting;
			} else if(i.buttonNoDown()) {
				return CheckDiskPresent;
			} else {
				return Rest;
			}
		}
	},
	
	CheckDiskPresent {
		@Override
		public State next(Input i, Output o) {
			i.updateColor();
			if (i.colorSensorGrey()) {
				return Waiting;
			} else if (i.colorSensorBlack() || i.colorSensorWhite()) {
				return SortDisksNoCounting;
			} else {
				return CheckDiskPresent;
			}
		}
	},
	
	Waiting {
		@Override
		public State next(Input i, Output o) {
			if (!CountIsGreaterThanZero()) {
					o.WaitForInput();
			} else {
				// Display nr. of inserted disks and enter to start the sorting
			}
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
	
	ExpectsFinished {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); 
			if (i.colorSensorBlack() || i.colorSensorWhite()) {
				o.AskUser(); //should the machine stop
				return AskUser;
			} else {
				o.TubeEmpty(); //press any button to continue
				if (i.buttonYesDown() && i.buttonNoDown() && i.buttonSSDown()) {
					return Rest;
				} else {
					return ExpectsFinished;
				}
			}
		}
	},
	
	AskUser {
		@Override
		public State next(Input i, Output o) {
			if (i.buttonYesDown()) {
				return Rest;
			} else if (i.buttonNoDown()) {
				return AcceptDisk2;
			} else {
				return AskUser;
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
				o.MotorSortWhite();
				//do white counter++
				//wait until done
				return AcceptDisk2;
			} else if (i.colorSensorBlack() && !i.colorSensorWhite() && !i.colorSensorGrey()) {
				//do count--
				o.MotorSortBlack();
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
