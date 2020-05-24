package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  TorpedoStore pts;
  TorpedoStore sts;

  @BeforeEach
  public void init(){

    pts = mock(TorpedoStore.class);
    sts = mock(TorpedoStore.class);

    when(pts.isEmpty()).thenReturn(false);
    when(sts.isEmpty()).thenReturn(false);
    when(pts.fire(any(Integer.class))).thenReturn(true);
    when(sts.fire(any(Integer.class))).thenReturn(true);

    this.ship = new GT4500(pts, sts);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    // Assert
    assertEquals(true, result);
    
    boolean done = false;

    try { // verify that only one store was fired
      verify(pts).fire(any(Integer.class));
      verify(sts, never()).fire(any(Integer.class));
    } catch(Exception e) {
      if (!done) {
        verify(sts).fire(any(Integer.class));
        verify(pts, never()).fire(any(Integer.class));
      }
    }
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(pts).fire(any(Integer.class));
    verify(sts).fire(any(Integer.class));
  }

  @Test
  public void consecutiveAllFiring() {

    boolean result = ship.fireTorpedo(FiringMode.ALL);
    when(pts.isEmpty()).thenReturn(true);
    when(sts.isEmpty()).thenReturn(true);

    assertEquals(true, result);
    verify(pts,times(1)).fire(any(Integer.class));
    verify(sts,times(1)).fire(any(Integer.class));

    result = ship.fireTorpedo(FiringMode.ALL);
    assertEquals(false, result);
    verify(pts,times(1)).fire(any(Integer.class));
    verify(sts,times(1)).fire(any(Integer.class));
  }

  @Test
  public void consecutiveSingleFiring() {

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    when(pts.isEmpty()).thenReturn(true);

    assertEquals(true, result);
    verify(pts,times(1)).fire(any(Integer.class));
    verify(sts,never()).fire(any(Integer.class));
    result = ship.fireTorpedo(FiringMode.SINGLE);
    assertEquals(true, result);
    verify(pts,times(1)).fire(any(Integer.class));
    verify(sts,times(1)).fire(any(Integer.class));
  }

  @Test
  public void switchOnEmptyStore() {
    when(pts.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    assertEquals(true, result);
    verify(pts,never()).fire(any(Integer.class));
    verify(sts,times(1)).fire(any(Integer.class));

  }

  @Test
  public void dontSwitchOnFaultyStore() {
    when(pts.fire(any(Integer.class))).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    assertEquals(false, result);

    verify(sts,never()).fire(any(Integer.class));

  }

  @Test
  public void singleAllFire() {

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    when(pts.isEmpty()).thenReturn(true);

    assertEquals(true, result);
    verify(pts,times(1)).fire(any(Integer.class));
    verify(sts,never()).fire(any(Integer.class));
    result = ship.fireTorpedo(FiringMode.ALL);
    assertEquals(false, result);
    verify(pts,times(1)).fire(any(Integer.class));
    verify(sts,times(1)).fire(any(Integer.class));

  }

  /* New test: firing should fail if null is passed as firing mode. */

  @Test
  public void invalidFiringMode() {

    boolean result = ship.fireTorpedo(null);

    assertEquals(false, result);
  }


}
