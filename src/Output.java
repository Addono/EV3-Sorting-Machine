import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Output {
	
	String currentMessage = "";
	public void TubeEmpty()
	{
		currentMessage = "Tube is empty!";
	}
	public void TubeNotEmpty()
	{
		currentMessage = "Tube not empty.Sorting";
	}
	public void AskUser()
	{
		currentMessage = "Disk detected(error).Break?";
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
}
