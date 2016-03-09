import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.ConfigurationPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

import lejos.hardware.sensor.EV3SensorConstants;

public class SensorDCTest implements EV3SensorConstants {
	
	public static void main(String[] args) {
		ConfigurationPort[] ports = new ConfigurationPort[8];
		
		ports[0] = SensorPort.S1.open(ConfigurationPort.class);
		ports[1] = SensorPort.S2.open(ConfigurationPort.class);
		ports[2] = SensorPort.S3.open(ConfigurationPort.class);
		ports[3] = SensorPort.S4.open(ConfigurationPort.class);
		ports[4] = MotorPort.A.open(ConfigurationPort.class);
		ports[5] = MotorPort.B.open(ConfigurationPort.class);
		ports[6] = MotorPort.C.open(ConfigurationPort.class);
		ports[7] = MotorPort.D.open(ConfigurationPort.class);
		
		int lightSensor, touchSensor, motor;
		
		while(!Button.ESCAPE.isDown()) {
			lightSensor = touchSensor = motor = -1; // Reset all values.
			
			for(int i = 0; i < ports.length; i++) {
				int portType = ports[i].getPortType();
				
				switch(portType) {
				case CONN_INPUT_UART:
					if(lightSensor != -1) {
						// Throw error: Two light sensors?
					}
					
					lightSensor = i;
					break;
				
				case CONN_INPUT_DUMB:
					if(touchSensor != -1) {
						// Throw error: Two touch sensors?
					}
					
					touchSensor = i;
					break;
				
				case TYPE_TACHO:
				case TYPE_MINITACHO:
				case TYPE_NEWTACHO:
				case CONN_OUTPUT_TACHO:
					if(motor != -1) {
						// Throw error: Two motors connected?
					}
					
					motor = i;
					break;
				
				case CONN_ERROR:
					// Throw error: Unexpected sensor for this port, motor in sensor port or vice versa?
				}
			}
			
			LCD.drawString("Motor      : "  + motor, 0, 0);
			LCD.drawString("Touchsensor: " + touchSensor, 0, 1);
			LCD.drawString("Lightsensor: " + lightSensor, 0, 2);
			
			Delay.msDelay(50);
			LCD.clearDisplay();
		}
	}

}
