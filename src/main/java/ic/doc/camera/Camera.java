package ic.doc.camera;

public class Camera implements WriteListener {

  private final Sensor cameraSensor;
  private final MemoryCard cameraMemoryCard;
  private Boolean cameraOn = false;
  private Boolean currentlyWriting = false;

  public Camera(Sensor sensor, MemoryCard memoryCard) {
    cameraSensor = sensor;
    cameraMemoryCard = memoryCard;
  }

  public void pressShutter() {
    if (cameraOn && !currentlyWriting) {
      currentlyWriting = true;
      cameraMemoryCard.write(cameraSensor.readData());
    }
  }

  public void powerOn() {
    if (!cameraOn) {
      cameraOn = true;
      cameraSensor.powerUp();
    }
  }

  public void powerOff() {
    if (cameraOn) {
      cameraOn = false;
      if (!currentlyWriting) {
        cameraSensor.powerDown();
      }
    }
  }

  @Override
  public void writeComplete() {
    currentlyWriting = false;
    if (!cameraOn) {
      cameraSensor.powerDown();
    }
  }
}

