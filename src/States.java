/*
 * Defines all states, and how input and the current state define
 * 	the output and the new current state.
 */
enum States implements State {
	Initial {
		@Override
		public State next(Input i, Output o) {
			o.setLed("orange", 1);
			return moveBetweenTeeth;
		}
	},
	
	moveBetweenTeeth {
		@Override
		public State next(Input i, Output o) {
			i.updateColor();
			
			if(i.colorSensorTeeth()) {
				o.motorTurnHalfTeeth(true);
			}
			
			return findFirstPoint;
		}
	},
	
	findFirstPoint {
		@Override
		public State next(Input i, Output o) {
			i.updateColor();
			
			if(i.colorSensorTeeth()) {
				o.setFirstCaliPoint();
				o.motorTurnHalfTeeth(true);
				return findSecondPoint;
			} else {
				o.motorTurnSmallStep(false);
				return findFirstPoint;
			}
		}
	},
	
	findSecondPoint {
		@Override
		public State next(Input i, Output o) {
			i.updateColor();
			
			if(i.colorSensorTeeth()) {
				o.setSecondCaliPoint();
				o.motorMoveInbetweenCaliPoints();
				return Rest;
			} else {
				o.motorTurnSmallStep(true);
				return findSecondPoint;
			}
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
				o.setLed("green", 2);
				return CheckDiskPresent;
			} else if (i.touchDown()) {
				o.setLed("red", 2);
				return InsertedEarly;
			}
			return Rest;
		}
	},
	
	InsertedEarly {
		@Override
		public State next(Input i, Output o) {
			o.tooEarly();
			if (i.buttonNoDown() || i.buttonSSDown() || i.buttonYesDown()) {
				o.setLed("green", 0);
				return CheckDiskPresent;
			}
			return InsertedEarly;
		}
	},
	
	CheckDiskPresent {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data. 
			
			if (i.colorSensorGrey()&&!i.colorSensorBlack()&&!i.colorSensorWhite()) {
				o.setCounterToZero();
				o.setLed("orange", 1);
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
				o.setLed("green", 0);
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
				o.setLed("green", 0);
				return ExpectsDisk;
			} else {
				o.setLed("orange", 1);
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
				o.setLed("red", 2);
				return AskUser;
			} else {
				o.tubeEmpty(); // Press any button to continue
				
				if (i.buttonYesDown() || i.buttonNoDown() || i.buttonSSDown()) {
					o.setLed("orange", 1);
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
				o.setLed("orange", 1);
				return Rest;
			} else if (i.buttonNoDown()) {
				o.setLed("green", 0);
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
				o.setLed("red", 2);
				if (i.buttonNoDown() || i.buttonSSDown() || i.buttonYesDown()){
					o.setLed("orange", 1);
					return Rest;
				}
			} else {//all three are false
				o.anotherColor();
				o.setLed("red", 2);
				if (i.buttonNoDown() || i.buttonSSDown() || i.buttonYesDown()){
					o.setLed("orange", 1);
					return Rest;
				}
			}
			
			return ExpectsDisk;
		}
	},
	
	Exit {
		@Override 
		public State next(Input i, Output o) {
			return Exit;
		}
	}
}
