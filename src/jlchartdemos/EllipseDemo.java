/*
 * Program to 
 * Created on Dec 24, 2006
 * By Kenneth Evans, Jr.
 */

package jlchartdemos;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import utils.RainbowColorScheme;

import demolauncher.JLChartDemo;
import fr.esrf.tangoatk.widget.util.chart.JLAxis;
import fr.esrf.tangoatk.widget.util.chart.JLChart;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;

public class EllipseDemo extends JLChartDemo
{
  private static final double scale = 100;

  public EllipseDemo() {
    super();
    setName("Ellipses");
  }

  public String getInfo() {
    String info = "This is a plot with ellipses.";
    return info;
  }

  public void createPanel() {
    if(created) return;

    // Make a JPanel
    JPanel panel = new JPanel();
    BorderLayout layout = new BorderLayout();
    panel.setLayout(layout);
    setPanel(panel);

    int nSeries = 11;
    int nPoints = 100;
    double R = 10 * scale;
    double alpha = Math.toRadians(30.);
    double beta = Math.toRadians(45.);
    double theta0 = Math.toRadians(1);
    double theta1 = Math.toRadians(2);
    double x0 = .5 * scale;
    double y0 = .5 * scale;
    // Can't use \n in header String
    String title = String.format(
      "R=%.1f alpha=%.1f beta=%.1f theta0=%.1f theta1=%.1f x0=%.1f y0=%.1f",
      R, Math.toDegrees(alpha), Math.toDegrees(beta), Math.toDegrees(theta0),
      Math.toDegrees(theta1), x0, y0);

    chart = new JLChart();
    chart.setHeader(title);
    chart.setHeaderFont(new Font("Dialog", Font.BOLD, 18));

    chart.getXAxis().setName("x");
    chart.getXAxis().setGridVisible(true);
    chart.getXAxis().setSubGridVisible(true);
    chart.getXAxis().setAnnotation(JLAxis.VALUE_ANNO);
    // Without this the outer curve touches the axis
    chart.getXAxis().setAutoScale(false);
    chart.getXAxis().setMaximum(0);
    chart.getXAxis().setMinimum(scale);

    chart.getY1Axis().setName("y");
    chart.getY1Axis().setGridVisible(true);
    chart.getY1Axis().setSubGridVisible(true);
    // Without this the outer curve touches the axis
    chart.getY1Axis().setAutoScale(false);
    chart.getY1Axis().setMaximum(0);
    chart.getY1Axis().setMinimum(scale);

    // Create the arrays
    double delTheta = (theta1 - theta0) / (nSeries - 1);
    double delPhi = 2. * Math.PI / (nPoints - 1);
    double phi, x1, y1, x2, y2;
    double theta, a, b;
    double[][] xArrays = new double[nSeries][nPoints];
    double[][] yArrays = new double[nSeries][nPoints];
    double[] xArray = null;
    double[] yArray = null;
    JLDataView dv = null;
    String legend;
    double fract;
    for(int i = 0; i < nSeries; i++) {
      theta = theta0 + i * delTheta;
      a = R * Math.sin(theta) / Math.cos(theta - beta);
      b = R * Math.tan(theta);
      xArray = xArrays[i];
      yArray = yArrays[i];
      for(int n = 0; n < nPoints; n++) {
        phi = n * delPhi;
        x2 = a * Math.cos(phi);
        y2 = b * Math.sin(phi);
        x1 = x0 + x2 * Math.cos(alpha) - y2 * Math.sin(alpha);
        y1 = y0 + x2 * Math.sin(alpha) + y2 * Math.cos(alpha);
        xArray[n] = x1;
        yArray[n] = y1;
      }
      dv = new JLDataView();
      legend = String.format("%.3f", Math.toDegrees(theta));
      dv.setName(legend);
      dv.setData(xArray, yArray);
      // This is necessary for the function to be multi-valued
      dv.setXDataSorted(false);
      fract = (float)i / (float)(nSeries - 1);
      dv.setColor(RainbowColorScheme.getColor(fract));
      chart.getY1Axis().addDataView(dv);
    }

    panel.add(chart, BorderLayout.CENTER);

    created = true;
  }

}
