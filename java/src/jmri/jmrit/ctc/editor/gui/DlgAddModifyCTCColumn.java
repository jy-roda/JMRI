package jmri.jmrit.ctc.editor.gui;

import jmri.jmrit.ctc.editor.code.AwtWindowProperties;
import jmri.jmrit.ctc.editor.code.Columns;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Gregory J. Bedlek Copyright (C) 2018, 2019
 */
public class DlgAddModifyCTCColumn extends javax.swing.JDialog {

    private static final String FORM_PROPERTIES = "DlgAddModifyCTCColumn";  // NOI18N
    private final AwtWindowProperties _mAwtWindowProperties;
    private final Columns _mColumns;
    private final boolean _mIsModify;
    public int _mNewSwitchNumber;
    public int _mNewSignalEtcNumber;
    public int _mNewGUIColumnNumber;
    public boolean _mNewGUIGeneratedAtLeastOnceAlready;

    /**
     * Creates new form NewColumn
     */
    private boolean _mClosedNormally = false;
    public boolean closedNormally() { return _mClosedNormally; }

    public DlgAddModifyCTCColumn(java.awt.Frame parent, boolean modal, AwtWindowProperties awtWindowProperties, Columns columns, boolean isModify, int existingSwitchNumber, int existingGUIColumnNumber, boolean existingGUIGeneratedAtLeastOnceAlready) {
        super(parent, modal);
        initComponents();
        _mAwtWindowProperties = awtWindowProperties;
        _mColumns = columns;
        _mIsModify = isModify;
        if (!isModify) {    // Add:
            this.setTitle(Bundle.getMessage("TitleDlgAddModAdd"));  // NOI18N
            _mInfo.setVisible(false);
        } else {    // Modify:
            this.setTitle(Bundle.getMessage("TitleDlgAddModChg"));  // NOI18N
            _mInfo.setVisible(true);
        }
        _mGUIColumnNumber.setModel(new SpinnerNumberModel(existingGUIColumnNumber, 0, 32767, 1));
        _mGUISwitchNumber.setModel(new SpinnerNumberModel(existingSwitchNumber, 1, 32767, 2));
        _mGUIGeneratedAtLeastOnceAlready.setSelected(existingGUIGeneratedAtLeastOnceAlready);
        _mAwtWindowProperties.setWindowState((java.awt.Window)this, FORM_PROPERTIES);
        this.getRootPane().setDefaultButton(_mSaveAndClose);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")  // NOI18N
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        _mSaveAndClose = new javax.swing.JButton();
        _mInfo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        _mGUIColumnNumber = new javax.swing.JSpinner();
        _mGUISwitchNumber = new javax.swing.JSpinner();
        _mGUIGeneratedAtLeastOnceAlready = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText(Bundle.getMessage("LabelDlgAddModSwitch"));

        jLabel2.setText(Bundle.getMessage("InfoDlgAddModSignal"));

        _mSaveAndClose.setText(Bundle.getMessage("ButtonSaveClose"));
        _mSaveAndClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _mSaveAndCloseActionPerformed(evt);
            }
        });

        _mInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        _mInfo.setText(Bundle.getMessage("InfoDlgAddModNote")
        );
        _mInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        _mInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        _mInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setText(Bundle.getMessage("LabelDlgAddModColumn"));

        jLabel5.setText(Bundle.getMessage("InfoDlgAddModGen"));

        _mGUIGeneratedAtLeastOnceAlready.setText(Bundle.getMessage("LabelDlgAddModGen")
        );
        _mGUIGeneratedAtLeastOnceAlready.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _mGUIGeneratedAtLeastOnceAlreadyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(_mInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(_mSaveAndClose, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_mGUIGeneratedAtLeastOnceAlready)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(_mGUIColumnNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5))
                                    .addComponent(_mGUISwitchNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 136, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(_mGUISwitchNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(_mGUIColumnNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_mGUIGeneratedAtLeastOnceAlready)
                .addGap(32, 32, 32)
                .addComponent(_mSaveAndClose)
                .addGap(18, 18, 18)
                .addComponent(_mInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void _mSaveAndCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__mSaveAndCloseActionPerformed
        _mNewSwitchNumber = (int)_mGUISwitchNumber.getValue();
        _mNewGUIColumnNumber = (int)_mGUIColumnNumber.getValue();
        String errorText = _mColumns.checkForDups(_mNewSwitchNumber, _mNewGUIColumnNumber, _mIsModify, _mColumns.getEntrySelectedIndex());
        if (errorText != null) {
            JOptionPane.showMessageDialog(this, errorText, Bundle.getMessage("ErrorDlgAddModDuplicate"), JOptionPane.ERROR_MESSAGE); return; // NOI18N   DO NOT EXIT OUT OF FORM!
        }
        _mNewSignalEtcNumber = _mNewSwitchNumber + 1;
        _mNewGUIGeneratedAtLeastOnceAlready = _mGUIGeneratedAtLeastOnceAlready.isSelected();

        _mClosedNormally = true;
        _mAwtWindowProperties.saveWindowState(this, FORM_PROPERTIES);
        dispose();  // All valid, valid to exit out of form.
    }//GEN-LAST:event__mSaveAndCloseActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        _mAwtWindowProperties.saveWindowState(this, FORM_PROPERTIES);
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void _mGUIGeneratedAtLeastOnceAlreadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__mGUIGeneratedAtLeastOnceAlreadyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__mGUIGeneratedAtLeastOnceAlreadyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner _mGUIColumnNumber;
    private javax.swing.JCheckBox _mGUIGeneratedAtLeastOnceAlready;
    private javax.swing.JSpinner _mGUISwitchNumber;
    private javax.swing.JLabel _mInfo;
    private javax.swing.JButton _mSaveAndClose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}
