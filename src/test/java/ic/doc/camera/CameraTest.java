package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  Camera camera = new Camera(sensor, memoryCard);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {
    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
    }});

    camera.powerOn();
  }

  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {
    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerDown();
    }});

    switchingTheCameraOnPowersUpTheSensor();
    camera.powerOff();
  }

  @Test
  public void pressingTheShutterWhenThePowerIsOffDoesNothing() {
    context.checking(new Expectations() {{
      exactly(0).of(memoryCard).write(exactly(0).of(sensor).readData());
    }});

    switchingTheCameraOffPowersDownTheSensor();
    camera.pressShutter();
  }

  @Test
  public void pressingTheShutterWhenThePowerIsOnCopiesDataFromSensorToMemoryCard() {
    context.checking(new Expectations() {{
      exactly(1).of(memoryCard).write(exactly(1).of(sensor).readData());
    }});

    switchingTheCameraOnPowersUpTheSensor();
    camera.pressShutter();
  }

  @Test
  public void ifDataCurrentlyWritingThenSwitchingCameraOffDoesNotPowerDownSensor() {
    context.checking(new Expectations() {{
      exactly(0).of(sensor).powerDown();
    }});

    pressingTheShutterWhenThePowerIsOnCopiesDataFromSensorToMemoryCard();
    camera.powerOff();
  }

  @Test
  public void onceDataWritingCompleteThenCameraPowersDownSensor() {
    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerDown();
    }});

    ifDataCurrentlyWritingThenSwitchingCameraOffDoesNotPowerDownSensor();
    camera.writeComplete();
  }

  @Test
  public void canNotWriteNewImageWhenAlreadyWriting() {
    context.checking(new Expectations() {{
      exactly(0).of(memoryCard).write(exactly(0).of(sensor).readData());
    }});

    pressingTheShutterWhenThePowerIsOnCopiesDataFromSensorToMemoryCard();
    camera.pressShutter();
  }

  @Test
  public void ifWriteCompleteAndCameraOnThenSensorDoesNotTurnOff() {
    context.checking(new Expectations() {{
      exactly(0).of(sensor).powerDown();
    }});

    pressingTheShutterWhenThePowerIsOnCopiesDataFromSensorToMemoryCard();
    camera.writeComplete();
  }

}
