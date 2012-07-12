/*
 * Program to display JVM memory usage in a JLChart.
 * Created on February 1, 2009
 * By Kenneth Evans, Jr.
 */

package jlchartdemos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import utils.Utils;
import fr.esrf.tangoatk.widget.util.chart.DataList;
import fr.esrf.tangoatk.widget.util.chart.IJLChartActionListener;
import fr.esrf.tangoatk.widget.util.chart.JLAxis;
import fr.esrf.tangoatk.widget.util.chart.JLChart;
import fr.esrf.tangoatk.widget.util.chart.JLChartActionEvent;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;

/**
 * A demo application showing a dynamically updated chart that displays the
 * current JVM memory usage.
 */
public class JLChartMemoryUsage extends JPanel
{
  private static final boolean verbose = false;
  private static final boolean debug = false;
  private static final long serialVersionUID = 1L;
  /**
   * Scale to MBytes.
   */
  private static final double SCALE = 1. / 1048576.;
  private static final int DEFAULT_INTERVAL = 1000;
  private static final int DEFAULT_AGE = 60000;

  private boolean doMax = true;
  private JLChart chart = null;
  private JLDataView total;
  private JLDataView used;
  private JLDataView max;
  private Timer timer = null;
  private int interval = DEFAULT_INTERVAL;
  private int maxAge = DEFAULT_AGE;
  private String header = null;
  private boolean showLegend = true;

  // private JCheckBoxMenuItem showLegendMenuItem = null;

  /**
   * JLChartMemoryUsage constructor
   */
  public JLChartMemoryUsage() {
    this(DEFAULT_AGE);
  }

  /**
   * JLChartMemoryUsage constructor
   * 
   * @param maxAge The maximum age (in milliseconds).
   */
  public JLChartMemoryUsage(int maxAge) {
    super(new BorderLayout());
    this.maxAge = maxAge;

    chart = new JLChart();
    chart.setHeader(getName());
    chart.setHeaderFont(new Font("Dialog", Font.BOLD, 18));
    chart.setDisplayDuration(maxAge);

    chart.getXAxis().setName("Time");
    chart.getXAxis().setAutoScale(true);
    chart.getXAxis().setGridVisible(true);
    chart.getXAxis().setSubGridVisible(true);
    chart.getXAxis().setAnnotation(JLAxis.TIME_ANNO);
    chart.getXAxis().setAxisDuration(maxAge);
    chart.getXAxis().setFitXAxisToDisplayDuration(true);

    chart.getY1Axis().setName("Memory, MB");
    chart.getY1Axis().setAutoScale(true);
    chart.getY1Axis().setGridVisible(true);
    chart.getY1Axis().setSubGridVisible(true);

    total = new JLDataView();
    total.setColor(new Color(0, 0, 255));
    total.setName("Total Memory");

    used = new JLDataView();
    used.setColor(new Color(0, 0, 0));
    used.setName("Used Memory");

    max = new JLDataView();
    max.setColor(new Color(255, 0, 0));
    max.setName("Max Memory");

    chart.getY1Axis().addDataView(total);
    chart.getY1Axis().addDataView(used);
    chart.getY1Axis().addDataView(max);

    // Modify the context menu
    extendPopupMenu();

    // Add the legend
    if(showLegend) createLegend(chart);

    add(chart, BorderLayout.CENTER);
  }

  /**
   * Returns a String with configuration information.
   * 
   * @return
   */
  public String getConfiguration() {
    String ls = Utils.LS;
    String info = "";
    info += "Chart Configuration" + ls;
    info += chart.getConfiguration() + ls;
    JLAxis axis = chart.getXAxis();
    info += "X Axis: " + axis.getAxeName() + ls;
    info += axis.getConfiguration("  ") + ls;
    info += "  JLDataViews:" + ls;
    Vector<JLDataView> views = axis.getViews();
    int i = 0;
    for(JLDataView dv : views) {
      info += "    DataView " + i + " " + dv.getName() + ls;
      info += dv.getConfiguration("      ") + ls;
    }
    axis = chart.getY1Axis();
    info += "Y1 Axis: " + axis.getAxeName() + ls;
    info += axis.getConfiguration("  ") + ls;
    info += "  JLDataViews:" + ls;
    views = axis.getViews();
    i = 0;
    for(JLDataView dv : views) {
      info += "    DataView " + i + " " + dv.getName() + ls;
      info += dv.getConfiguration("      ") + ls;
    }

    return info;
  }

