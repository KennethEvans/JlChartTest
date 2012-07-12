/*
 * Program to 
 * Created on Aug 15, 2006
 * By Kenneth Evans, Jr.
 */

package jlchartdemos;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import demolauncher.JLChartDemo;

public class MemoryUsageDemo extends JLChartDemo
{

  public MemoryUsageDemo() {
    super();
    setName("Memory Usage");
  }

  public String getInfo() {
    String info =
      "A dynamically-updating XY plot with a time axis.";
    return info;
  }

  public void createPanel() {
    if(created) return;

    // Make a JPanel
    JPanel panel = new JPanel();
    BorderLayout layout = new BorderLayout();
    panel.setLayout(layout);
    setPanel(panel);

    // Create a MemoryUsage panel
    JLChartMemoryUsage mu = new JLChartMemoryUsage(30000);
    mu.setInterval(100);
    mu.setHeader("JVM Memory Usage");
    mu.start();

    panel.add(mu, BorderLayout.CENTER);

    created = true;
  }

}
