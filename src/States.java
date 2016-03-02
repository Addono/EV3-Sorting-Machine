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
			LCD.drawString("Resting now", 0, 0);
			return Rest;
		}
	},
	
	WaitForButton {
		@Override
		public State next(Input input) {
			if (input.isEnterPressed()) {
				LCD.drawString("Waiting for a button", 0, 0);
				return Rest;
			}
			
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
