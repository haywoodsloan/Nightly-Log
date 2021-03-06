package app.browser;

import app.main;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

//This class is the base web browser. It contains all the code needed to create a basic web browser not specific to the supervisor toolkit.
public class JWebBrowserPanel extends javax.swing.JPanel {

    //A list of all file types that should be downloadable.
    private final String[] fileSuffixes = {"doc", "docx", "rft", "txt", "pdf", "pps",
        "ppt", "pptx", "png", "bmp", "tif", "jpg", "jpeg", "xls", "xlsx", "7z", "rar", "zip"};

    //We use a JFXPanel so we can display a JavaFX element in a Swing element. The toolkit started as Swing before using JavaFX's web browser, so this is a cop-out to rewriting everthing.
    private final JFXPanel fxWebViewPanel = new JFXPanel();
    private WebEngine engine;

    public JWebBrowserPanel() {

        //This latch makes sure that JWebBrowserPanel doesn't return from its constructor until the JavaFX components are done being created.
        CountDownLatch latch = new CountDownLatch(1);

        createScene(latch);
        initComponents();
        webViewContainer.add(fxWebViewPanel);

        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(JWebBrowserPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backButton = new javax.swing.JButton();
        forwardButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        webViewContainer = new javax.swing.JPanel();
        printButton = new javax.swing.JButton();

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/img/backIcon.png"))); // NOI18N
        backButton.setBorder(null);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        forwardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/img/forwardIcon.png"))); // NOI18N
        forwardButton.setBorder(null);
        forwardButton.setContentAreaFilled(false);
        forwardButton.setFocusPainted(false);
        forwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardButtonActionPerformed(evt);
            }
        });

        refreshButton.setForeground(new java.awt.Color(240, 240, 240));
        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/img/refreshIcon.png"))); // NOI18N
        refreshButton.setBorder(null);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setMaximumSize(new java.awt.Dimension(20, 20));
        refreshButton.setMinimumSize(new java.awt.Dimension(20, 20));
        refreshButton.setPreferredSize(new java.awt.Dimension(20, 20));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        webViewContainer.setLayout(new java.awt.BorderLayout());

        printButton.setForeground(new java.awt.Color(240, 240, 240));
        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/img/printIcon.png"))); // NOI18N
        printButton.setBorder(null);
        printButton.setContentAreaFilled(false);
        printButton.setFocusPainted(false);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(webViewContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forwardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 610, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forwardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(webViewContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    //Called when the forward arrow is clicked. Navigates to the next page in the browsing history.
    private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardButtonActionPerformed
        Platform.runLater(() -> {
            try {
                engine.getHistory().go(1);
            } catch (Exception e) {
                Logger.getLogger(JWebBrowserPanel.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }//GEN-LAST:event_forwardButtonActionPerformed

    //Called when the refresh icon is clicked. Reloads the page.
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        Platform.runLater(() -> {
            try {
                engine.reload();
            } catch (Exception e) {
                Logger.getLogger(JWebBrowserPanel.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }//GEN-LAST:event_refreshButtonActionPerformed

    //Called when the back arrow is clicked. Navigates to the previous page. 
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        Platform.runLater(() -> {
            try {
                engine.getHistory().go(-1);
            } catch (Exception e) {
                Logger.getLogger(JWebBrowserPanel.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }//GEN-LAST:event_backButtonActionPerformed

    //Called when the print button is clicked. Opens the print dialog, but using a thread helps prevent the risk of locking the toolkit if the print fails.
    //It seams like the print only works after two print jobs have been started. This may need more work.
    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        new Thread() {

            @Override
            public void run() {
                PrinterJob job = PrinterJob.createPrinterJob();

                if (job.showPrintDialog(null)) {
                    System.out.println("Sending pring job to " + job.getPrinter().getName());

                    engine.print(job);
                    job.endJob();
                }
            }

        }.start();
    }//GEN-LAST:event_printButtonActionPerformed

    //This is used to create all of the JavaFX components because that all must be done in the JavaFX thread. This means most of this code is preformed asynchronously.
    private void createScene(CountDownLatch latch) {

        Platform.runLater(() -> {
            WebView view = new WebView();
            engine = view.getEngine();

            //We must handle links that would creat a popup window. Currently only starting observations and downloading files must behave differently.
            engine.setCreatePopupHandler((PopupFeatures param) -> {

                Stage stage = new Stage();
                WebView popupView = new WebView();

                //This is how we handle obeservations and download differently. We close the popup window if they are a special type.
                popupView.getEngine().locationProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

                    if (newValue.startsWith("conf:sip")) {
                        stage.close();

                    } else if (contains(fileSuffixes, newValue.substring(newValue.lastIndexOf(".") + 1)) && stage.isShowing()) {
                        System.out.println("The browser has detected a file to redirect to the native browser");
                        stage.close();

                        try {
                            Desktop.getDesktop().browse(URI.create(newValue));
                        } catch (IOException ex) {
                            Logger.getLogger(JWebBrowserPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                stage.setScene(new Scene(popupView));
                stage.setTitle("Supervisor Toolkit Popup");
                stage.getIcons().add(SwingFXUtils.toFXImage(main.icon, null));
                stage.show();

                return popupView.getEngine();
            });

            //If the user has choosen to debug this will attatch several listeners to give alerts to the user.
            //Also redirects javascript errors.
            if (main.debug) {

                //These listeners are in place to give alerts should an aspect of the WebEngine fail.
                engine.setOnAlert((WebEvent<String> event) -> {
                    System.out.println("Web browser at location: " + getName()
                            + "\n     Has given the following alert: " + event.getData());
                });

                engine.setOnError((WebErrorEvent event) -> {
                    System.err.println("Web browser at location: " + getName()
                            + "\n     Has given the following error: " + event.getMessage());
                });

                engine.getLoadWorker().exceptionProperty().addListener((ObservableValue<? extends Throwable> observable, Throwable newValue, Throwable oldValue) -> {
                    System.err.println("Web browser at location: " + engine.getLocation()
                            + "\n     Has given the following exception: " + oldValue.getMessage());
                });

                engine.getLoadWorker().workDoneProperty().addListener((ObservableValue<? extends Number> observable, Number newValue, Number oldValue) -> {
                    ConsoleBridge bridge = new ConsoleBridge();
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("java", bridge);

                    //This script redirects JavaScript errors to System.err via ConsoleBridge
                    engine.executeScript("window.onerror = function (msg, url, line) {\n"
                            + "    java.error(msg, url, line);\n"
                            + "}");
                });

                //This command will allow a debug console to be brought up if Ctrl-D is pressed.
                view.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
                    if (event.isControlDown() && event.getCode() == KeyCode.D) {
                        engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
                    }
                });
            }

            fxWebViewPanel.setScene(new Scene(view));
            latch.countDown();
        }
        );
    }

    //Used to load a webpage, first constructs a URL then attempts to read in each line of the url.
    public void loadURL(final String url) {
        Platform.runLater(() -> {
            try {
                engine.load(toURL(url));
            } catch (Exception e) {
                Logger.getLogger(JWebBrowserPanel.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    //This function can be used to confirm in the URL requested to be loaded is correctly formatted. If the URL fails the first time a second guess with http:// prefixing it will occur.
    private String toURL(String url) {
        try {
            return new URL(url).toExternalForm();
        } catch (MalformedURLException ex) {
            return url.contains("http://") ? null : toURL("http://" + url);
        }
    }

    //Just some shortcut code to determine if the given String matches an element of the String[]. Matching here is case insensitive.
    private boolean contains(String[] suffixList, String suffix) {

        for (String suffixItem : suffixList) {
            if (suffixItem.equalsIgnoreCase(suffix)) {
                return true;
            }
        }

        return false;
    }

    //Allows other classes to access the WebEngine of the web browser.
    public WebEngine getEngine() {
        return engine;
    }

    //This class allows us to forward JavaScript errors to System.err
    public class ConsoleBridge {

        public void error(String message, String url, String line) {
            System.err.println("Error from site: " + url
                    + "\n     With message: " + message
                    + "\n     At line: " + line);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton forwardButton;
    private javax.swing.JButton printButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel webViewContainer;
    // End of variables declaration//GEN-END:variables
}
