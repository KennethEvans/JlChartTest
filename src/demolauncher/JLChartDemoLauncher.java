package demolauncher;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jlchartdemos.EllipseDemo;
import jlchartdemos.LogAxisDemo;
import jlchartdemos.MemoryUsageDemo;
import jlchartdemos.TrignometricFunctionsDemo;
import utils.Utils;

public class JLChartDemoLauncher extends JFrame
{
  private static final long serialVersionUID = 1L;
  private static final int WIDTH = 600;
  private static final int HEIGHT = WIDTH;
  private JLChartDemo[] demos = {new TrignometricFunctionsDemo(),
    new LogAxisDemo(), new EllipseDemo(), new MemoryUsageDemo()};
  private JLChartDemoLauncher launcher = this;
  private JLChartDemo demo = null;
  private String lastDirectory = null;
  private Container contentPane = this.getContentPane();
  private JPanel listPanel = new JPanel();
  private JPanel displayPanel = new JPanel();
  private JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
    listPanel, displayPanel);
  private JList list = new JList(demos);
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuFile = new JMenu();
  private JMenuItem menuFileResetCurrent = new JMenuItem();
  private JMenuItem menuFileInfo = new JMenuItem();
  private JMenuItem menuFileChartInfo = new JMenuItem();
  private JMenuItem menuFileDebugInfo = new JMenuItem();
  private JMenuItem menuFileCopyToClipboard = new JMenuItem();
  private JMenuItem menuFileSaveAs = new JMenuItem();
  private JMenuItem menuFileExit = new JMenuItem();

  /**
   * VDemoLauncher default constructor. Same as VDemoLauncher(true, null);
   */
  public JLChartDemoLauncher() {
    this(true, null);
  }

  /**
   * VDemoLauncher constructor.
   * 
   * @param addMenuBar Add the menu bar if true.
   * @param contentPane The content pane to use. If it is null, then use the
   *          default one.
   */
  public JLChartDemoLauncher(boolean addMenuBar, Container contentPane) {
    if(contentPane == null)
      contentPane = this.getContentPane();
    else
      this.contentPane = contentPane;
    if(addMenuBar) {
      this.setTitle("JLChart Demos");
      menuInit();
    }
    contentInit();
  }

  /**
   * Initializes the menu bar.
   */
  private void menuInit() {
    // Menu
    this.setJMenuBar(menuBar);
    menuFile.setText("File");

    // File Reset
    menuFileResetCurrent.setText("Reset Current");
    menuFileResetCurrent.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        resetCurrent();
      }
    });
    menuFile.add(menuFileResetCurrent);

    // File Info
    menuFileInfo.setText("Info...");
    menuFileInfo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        info();
      }
    });
    menuFile.add(menuFileInfo);

    // File Display Info
    menuFileChartInfo.setText("Chart Info...");
    menuFileChartInfo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        displayInfo();
      }
    });
    menuFile.add(menuFileChartInfo);

    // File Debug Info
    menuFileDebugInfo.setText("Debug Info...");
    menuFileDebugInfo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        debugInfo();
      }
    });
    menuFile.add(menuFileDebugInfo);

    // File Copy to Clipboard
    menuFileCopyToClipboard.setText("Copy to Clipboard");
    menuFileCopyToClipboard.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        copyToClipboard();
      }
    });
    menuFile.add(menuFileCopyToClipboard);

    // File Save as
    menuFileSaveAs.setText("Save As...");
    menuFileSaveAs.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        saveAs();
      }
    });
    menuFile.add(menuFileSaveAs);

    // File Exit
    menuFileExit.setText("Exit");
    menuFileExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        quit();
      }
    });
    menuFile.add(menuFileExit);

    menuBar.add(menuFile);
  }

  /**
   * Initializes the content pane.
   */
  private void contentInit() {
    // Main split pane
    mainPane.setContinuousLayout(true);
    if(false) {
      mainPane.setOneTouchExpandable(true);
    }

    // List panel
    listPanel.setLayout(new BorderLayout());
    listPanel.add(list, BorderLayout.CENTER);
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent ev) {
        onListValueChanged(ev);
      }
    });

    // Display panel
    displayPanel.setLayout(new BorderLayout());
    displayPanel.setPreferredSize(new Dimension(HEIGHT, WIDTH));

    // Content pane
    contentPane.setLayout(new BorderLayout());
    contentPane.add(mainPane, BorderLayout.CENTER);
  }

  /**
   * Displays info.
   */
  public void info() {
    if(displayPanel == null || demo == null) return;
    Utils.infoMsg(demo.getInfo());
  }

  /**
   * Resets the current demo.
   */
  public void resetCurrent() {
    if(displayPanel == null || demo == null) return;
    demo.setCreated(false);
    onListValueChanged(null);
  }

  /**
   * Displays display info.
   */
  public void displayInfo() {
    if(demo == null) {
      Utils.errMsg("No demo");
      return;
    }
    Utils.infoMsg(demo.getChartInfo());
  }

  /**
   * Displays debug info.
   */
  public void debugInfo() {
    // if(demo == null) {
    // Utils.errMsg("No demo");
    // return;
    // }
    if(false) {
      Container container = getContentPane();
      // Utils.printComponents(container);
      System.out.println("Parents of contentPane");
      for(int i = 0; i < 3; i++) {
        if(container == null) break;
        // Only print the top one that has a null parent
        if(container.getParent() == null) {
          System.out.println(i + " " + container.getClass().getName());
          container.list(System.out, 2);
        }
        container = container.getParent();
      }
    }

    // Print properties
    if(true) {
      String info = "";
      info += "\nProperties:\n";
      try {
        String[] name = {"user.dir", "java.version", "java.vm.version",
          "java.vm.home", "java.ext.dirs", "sun.boot.class.path",
          "sun.boot.library.path",};
        for(int i = 0; i < name.length; i++) {
          String value = System.getProperty(name[i]);
          String line = "  " + name[i] + ": " + value + "\n";
          info += line;
        }
      } catch(Exception ex) {
        info += "Get Property info failed: " + ex.getMessage() + "\n";
      }
      if(true) {
        String fileName = "c:\\scratch\\visad_rcp.temp";
        try {
          PrintWriter out = new PrintWriter(new FileWriter(fileName, true));
          Date now = new Date();
          SimpleDateFormat defaultFormatter = new SimpleDateFormat(
            "MMM dd HH:mm:ss");
          out.print(defaultFormatter.format(now) + "\n");
          out.print(info + "\n");
          out.close();
        } catch(Exception ex) {
          Utils.errMsg("Error writing " + fileName);
        }

      } else {
        System.out.println(info);
      }
    }

    Utils.infoMsg("Debug information was printed to System.out");
  }

  /**
   * Saves the display frame to the clipboard
   */
  public void copyToClipboard() {
    if(displayPanel == null) return;
    Cursor oldCursor = getCursor();
    try {
      launcher.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      Utils.savePanelToClipboard(displayPanel);
    } catch(Exception ex) {
      Utils.excMsg("copy failed:", ex);
    } finally {
      setCursor(oldCursor);
    }
  }

  /**
   * Saves the display panel to a file
   */
  public void saveAs() {
    if(displayPanel == null) return;
    Cursor oldCursor = getCursor();
    try {
      launcher.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      File file = Utils.savePanelToFile(displayPanel, lastDirectory);
      if(file != null) {
        // Save the selected path for next time
        lastDirectory = file.getParentFile().getPath();
      }
    } catch(Exception ex) {
      Utils.excMsg("Saving failed:", ex);
    } finally {
      setCursor(oldCursor);
    }
  }

  /**
   * Quits the application
   */
  public void quit() {
    System.exit(0);
  }

  /**
   * Handler for the list.
   * 
   * @param ev
   */
  private void onListValueChanged(ListSelectionEvent ev) {
    if(ev != null && ev.getValueIsAdjusting()) return;

    Cursor oldCursor = getCursor();
    try {
      launcher.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      demo = (JLChartDemo)list.getSelectedValue();
      displayPanel.removeAll();
      demo.createPanel();
      JPanel panel = demo.getPanel();
      launcher.setTitle(demo.getName());
      if(panel != null) {
        displayPanel.add(panel, BorderLayout.CENTER);
        panel.repaint();
      } else {
        JLabel label = new JLabel();
        label.setText("Cannot create " + demo.getName());
        displayPanel.add(label);
      }

      // Look for a parent that is a Window and call its pack()
      Container parent = contentPane;
      while(parent != null) {
        if(parent instanceof Window) {
          Window window = (Window)parent;
          window.validate();
          break;
        }
        parent = parent.getParent();
      }

      // Trial and error testing
      if(false) {
      }

    } catch(Exception ex) {
      Utils.excMsg("Launching failed:", ex);
    } finally {
      setCursor(oldCursor);
    }
  }

  /**
   * @return Returns the demo.
   */
  public JLChartDemo getDemo() {
    return demo;
  }

  /**
   * @return Returns the displayPanel.
   */
  public JPanel getDisplayPanel() {
    return displayPanel;
  }

  /**
   * Main program.
   * 
   * @param args
   */
  public static void main(String[] args) {
    // List styles

    try {
      // Set window decorations
      JFrame.setDefaultLookAndFeelDecorated(true);

      // Set the native look and feel
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      // Make the job run in the AWT thread
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          JLChartDemoLauncher dl = new JLChartDemoLauncher();
          // Make it exit when the window manager close button is clicked
          // dl.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          // EXIT_ON_CLOSE is necessary to stop any timers (e.g. in MemoryUsage)
          dl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          dl.pack();
          dl.setVisible(true);
          dl.setLocationRelativeTo(null);
        }
      });
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }

}
