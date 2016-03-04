import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Output {
	// Store the state variables object.
	private StateVariables sv;
	private int turndegrees 288;
	
	public Output(StateVariables sv) {
		this.sv = sv;
	}
	
	String currentMessage = "";
	public void TubeEmpty()
	{
		currentMessage = "Tube is empty! Press any button";
	}
	public void WaitForInput() {
		currentMessage = "The tube is empty, waiting for input";
	}
	public void AskIfEmpty() {
		currentMessage = "Is the tube empty? Yes or No?";
	}
	public void TubeNotEmpty()
	{
		currentMessage = "Tube not empty.Sorting";
	}
	public void AskUser()
	{
		currentMessage = "Tube should be empty but disk detected, should the machine stop?";
	}
	public void Break()
	{
		currentMessage = "Break. Resting..";
	}
	public void NotBreak()
	{
		currentMessage = "No break. Sorting..";
	}
	public void Start()
	{
		currentMessage = "Starting..";
	}
	public void NoDisk()
	{
		currentMessage = "No disk detected";
	}
	public void AnotherColor()
	{
		currentMessage = "Different color disk";
	}
	public void setMessage()
	{
		LCD.clearDisplay();
		LCD.drawString(currentMessage, 0, 0);
	}
	
	public void MotorSortBlack()  // When the colorsensor detects a black disk, turn one right.
	{
		Motor.A.rotate(turndegrees, false);
	}
	
	public void MotorSortWhite() { // When the colorsensor detects a white disk, turn one left.
		Motor.A.rotate(turndegrees, false);
	}
}
