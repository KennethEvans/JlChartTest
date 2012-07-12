/*
 * Program to 
 * Created on Dec 24, 2006
 * By Kenneth Evans, Jr.
 */

package jlchartdemos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import demolauncher.JLChartDemo;
import fr.esrf.tangoatk.widget.util.chart.JLAxis;
import fr.esrf.tangoatk.widget.util.chart.JLChart;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;

public class TrignometricFunctionsDemo extends JLChartDemo
{

  public TrignometricFunctionsDemo() {
    super();
    setName("Trignometric Functions");
  }

  public String getInfo() {
    String info = "This is a chart with trignometric functions.";
    return info;
  }

  public void createPanel() {
    if(created) return;

    // Make a JPanel
    JPanel panel = new JPanel();
    BorderLayout layout = new BorderLayout();
    panel.setLayout(layout);
    setPanel(panel);

    chart = new JLChart();
    chart.setHeader(getName());
    chart.setHeaderFont(new Font("Dialog", Font.BOLD, 18));

    chart.getXAxis().setName("x");
    chart.getXAxis().setAutoScale(true);
    chart.getXAxis().setGridVisible(true);
    chart.getXAxis().setSubGridVisible(true);
    chart.getXAxis().setAnnotation(JLAxis.VALUE_ANNO);

    chart.getY1Axis().setAutoScale(true);
    chart.getY1Axis().setGridVisible(true);
    chart.getY1Axis().setSubGridVisible(true);
    // This is necessary or the axis goes from -1 to 2
    chart.getY1Axis().setAutoScale(false);
    chart.getY1Axis().setMaximum(1.0);
    chart.getY1Axis().setMinimum(-1.0);

    // Create a simple XY chart
    int nPoints = 100;
    double[] x = new double[nPoints];
    double[] y1 = new double[nPoints];
    double[] y2 = new double[nPoints];
    double del = 2. * Math.PI / (nPoints - 1);
    for(int i = 0; i < nPoints; i++) {
      x[i] = i * del;
      y1[i] = Math.sin(x[i]);
      y2[i] = Math.cos(x[i]);
    }

    JLDataView dataView1 = new JLDataView();
    chart.getY1Axis().addDataView(dataView1);
    dataView1.setData(x, y1);
    dataView1.setColor(new Color(255, 0, 0));
    dataView1.setName("sin");

    JLDataView dataView2 = new JLDataView();
    chart.getY1Axis().addDataView(dataView2);
    dataView2.setData(x, y2);
    dataView2.setColor(new Color(0, 0, 255));
    dataView2.setName("cos");

    panel.add(chart, BorderLayout.CENTER);

    created = true;
  }

}
