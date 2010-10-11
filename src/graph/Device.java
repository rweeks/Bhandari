/**
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package graph;

public class Device implements Comparable<Device>{
  private String name;

  Device(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toString()
  {
    return name;
  }

  public int compareTo(Device device) {
    return name.compareTo(device.name);
  }
}