  private void extendPopupMenu() {
    if(chart == null) return;
    // There is already a separator before the added items
    // chart.addSeparator();

    // Remove standard items
    if(true) {
      chart.removeMenuItem(JLChart.MENU_TABLE);
      chart.removeMenuItem(JLChart.MENU_STAT);
      chart.removeMenuItem(JLChart.MENU_DATASAVE);
      // No variable for abscissa margin, save snapshot
    }

    // Show Max
    chart.addJLChartActionListener(new IJLChartActionListener() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(JLChartActionEvent evt) {
        if(false) {
          System.out.println("actionPerformed");
          System.out.println(evt);
          System.out.println(evt.getName());
          System.out.println(evt.getState());
        }
        if(!evt.getName().equals("Toggle Max Memory")) return;
        toggleMax();
      }

      public boolean getActionState(JLChartActionEvent evt) {
        if(false) {
          System.out.println("getActionState");
          System.out.println(evt);
          System.out.println(evt.getName());
          System.out.println(evt.getState());
        }
        // Only used with chk
        return false;
      }

    });
    chart.addUserAction("Toggle Max Memory");

    // Show Legend
    chart.addJLChartActionListener(new IJLChartActionListener() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(JLChartActionEvent evt) {
        if(!evt.getName().equals("Toggle Legend")) return;
        chart.setLabelVisible(!chart.isLabelVisible());
      }

      public boolean getActionState(JLChartActionEvent evt) {
        // Only used with chk
        return false;
      }

    });
    chart.addUserAction("Toggle Legend");

    // Interval
    chart.addJLChartActionListener(new IJLChartActionListener() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(JLChartActionEvent evt) {
        if(!evt.getName().equals("Set Interval...")) return;
        queryResetInterval();
      }

      public boolean getActionState(JLChartActionEvent evt) {
        // Only used with chk
        return false;
      }

    });
    chart.addUserAction("Set Interval...");

    // Max Age
    chart.addJLChartActionListener(new IJLChartActionListener() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(JLChartActionEvent evt) {
        if(!evt.getName().equals("Set Max Age...")) return;
        queryResetMaxAge();
      }

      public boolean getActionState(JLChartActionEvent evt) {
        // Only used with chk
        return false;
      }

    });
    chart.addUserAction("Set Max Age...");
  }

  public static void createDefaultLegend(JLChart chart) {
    // if(chart == null) return;
    // LegendTitle legend = new LegendTitle(chart.getPlot());
    // if(legend == null) return;
    // legend.setPosition(RectangleEdge.BOTTOM);
    // legend.setMargin(new RectangleInsets(3, 3, 3, 3));
    // legend.setBackgroundPaint(Color.white);
    // legend.setBorder(new BlockBorder());
    // chart.addLegend(legend);
  }

  public static void createLegend(JLChart chart) {
    // if(chart == null) return;
    // LegendTitle legend = new LegendTitle(chart.getPlot());
    // legend.setPosition(RectangleEdge.BOTTOM);
    // if(legend == null) return;
    // // legend.setMargin(new RectangleInsets(3, 3, 3, 3));
    // // legend.setBackgroundPaint(Color.white);
    // // legend.setBorder(new BlockBorder());
    // chart.addLegend(legend);
  }

  /**
   * Bring up a dialog to set the interval.
   */
  public void queryResetInterval() {
    String result = JOptionPane.showInputDialog("Enter update interval in ms",
      Integer.toString(interval));
    if(result != null) {
      int newVal = 0;
      try {
        newVal = Integer.valueOf(result).intValue();
      } catch(NumberFormatException ex) {
        Utils.errMsg("Invalid interval");
        return;
      }
      setInterval(newVal);
    }
  }

  /**
   * Bring up a dialog to set the maxAge.
   */
  public void queryResetMaxAge() {
    String result = JOptionPane.showInputDialog("Enter maximum age in ms",
      Integer.toString(maxAge));
    if(result != null) {
      int newVal = 0;
      try {
        newVal = Integer.valueOf(result).intValue();
      } catch(NumberFormatException ex) {
        Utils.errMsg("Invalid age");
        return;
      }
      setMaxAge(newVal);
    }
  }

  /**
   * Toggle whether the maximum value is shown.
   */
  public void toggleMax() {
    if(max == null) return;
    if(doMax) {
      doMax = false;
      chart.getY1Axis().removeDataView(max);
    } else {
      doMax = true;
      chart.getY1Axis().addDataView(max);
    }
  }

  /**
   * Starts the updating, stopping any previous updating first. Note that the
   * timer may continue to run for a long time after references to it are gone.
   * See the documentation for java.util.Timer (even though this class uses
   * javax.swing.Timer).
   */
  public void start() {
    stop();
    timer = new Timer(interval, new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        update();
      }
    });
    timer.start();
  }

  /**
   * Stops the updating.
   */
  public void stop() {
    if(timer != null) {
      timer.stop();
      timer = null;
    }
  }

  /**
   * @return The value of interval in ms.
   */
  public int getInterval() {
    return interval;
  }

  /**
   * @param interval The new value for interval in ms.
   */
  public void setInterval(int interval) {
    if(interval == this.interval) return;
    this.interval = interval;
    // Restart the timer if it is running, otherwise do nothing
    if(timer != null && timer.isRunning()) {
      start();
    }
  }

  /**
   * @return The value of maxAge in ms.
   */
  public int getMaxAge() {
    return maxAge;
  }

  /**
   * @param maxAge The new value for maxAge in ms.
   */
  public void setMaxAge(int maxAge) {
    if(maxAge == this.maxAge) return;
    this.maxAge = maxAge;
    chart.setDisplayDuration(maxAge);
  }

  /**
   * @return The value of header.
   */
  public String getHeader() {
    return header;
  }

  /**
   * @param header The new value for header.
   */
  public void setHeader(String header) {
    if(header == this.header) return;
    this.header = header;
    if(chart != null) chart.setHeader(header);
  }

  /**
   * @return The value of showLegend.
   */
  public boolean isShowLegend() {
    return showLegend;
  }

  /**
   * @param showLegend The new value for showLegend.
   */
  public void setShowLegend(boolean showLegend) {
    // if(showLegend == this.showLegend) return;
    // this.showLegend = showLegend;
    // if(showLegendMenuItem != null
    // && showLegendMenuItem.isSelected() != showLegend) {
    // showLegendMenuItem.setSelected(showLegend);
    // }
    // if(chart == null) return;
    // if(showLegend) {
    // createLegend(chart);
    // } else {
    // chart.removeLegend();
    // }
  }

  /**
   * @return The value of chart.
   */
  public JLChart getChart() {
    return chart;
  }

  /**
   * @return The value of doMax.
   */
  public boolean isDoMax() {
    return doMax;
  }

  /**
   * @param doMax The new value for doMax.
   */
  public void setDoMax(boolean doMax) {
    if(doMax != this.doMax) toggleMax();
  }

  /**
   * The update method for the timer.
   */
  public void update() {
    // Max memory is the maximum allowed before getting out-of-memory errors
    // Total memory is what is currently allocated
    // Free memory is what is not used of what is allocated
    // Used is total - free
    double totalMem = SCALE * Runtime.getRuntime().totalMemory();
    double freeMem = SCALE * Runtime.getRuntime().freeMemory();
    double maxMem = SCALE * Runtime.getRuntime().maxMemory();
    long time = System.currentTimeMillis();
    chart.addData(total, time, totalMem);
    chart.addData(used, time, totalMem - freeMem);
    chart.addData(max, time, maxMem);
    if(verbose) {
      System.out.println("Update: time: " + time);
      System.out.println("length=" + total.getDataLength() + " maxTime="
        + total.getMaxTime());
      DataList last = total.getLastValue();
      if(last != null) {
        // last seems to always be null
        System.out.println("lastValue=" + last.x + " " + last.y);
      }
      if(false) {
        // Print the data array
        int i = 0;
        DataList data = total.getData();
        while(data != null) {
          System.out.println(i++ + " data: " + data.x + " " + data.y);
          data = data.next;
        }
      }
    }
  }

  /**
   * Entry point for the sample application.
   * 
   * @param args ignored.
   */
  public static void main(String[] args) {
    try {
      // Set window decorations
      JFrame.setDefaultLookAndFeelDecorated(true);

      // Set the native look and feel
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      // Make the job run in the AWT thread
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          JLChartMemoryUsage panel = new JLChartMemoryUsage(600000);
          // panel.setInterval(100); // For testing
          panel.setInterval(1000);
          panel.setHeader("JVM Memory Usage");
          // panel.setDoMax(false);
          panel.start();

          if(debug) {
            System.out.println(panel.getConfiguration());
          }

          JFrame frame = new JFrame("JLChart Memory Usage");
          frame.getContentPane().add(panel, BorderLayout.CENTER);
          frame.setBounds(200, 120, 600, 280);
          // EXIT_ON_CLOSE is necessary to stop the timer
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
        }
      });
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }

}
