import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.ConfigurationPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

import lejos.hardware.Device;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.ConfigurationPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3SensorConstants;

public class SensorDCTest implements EV3SensorConstants {

	public static void main(String[] args) {
		ConfigurationPort configPort = SensorPort.S1.open(ConfigurationPort.class);
		ConfigurationPort motorPort = MotorPort.A.open(ConfigurationPort.class);
		
		while(!Button.ESCAPE.isDown()) {
			LCD.drawString(configPort.getPortType() + " " + CONN_NONE, 0, 0);
			LCD.drawString(motorPort.getPortType() + "", 0, 1);
			Delay.msDelay(50);
		}
	}

}
