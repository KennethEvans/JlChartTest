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

public class LogAxisDemo extends JLChartDemo
{

  public LogAxisDemo() {
    super();
    setName("Logarithmic Axes");
  }

  public String getInfo() {
    String info = "This is a chart with logarithmic axes.";
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
    chart.getXAxis().setScale(JLAxis.LOG_SCALE);

    chart.getY1Axis().setAutoScale(true);
    chart.getY1Axis().setGridVisible(true);
    chart.getY1Axis().setSubGridVisible(true);
    chart.getY1Axis().setScale(JLAxis.LOG_SCALE);

    // Create a simple XY chart
    int nPoints = 100;
    double[] x = new double[nPoints];
    double[] y1 = new double[nPoints];
    double[] y2 = new double[nPoints];
    double[] y3 = new double[nPoints];
    double del = 10. / (nPoints - 1);
    for(int i = 0; i < nPoints; i++) {
      x[i] = (i + 1) * del;
      y1[i] = Math.exp(x[i]);
      y2[i] = Math.pow(2, x[i]);
      y3[i] = Math.pow(x[i], 2);
    }

    JLDataView dataView1 = new JLDataView();
    chart.getY1Axis().addDataView(dataView1);
    dataView1.setData(x, y1);
    dataView1.setColor(new Color(255, 0, 0));
    dataView1.setName("exp(x)");

    JLDataView dataView2 = new JLDataView();
    chart.getY1Axis().addDataView(dataView2);
    dataView2.setData(x, y2);
    dataView2.setColor(new Color(0, 0, 255));
    dataView2.setName("2^x");

    JLDataView dataView3 = new JLDataView();
    chart.getY1Axis().addDataView(dataView3);
    dataView3.setData(x, y3);
    dataView3.setColor(new Color(0, 127, 0));
    dataView3.setName("x^2");

    panel.add(chart, BorderLayout.CENTER);

    created = true;
  }

}
