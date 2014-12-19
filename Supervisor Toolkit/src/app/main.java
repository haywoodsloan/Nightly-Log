/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.alarms.AlarmsEditPanel;
import app.browser.ExtendedWebBrowser;
import app.popup.TabbedPaneMouseAdapter;
import app.options.OptionsEditPanel;
import app.timer.BrowserTimerThread;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sloan
 */
public class main {

    public final static String[] Default = {"[Nightly Log] -B", "https://docs.google.com/forms/d/172-Elqzog2MgLSMe9WvCHkuxHsJAb5IaFJZKq74KxPw/viewform",
        "[Equipment Problem Report]", "https://docs.google.com/forms/d/1X8K1XeWBykPRnnxn5TWaLGUcc68Yn3JiejvpSgwiJTc/viewform",
        "[Incident Report]", "https://docs.google.com/forms/d/1Zy4Hd4FxPlpSAOZMigRfUVywnL78-pBm5HP5E69TasE/viewform",
        "[Textbook Request Form]", "https://docs.google.com/forms/d/1wW0GEoEqkOlpTIPP__2kRSWbD1RskTBo4wtBaO738BM/viewform",
        "[Real-Time Agent] -S", "http://geomantce-cra.rose-hulman.edu:8080/ACEReport/",
        "[Phone Surveys] -t:30", "https://prod11gbss8.rose-hulman.edu/BanSS/rhit_hwhl.P_QuestionPage",
        "[Attendance Page]", "http://askrose.org/askrose-login"
    };
    public static File file;
    public static OptionsEditPanel optionsEdit;
    static ExtendedWebBrowser[] webBrowsers;
    public static JFrame frame;
    public static int addressCount;
    static BufferedImage icon;
    static boolean bears = false;

    public static JComponent createContent(String[] address) {
        webBrowsers = new ExtendedWebBrowser[address.length / 2];
        JTabbedPane webBrowserPane = new JTabbedPane();
        try {
            for (int i = 1; i < address.length; i = i + 2) {
                webBrowsers[(i - 1) / 2] = new ExtendedWebBrowser();
                System.out.println("Navagating to " + address[i]);
                webBrowsers[(i - 1) / 2].navigate(address[i]);
                webBrowsers[(i - 1) / 2].setBarsVisible(false);
                webBrowsers[(i - 1) / 2].setButtonBarVisible(true);
                addTabWithOptions(webBrowserPane, webBrowsers[(i - 1) / 2], address[i - 1]);

            }
        } catch (StringIndexOutOfBoundsException ex1) {
            RepairOptions();
            return createContent(ReadOptions());
        }
        optionsEdit = new OptionsEditPanel(false);
        webBrowserPane.addTab("Options", optionsEdit);
        webBrowserPane.addTab("Alarms", new AlarmsEditPanel());
        webBrowserPane.addMouseListener(new TabbedPaneMouseAdapter(webBrowserPane));
        addressCount = address.length / 2;
        return webBrowserPane;
    }

    /* Standard main method to try that test as a standalone application. */
    public static void main(final String[] args) {
        for (String arg : args) {
            if (arg.equals("don'tfeedthebears")
                    || arg.equals("don'tfeedthebear")
                    || arg.equals("bear")
                    || arg.equals("bears")
                    || arg.equals("dontfeedthebears")
                    || arg.equals("dontfeedthebear")) {
                bears = true;
            }
        }
        NativeInterface.open();
        UIUtils.setPreferredLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Supervisor Reports");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 600);
                frame.setLocationByPlatform(true);
                try {
                    String iconPath;
                    if (bears) {
                        iconPath = "img/bear.png";
                    } else {
                        iconPath = "img/icon.png";
                    }
                    InputStream iconStream = main.class.getResourceAsStream(iconPath);
                    icon = ImageIO.read(iconStream);
                    frame.setIconImage(icon);
                } catch (IOException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.add(createContent(ReadOptions()), BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        String ObjButtons[] = {"Yes", "No"};
                        int PromptResult = JOptionPane.showOptionDialog(frame, "Are you sure you want to exit?", "Supervisor Toolkit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                        if (PromptResult == JOptionPane.YES_OPTION) {
                            for (ExtendedWebBrowser webBrowser : webBrowsers) {
                                webBrowser.executeJavascript("window.onbeforeunload = null");
                            }
                            System.exit(0);
                        }
                    }
                });
                frame.setVisible(true);
            }
        });
        NativeInterface.runEventPump();
    }

    static String[] ReadOptions() {
        ArrayList<String> LineList = new ArrayList();
        String workingLine;
        try {
            System.out.println("Attempting to read options file");
            file = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\SuperToolkit\\Options.txt");
            if (!file.exists() || file.length() == 0) {
                System.out.println("Options file not found attemping to create default");
                if (!file.getParentFile().exists()) {
                    System.out.println("SuperToolkit directory not found attempting to create it");
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                writeOptions(file, Default);
            }
            FileReader read = new FileReader(file);
            BufferedReader bufRead = new BufferedReader(read);
            workingLine = bufRead.readLine();
            if (workingLine == null || workingLine.length() <= 0) {
                RepairOptions();
                return ReadOptions();
            }
            while (workingLine != null) {
                LineList.add(workingLine);
                workingLine = bufRead.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LineList.toArray(new String[1]);
    }

    public static void infoBox(String infoMessage, String location) {
        JOptionPane.showMessageDialog(null, infoMessage, location, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void writeOptions(File file, String[] writeContents) {
        PrintWriter write;
        try {
            write = new PrintWriter(file);
            for (String writeContent : writeContents) {
                write.println(writeContent);
            }
            write.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void RepairOptions() {
        System.out.println("Options file not formatted correctly. Opening repair window");
        infoBox("Options file is not formatted correctly please repair it manually.", "Bad Startup");
        JDialog diag = new JDialog((Window) null);
        diag.setTitle("Options Manual Repair");
        diag.add(new OptionsEditPanel(true), BorderLayout.CENTER);
        diag.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        diag.setSize(900, 600);
        diag.setLocationByPlatform(true);
        diag.setIconImage(icon);
        diag.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        diag.setVisible(true);
        System.out.println("Options revision submitted");
    }

    public static void addTabWithOptions(JTabbedPane webBrowserPane, ExtendedWebBrowser webBrowser, String title) {
        int tabTitleEndPos = title.lastIndexOf("]");
        String[] parsedOptions = title.substring(tabTitleEndPos + 1).trim().split("-", -1);
        String tabTitle = title.substring(1, tabTitleEndPos);
        System.out.println("Constructing web browser in tab: " + tabTitle);
        webBrowser.setName(tabTitle);
        webBrowserPane.add(tabTitle, webBrowser);
        for (String parsedOption : parsedOptions) {
            if (parsedOption.contains("t:")) {
                System.out.println("Adding a timer to " + tabTitle + " from options for " + parsedOption.substring(2).trim() + " minute(s)");
                BrowserTimerThread browserTimer = new BrowserTimerThread(Integer.parseInt(parsedOption.substring(2).trim()), webBrowser);
                webBrowser.addBrowserTimer(browserTimer);
            } else if (parsedOption.contains("B")) {
                System.out.println("Enabling auto backup for " + tabTitle);
                webBrowser.enableBackup();
            }
            else if (parsedOption.contains("S")){
                System.out.println("Enabling status monitor for " + tabTitle);
                webBrowser.enableMonitor();
            }
        }
    }
}
