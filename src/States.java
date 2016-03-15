/*
 * Defines all states, and how input and the current state define
 * 	the output and the new current state.
 */
enum States implements State {
	Initial {
		@Override
		public State next(Input i, Output o) {
			o.isCalibrating();
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
				o.motorMoveInBetweenCaliPoints();
				o.askIfEmpty(); // Ask the user if the tube is empty.
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
			if (i.buttonYesDown()) {
				o.setCounterToZero();
				return Waiting;
			} else if(i.buttonNoDown()) {
				return CheckDiskPresent;
			} else if (i.touchDown()) {
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
				return CheckDiskPresent;
			}
			return InsertedEarly;
		}
	},
	
	CheckDiskPresent {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data. 
			
			if (i.colorSensorIsNoDisk() && !i.colorSensorBlack() && !i.colorSensorWhite()) {
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
			
			if (i.colorSensorIsNoDisk()) {
				return CheckDiskPresent;
			} else if (i.colorSensorBlack()) {
				o.sorting();
				o.motorSortBlack();
				return SortDisksNoCounting;
			} else if (i.colorSensorWhite()) {
				o.sorting();
				o.motorSortWhite();
				return SortDisksNoCounting;
			} else {
				o.anotherColor();
				if (i.buttonSSDown()) {
					o.sorting();
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
			
			if ((i.colorSensorBlack() || i.colorSensorWhite()) && !i.colorSensorIsNoDisk()) {
				o.askUser(); // Should the machine stop
				return AskUser;
			} else {
				o.tubeEmpty(); // Press any button to continue
				
				if (i.buttonYesDown() || i.buttonNoDown() || i.buttonSSDown()) {
					o.askIfEmpty(); // Ask the user if the tube is empty.
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
				o.askIfEmpty(); // Ask the user if the tube is empty.
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
			
			if (i.colorSensorWhite() && !i.colorSensorBlack() && !i.colorSensorIsNoDisk()) {
				o.decreaseCounter();
				o.motorSortWhite();
				o.increaseWhiteCounter();
				return AcceptDisk2;
			} else if (i.colorSensorBlack() && !i.colorSensorWhite() && !i.colorSensorIsNoDisk()) {
				o.decreaseCounter();
				o.motorSortBlack();
				o.increaseBlackCounter();
				return AcceptDisk2;
			} else if (!i.colorSensorBlack() && !i.colorSensorWhite() && i.colorSensorIsNoDisk()) {
				o.stuckInTube();
				
				if (i.buttonSSDown()) {
					o.askIfEmpty(); // Ask the user if the tube is empty.
					return Rest;
				}
			} else {// If the color sensor is indecisive.
				o.anotherColor();
				
				if (i.buttonSSDown()) {
					o.askIfEmpty(); // Ask the user if the tube is empty.
					return Rest;
				}
			}
			
			return ExpectsDisk;
		}
	}
}
