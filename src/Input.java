import lejos.hardware.Button;
import lejos.hardware.port.ConfigurationPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.EV3SensorConstants;

public class Input implements EV3SensorConstants {
	// Store the state variables object.
	private StateVariables sv;

	// Initialize variables for the color variables.
	private EV3ColorSensor colorSensor;
	private SampleProvider colorRGB;
	private float[] RGB = new float[3];
	private float RGBAvg, redPercentage;

	// Initialize all port variables. Needed to find in which port the motor is.
	private Port[] ports = new Port[4];
	ConfigurationPort[] configPorts = new ConfigurationPort[4];
	int colorPort, touchPort;
	EV3TouchSensor touchSensor;

	// Initialize variables for the touch sensor and buttons.
	private float[] isTouched = new float[1];
	private boolean enterWasDown = false;

	public Input(StateVariables sv) {
		this.sv = sv;

		initializeSensors();
	}

	// 				HANDLE COLOR INPUT

	/**
	 * Initialize sensors, it will search all four sensor ports
	 * for the touch and color sensor.
	 */
	void initializeSensors() {
		// Define all ports.
		ports[0] = SensorPort.S1;
		ports[1] = SensorPort.S2;
		ports[2] = SensorPort.S3;
		ports[3] = SensorPort.S4;

		// Open all configuration ports.
		for (int i = 0; i < ports.length; i++) {
			configPorts[i] = ports[i].open(ConfigurationPort.class);
		}

		// Wait until both a touch and a color sensor are detected.
		updateSensors();
		while (touchPort == -1 || colorPort == -1) {
			updateSensors();
		}

		// Close all ports, to allow the data of the sensors to be read.
		for (int i = 0; i < configPorts.length; i++) {
			configPorts[i].close();
		}

		// Set the touch and color sensor.
		touchSensor = new EV3TouchSensor(ports[touchPort]);
		colorSensor = new EV3ColorSensor(ports[colorPort]);

		// Create the RGB color fetcher.
		colorRGB = colorSensor.getRGBMode();
	}

	/**
	 * Update sensor position.
	 */
	private boolean updateSensors() {
		colorPort = touchPort = -1; // Reset all values.

		for (int i = 0; i < configPorts.length; i++) {
			int portType = configPorts[i].getPortType();

			// Find which sensor currently is in this port.
			switch (portType) {
			case CONN_INPUT_UART:
				if (colorPort != -1) {
					// Error: Two light sensors?
					return false;
				}

				colorPort = i;
				break;

			case CONN_INPUT_DUMB:
				if (touchPort != -1) {
					// Error: Two touch sensors?
					return false;
				}

				touchPort = i;
				break;

			case CONN_ERROR:
				// Error: Unexpected sensor for this port, motor
				// in sensor port or different EV3 sensor than color
				// or touch?
				return false;
			}
		}

		return true; // Return true if no errors where detected.
	}

	/**
	 * Fetches new colors from the sensor which replace the old values.
	 * 
	 * Note: Call them before getting color input which relies on this data,
	 * else old information will be used.
	 */
	public void updateColor() {
		colorRGB.fetchSample(RGB, 0); // Get the RGB values.

		RGBAvg = (RGB[0] + RGB[1] + RGB[2]) / 3;
		redPercentage = 100 * RGB[0] / (3 * RGBAvg);
	}

	/**
	 * Returns if a white disk is in front of the sorting wheel.
	 * @return True if the disk in front of the sensor is white, else false.
	 */
	public boolean colorSensorWhite() {
		return RGBAvg >= .2;
	}

	/**
	 * Returns if a black disk is in front of the sorting wheel.
	 * @return True if the disk in front of the sensor is black.
	 */
	public boolean colorSensorBlack() {
		return .2 > RGBAvg && !colorSensorIsNoDisk();
	}

	/**
	 * Returns if no disk is in front of the sorting wheel.
	 * @return True if no disk is in front of the sensor, else false.
	 */
	public boolean colorSensorIsNoDisk() {
		return redPercentage > 25 && RGB[0] <= 0.04f;
	}

	/**
	 * Returns if a teeth of the wheel is in front of the sensor.
	 * @return True if a wheel teeth is in front of the color sensor.
	 */
	public boolean colorSensorTeeth() {
		return RGB[0] > 0.04f && redPercentage > 30;
	}

	/**
	 * Checks if the touch sensor is pressed.
	 * @return True if the sensor is pressed, false if it isn't.
	 */
	public boolean touchDown() {
		// Get new values from the touch sensor.
		if (touchPort != -1) {
			touchSensor.fetchSample(isTouched, 0);

			// Convert result of the touch sensor to a boolean value.
			if (isTouched[0] == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	// 				HANDLE TOUCH SENSOR INPUT

	/**
	 * Returns if the start/stop button is pressed.
	 * @return True if the start/stop button is pressed, false if it isn't.
	 */
	public boolean buttonEnterDown() {
		return Button.ENTER.isDown();
	}
	
	public boolean buttonEnterPressed() {
		boolean isDown = buttonEnterDown();
		boolean isPressed = isDown && !enterWasDown;
		
		enterWasDown = isDown;
		
		return isPressed;
	}

	/**
	 * Returns if the Yes button is pressed.
	 * @return True if the Yes button is pressed, false if it isn't.
	 */
	public boolean buttonYesDown() {
		return Button.RIGHT.isDown();
	}

	/**
	 * Returns if the No button is pressed.
	 * @return True if the No button is pressed, false if it isn't.
	 */
	public boolean buttonNoDown() {
		return Button.LEFT.isDown();
	}

	// HANDLE STATE VARIABLES

	/**
	 * Returns if the disk counter is zero.
	 * @return True if the disk counter is greater than zero, else false.
	 */
	public boolean counterGreaterThanZero() {
		return sv.counterGreaterThanZero();
	}
	
	/**
	 * Retrieves the motor stalled state variable.
	 * @return The value of the motor stalled state variable.
	 */
	public boolean getMotorStalled() {
		return sv.getMotorStalled();
	}
}