/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.alarms;

import java.awt.KeyboardFocusManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haywoosd
 */
public class AlarmsEditPanel extends javax.swing.JPanel {

    private int entryNumber = 0;
    public static AlarmsAlertThread alertThread;
    File alarmsFile;
    String newLine = System.getProperty("line.separator");

    private void loadAlarms() {
        alarmsFile = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\SuperToolkit\\Alarms.txt");
        try {
            if (!alarmsFile.exists()) {
                System.out.println("Alarms file not found attemping to create default");
                alarmsFile.createNewFile();
            } else {
                FileInputStream input = new FileInputStream(alarmsFile);
                byte[] data = new byte[(int) alarmsFile.length()];
                input.read(data);
                String content = new String(data, "UTF-8");
                String[] splitContent = content.split(newLine, -1);
                for (int i = 0; i < splitContent.length - 1; i += 4) {
                    int hour = Integer.parseInt(splitContent[i]);
                    int minute = Integer.parseInt(splitContent[i + 1]);
                    int period = Integer.parseInt(splitContent[i + 2]);
                    String name = splitContent[i + 3];
                    System.out.println("Adding new alarm from file. " + name);
                    addEntry(hour, minute, period, name);
                }
                input.close();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AlarmsEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlarmsEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AlarmsEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeAlarm(int hour, int minute, int period, String name) {
        try {
            FileWriter write = new FileWriter(alarmsFile, true);
            write.write(Integer.toString(hour) + newLine);
            write.write(Integer.toString(minute) + newLine);
            write.write(Integer.toString(period) + newLine);
            write.write(name + newLine);
            write.close();
        } catch (IOException ex) {
            Logger.getLogger(AlarmsEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addEntry(int hour, int minute, int period, String name) {
        AlarmsAlertThread.timerMinutes.add(minute);
        AlarmsAlertThread.timerNames.add(name);
        if (period < 1) {
            AlarmsAlertThread.timerHours.add(hour);
        } else {
            if (hour == 12) {
                AlarmsAlertThread.timerHours.add(0);
            } else {
                AlarmsAlertThread.timerHours.add(hour + 12);
            }
        }
        AlarmsEntryPanel alarmEntry = new AlarmsEntryPanel(hour, minute, period, name, entryNumber);
        System.out.println("Adding new alarm entry: " + name);
        entryContainerPanel.add(alarmEntry);
        entryContainerPanel.validate();
        entryContainerScrollPane.validate();
        entryNumber++;
        if (alertThread == null || !alertThread.isAlive()) {
            System.out.println("Starting the alarms alert thread");
            alertThread = new AlarmsAlertThread();
            alertThread.start();
        }
    }

    public void removeEntry(int number) {
        System.out.println("Removing alarm entry: " + number);
        AlarmsAlertThread.timerHours.remove(number);
        AlarmsAlertThread.timerMinutes.remove(number);
        AlarmsAlertThread.timerNames.remove(number);
        try {
            FileInputStream input = new FileInputStream(alarmsFile);
            byte[] data = new byte[(int) alarmsFile.length()];
            input.read(data);
            input.close();
            String[] splitContent = new String(data, "UTF-8").split(newLine, -1);
            FileWriter alarmsWriter = new FileWriter(alarmsFile);
            for (int i = 0; i < (splitContent.length - 1) / 4; i++) {
                if (i != number) {
                    alarmsWriter.write(splitContent[i * 4] + newLine);
                    alarmsWriter.write(splitContent[i * 4 + 1] + newLine);
                    alarmsWriter.write(splitContent[i * 4 + 2] + newLine);
                    alarmsWriter.write(splitContent[i * 4 + 3] + newLine);
                }
            }
            if ((splitContent.length - 1) / 4 < 2) {
                System.out.println("Stopping the alarms alert thread");
                AlarmsEditPanel.alertThread.terminate();
            }
            alarmsWriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlarmsEntryPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AlarmsEntryPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AlarmsEntryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        entryContainerPanel.remove(number);
        for (int i = number; i < entryContainerPanel.getComponentCount(); i++) {
            System.out.println("Setting entry " + i + " to be " + i);
            ((AlarmsEntryPanel) entryContainerPanel.getComponent(i)).setEntryNumber(i);
        }
        entryNumber--;
    }

    public AlarmsEditPanel() {
        initComponents();
        loadAlarms();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        hourSelect = new javax.swing.JComboBox();
        minuteSelect = new javax.swing.JComboBox();
        periodSelect = new javax.swing.JComboBox();
        addButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        entryContainerScrollPane = new javax.swing.JScrollPane();
        entryContainerPanel = new javax.swing.JPanel();
        nameSelect = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Alarms");

        hourSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        minuteSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));

        periodSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AM", "PM" }));

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jLabel2.setText(":");

        entryContainerScrollPane.setBorder(null);

        entryContainerPanel.setLayout(new javax.swing.BoxLayout(entryContainerPanel, javax.swing.BoxLayout.PAGE_AXIS));
        entryContainerScrollPane.setViewportView(entryContainerPanel);

        nameSelect.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameSelect.setText("Alarm Name");
        nameSelect.setToolTipText("");
        nameSelect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameSelectFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameSelectFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(entryContainerScrollPane)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(nameSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hourSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minuteSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(periodSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hourSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minuteSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton)
                    .addComponent(jLabel2)
                    .addComponent(nameSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entryContainerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        System.out.println("Manually adding an alarm entry");
        addEntry(hourSelect.getSelectedIndex() + 1, minuteSelect.getSelectedIndex(), periodSelect.getSelectedIndex(), nameSelect.getText());
        writeAlarm(hourSelect.getSelectedIndex() + 1, minuteSelect.getSelectedIndex(), periodSelect.getSelectedIndex(), nameSelect.getText());
        nameSelect.setText("Alarm Name");
    }//GEN-LAST:event_addButtonActionPerformed

    private void nameSelectFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameSelectFocusGained
        System.out.println("Alarm name text field gained focus.");
        if (nameSelect.getText().equals("Alarm Name")) {
            nameSelect.setText("");
        }
    }//GEN-LAST:event_nameSelectFocusGained

    private void nameSelectFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameSelectFocusLost
        System.out.println("Alarm name text field lost focus.");
        if (nameSelect.getText().trim().equals("")) {
            nameSelect.setText("Alarm Name");
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        }
    }//GEN-LAST:event_nameSelectFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel entryContainerPanel;
    private javax.swing.JScrollPane entryContainerScrollPane;
    private javax.swing.JComboBox hourSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox minuteSelect;
    private javax.swing.JTextField nameSelect;
    private javax.swing.JComboBox periodSelect;
    // End of variables declaration//GEN-END:variables
}