/*
 * Defines all states, and how input and the current state define
 * 	the output and the new current state.
 */
enum States implements State {
	Initial {
		@Override
		public State next(Input i, Output o) {
			return Rest;
		}
	},
	
	Rest {
		@Override
		public State next(Input i, Output o) {
			o.askIfEmpty(); // Ask the user if the tube is empty.
			
			if (i.buttonYesDown()) {
				o.setCounterToZero();
				return Waiting;
			} else if(i.buttonNoDown()) {
				return CheckDiskPresent;
			}
			
			return Rest;
		}
	},
	
	CheckDiskPresent {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data. 
			
			if (i.colorSensorGrey()&&!i.colorSensorBlack()&&!i.colorSensorWhite()) {
				o.setCounterToZero();
				return Waiting;
			} else if (i.colorSensorBlack() || i.colorSensorWhite()) {
				return SortDisksNoCounting;
			}
			
			return CheckDiskPresent;
		}
	},
	
	SortDisksNoCounting {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data.
			
			if (i.colorSensorGrey()) {
				return CheckDiskPresent;
			} else if (i.colorSensorBlack()) {
				o.motorSortBlack();
				return SortDisksNoCounting;
			} else if (i.colorSensorWhite()) {
				o.motorSortWhite();
				return SortDisksNoCounting;
			} else {
				o.anotherColor();
				if (i.buttonNoDown() || i.buttonYesDown() || i.buttonSSDown()) {
					o.motorSortWhite();
					return SortDisksNoCounting;
				}
				
			}
			
			return SortDisksNoCounting;
		}
	},
	
	Waiting {
		@Override
		public State next(Input i, Output o) {
			if (!i.counterGreaterThanZero()) {
				o.waitForInput();
			} else {
				o.enterToSort();
			}
			
			if (i.touchDown()) {
				o.increaseCounter();
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
			}
			
			return DiskAdd;
		}
	},
	
	AcceptDisk2 {
		@Override
		public State next(Input i, Output o) {
			// Display count
			if (i.counterGreaterThanZero()) {
				return ExpectsDisk;
			} else {
				return ExpectsFinished;
			}
		}
	},
	
	ExpectsFinished {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data.
			
			if ((i.colorSensorBlack() || i.colorSensorWhite())&&!i.colorSensorGrey()) {
				o.askUser(); // Should the machine stop
				return AskUser;
			} else {
				o.tubeEmpty(); // Press any button to continue
				
				if (i.buttonYesDown() || i.buttonNoDown() || i.buttonSSDown()) {
					return Rest;
				} 
			}
			
			return ExpectsFinished;
		}
	},
	
	AskUser {
		@Override
		public State next(Input i, Output o) {
			o.askUser();
			if (i.buttonYesDown()) {
				return Rest;
			} else if (i.buttonNoDown()) {
				return ExpectsDisk;
			} else {
				return AskUser;
			}
			
		}
	},
	
	ExpectsDisk {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data.
			
			//display nr. of disks, plus nr. of black and white
			if (i.colorSensorWhite() && !i.colorSensorBlack() && !i.colorSensorGrey()) {
				o.decreaseCounter();
				o.motorSortWhite();
				o.increaseWhiteCounter();
				//wait until done
				return AcceptDisk2;
			} else if (i.colorSensorBlack() && !i.colorSensorWhite() && !i.colorSensorGrey()) {
				o.decreaseCounter();
				o.motorSortBlack();
				o.increaseBlackCounter();
				//wait until done
				return AcceptDisk2;
			} else if (!i.colorSensorBlack() && !i.colorSensorWhite() && i.colorSensorGrey()) {
				o.stuckInTube();
				if (i.buttonNoDown() || i.buttonSSDown() || i.buttonYesDown()){
					return Rest;
				}
			} else {//all three are false
				o.anotherColor();
				if (i.buttonNoDown() || i.buttonSSDown() || i.buttonYesDown()){
					return Rest;
				}
			}
			
		}
	},
	
	Exit {
		@Override 
		public State next(Input i, Output o) {
			return Exit;
		}
	}
}
