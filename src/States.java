/*
 * Defines all states, and how input and the current state define
 * 	the output and the new current state.
 */
enum States implements State {
	Initial {
		@Override
		public State next(Input i, Output o) {
			o.isCalibrating(); // Show a message that the machine is now calibrating.
			return moveBetweenTeeth; // Start the calibration at boot.
		}
	},
	
	/*
	 * If there is a teeth in front of the sensor, then turn the
	 * wheel for half a teeth to prevent the teeth to be in front
	 * of the sensor.
	 */
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
	
	/*
	 *  Turns the wheel until a teeth is found, then stores the wheel
	 *  and starts the search for the position of the second teeth.
	 */
	
	findFirstPoint {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Update the color sensor data.
			
			if(i.colorSensorTeeth()) { // Check if we found a teeth.
				o.setFirstCaliPoint(); // If so, set this as the first calibration point.
				o.motorTurnHalfTeeth(true); // And turn in the reversed direction past the current teeth.
				return findSecondPoint;
			} else {
				o.motorTurnSmallStep(false); // If not, keep turning until a teeth is found.
				return findFirstPoint;
			}
		}
	},
	
	/*
	 * Turns the wheel in search for the second teeth. It searches
	 * in the opposite direction compared to the search for the first
	 * teeth.
	 */
	findSecondPoint {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Update the color sensor data.
			
			if(i.colorSensorTeeth()) { // Check if we found the second teeth.
				o.setSecondCaliPoint(); // If so, store this as the second calibration point.
				o.motorMoveInBetweenCaliPoints(); // Turn to the calibrated point.
				o.askIfEmpty(); // Ask the user if the tube is empty.
				return Rest;
			} else {
				o.motorTurnSmallStep(true); // If not, keep turning until the second teeth is found.
				return findSecondPoint;
			}
		}
	},
	
	/*
	 * State prior to sorting, allows the user to tell the machine if the
	 * tube is:
	 * 	- Contains disks (No button): Then the machine will sort disks as
	 * long as they are in front of the sensor.
	 *  - Empty (Yes button): The machine will start counting inserted disks.
	 *  
	 *  Or if the user wants to calibrate, pressing Enter restarts the
	 *  calibration sequence.
	 *  
	 *  If a disk is inserted during this state, then we will handle this
	 *  by showing an error message and sorting the tube without counting.
	 */
	Rest {
		@Override
		public State next(Input i, Output o) {
			if (i.buttonYesDown()) {
				o.setCounterToZero();
				return Waiting;
			} else if(i.buttonNoDown()) {
				return CheckDiskPresent;
			} else if (i.touchDown()) {
				o.tooEarly();
				return InsertedEarly;
			} else if (i.buttonEnterPressed()) {
				o.isCalibrating();
				return moveBetweenTeeth;
			}
			
			return Rest;
		}
	},
	
	/*
	 * If a disk was inserted early, then wait until the user presses
	 * the Enter button and then start the sorting procedure.
	 */
	InsertedEarly {
		@Override
		public State next(Input i, Output o) {
			if (i.buttonEnterPressed()) {
				return CheckDiskPresent;
			}
			
			return InsertedEarly;
		}
	},
	
	/*
	 * Phase executed before sorting a disk, checks if as disk is present.
	 * If not, then return to the waiting state, else sort the disk.
	 */
	CheckDiskPresent {
		@Override
		public State next(Input i, Output o) {
			i.updateColor(); // Sample the sensor for new color data. 
			
			if (i.colorSensorIsNoDisk() && !i.colorSensorBlack() && !i.colorSensorWhite()) {
				o.setCounterToZero();
				return Waiting;
			} else if (i.colorSensorBlack() || i.colorSensorWhite()) {
				o.sorting();
				return SortDisksNoCounting;
			}
			
			return CheckDiskPresent;
		}
	},
	
	/*
	 * Sort the disks without using the counter.
	 */
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
				if (i.buttonEnterPressed()) {
					o.sorting();
					o.motorSortWhite();
					return SortDisksNoCounting;
				} else {
					o.anotherColor();
					return SortDisksNoCounting;
				}
			}
		}
	},
	
	/*
	 * Waits for the user to insert a disk, or to start the
	 * sorting procedure.
	 */
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
			
			if (i.buttonEnterPressed()) {
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
				o.askUnexpectedDisk(); // Ask the user what to do with the unexpected disk.
				return AskUser;
			} else {
				o.tubeEmpty(); // Press any button to continue
				
				if (i.buttonYesDown() || i.buttonNoDown() || i.buttonEnterPressed()) {
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
			if (i.buttonNoDown()) {
				o.askIfEmpty(); // Ask the user if the tube is empty.
				o.setCounterToZero();
				return Rest;
			} else if (i.buttonYesDown()) {
				o.increaseCounter(); // Apparently one - at least - more disk was present than expected, take this into account. 
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
				
				if (i.buttonEnterPressed()) {
					o.askIfEmpty(); // Ask the user if the tube is empty.
					return Rest;
				}
			} else {// If the color sensor is indecisive.
				o.anotherColor();
				
				if (i.buttonEnterPressed()) {
					o.askIfEmpty(); // Ask the user if the tube is empty.
					return Rest;
				}
			}
			
			return ExpectsDisk;
		}
	}
}
