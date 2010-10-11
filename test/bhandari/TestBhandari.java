/**
 * Copyright 2010 Russ Weeks rweeks@newbrightidea.com
 * Licensed under the GNU LGPL
 * License details here: http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package bhandari;

import graph.Network;
import graph.Device;
import graph.Link;
import graph.Utils;

import java.util.List;

/**
 * Sample topology looks like this:
 *   C-D
 *  /   \
 *  B   E
 *  \   /
 *   A-F
 *  /   \
 * G    J
 * \   /
 *  H-I
 *
 * All links have weight 1.  Shortest edge-disjoint pair
 * between (B,J) is (B,C,D,E,F,J), (B,A,G,H,I,J}
 */
public class TestBhandari {

  private static final String[] DEVICES =
          { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

  private static final String[][] LINKS =
          { {"A", "B"}, {"B", "C"}, {"C", "D"}, {"D", "E"}, {"E","F"},
            {"A", "G"}, {"G", "H"}, {"H", "I"}, {"I", "J"}, {"J", "F"},
            {"A", "F"} };

  private Network nw;

  public TestBhandari()
  {

  }

  public void init()
  {
    nw = new Network();
    for ( String deviceName: DEVICES )
    {
      nw.addDevice(deviceName);
    }
    for ( String[] link: LINKS )
    {
      nw.connectDevices(link[0], link[1], 1.0d);
    }
  }

  public void testEdgeDisjointShortestPair()
  {
    Device src = nw.getDevice( "B" );
    Device dest = nw.getDevice( "J" );
    List<List<Link>> paths = EdgeDisjointShortestPair.findDisjointPair(nw, src, dest);
    for ( List<Link> path: paths )
    {
      System.out.println( "Path: " );
      Device emergentDevice = src;
      for ( Link hop: path )
      {
        System.out.print( "  Hop: " );
        System.out.print( emergentDevice );
        emergentDevice = Utils.getOtherEndpoint(hop, emergentDevice );
        System.out.println( " -> " + emergentDevice );
      }
    }
  }

  public static void main( String[] args )
  {
    TestBhandari test = new TestBhandari();
    test.init();
    test.testEdgeDisjointShortestPair();
  }
}
