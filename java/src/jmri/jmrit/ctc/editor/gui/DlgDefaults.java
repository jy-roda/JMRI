package jmri.jmrit.ctc.editor.gui;

import jmri.jmrit.ctc.editor.code.AwtWindowProperties;
import jmri.jmrit.ctc.editor.code.CommonSubs;
import jmri.jmrit.ctc.editor.code.ProgramProperties;
import jmri.jmrit.ctc.ctcserialdata.OtherData;

/**
 *
 * @author Gregory J. Bedlek Copyright (C) 2018, 2019
 */
public class DlgDefaults extends javax.swing.JDialog {

    private static final String FORM_PROPERTIES = "DlgDefaults";    // NOI18N
    private final AwtWindowProperties _mAwtWindowProperties;
    private final ProgramProperties _mProgramProperties;
    private final OtherData _mOtherData;
    private boolean _mClosedNormally = false;
    public boolean closedNormally() { return _mClosedNormally; }
    
    private int _mSIDI_CodingTimeInMillisecondsOrig;
    private int _mSIDI_TimeLockingTimeInMillisecondsOrig;
    private int _mSWDI_CodingTimeInMillisecondsOrig;
    private boolean _mTUL_EnabledAtStartupOrig;
    private int _mCodeButtonDelayTimeOrig;
    
    private void initOrig(ProgramProperties programProperties, OtherData otherData) {
        _mSIDI_CodingTimeInMillisecondsOrig = programProperties._mSIDI_CodingTimeInMilliseconds;
        _mSIDI_TimeLockingTimeInMillisecondsOrig = programProperties._mSIDI_TimeLockingTimeInMilliseconds;
        _mSWDI_CodingTimeInMillisecondsOrig = programProperties._mSWDI_CodingTimeInMilliseconds;
        _mTUL_EnabledAtStartupOrig = otherData._mTUL_EnabledAtStartup;
        _mCodeButtonDelayTimeOrig = programProperties._mCodeButtonDelayTime;
    }
    private boolean dataChanged() {
        if (CommonSubs.getIntFromJTextFieldNoThrow(_mSIDI_CodingTimeInMilliseconds) != _mSIDI_CodingTimeInMillisecondsOrig) return true;
        if (CommonSubs.getIntFromJTextFieldNoThrow(_mSIDI_TimeLockingTimeInMilliseconds) != _mSIDI_TimeLockingTimeInMillisecondsOrig) return true;
        if (CommonSubs.getIntFromJTextFieldNoThrow(_mSWDI_CodingTimeInMilliseconds) != _mSWDI_CodingTimeInMillisecondsOrig) return true;
        if (_mTUL_EnabledAtStartupOrig != _mTUL_EnabledAtStartup.isSelected()) return true;
        if (CommonSubs.getIntFromJTextFieldNoThrow(_mCodeButtonDelayTime) != _mCodeButtonDelayTimeOrig) return true;
        return false;
    }
    
    public DlgDefaults(java.awt.Frame parent, boolean modal, AwtWindowProperties awtWindowProperties, ProgramProperties programProperties, OtherData otherData) {
        super(parent, modal);
        initComponents();
        _mAwtWindowProperties = awtWindowProperties;
        _mProgramProperties = programProperties;
        _mOtherData = otherData;
        
        CommonSubs.setMillisecondsEdit(_mSIDI_CodingTimeInMilliseconds);
        CommonSubs.setMillisecondsEdit(_mSWDI_CodingTimeInMilliseconds);
        CommonSubs.setMillisecondsEdit(_mSIDI_TimeLockingTimeInMilliseconds);
        CommonSubs.setMillisecondsEdit(_mCodeButtonDelayTime);
        
        _mSIDI_CodingTimeInMilliseconds.setText(Integer.toString(programProperties._mSIDI_CodingTimeInMilliseconds));
        _mSIDI_TimeLockingTimeInMilliseconds.setText(Integer.toString(programProperties._mSIDI_TimeLockingTimeInMilliseconds));
        _mSWDI_CodingTimeInMilliseconds.setText(Integer.toString(programProperties._mSWDI_CodingTimeInMilliseconds));
        _mTUL_EnabledAtStartup.setSelected(_mOtherData._mTUL_EnabledAtStartup);
        _mCodeButtonDelayTime.setText(Integer.toString(programProperties._mCodeButtonDelayTime));
        initOrig(_mProgramProperties, _mOtherData);
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

        _mSWDI_CodingTimeInMilliseconds = new javax.swing.JFormattedTextField();
        _mSIDI_CodingTimeInMilliseconds = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        _mSIDI_TimeLockingTimeInMilliseconds = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        _mSaveAndClose = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        _mTUL_EnabledAtStartup = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        _mCodeButtonDelayTime = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(Bundle.getMessage("TitleDlgDef"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        _mSWDI_CodingTimeInMilliseconds.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        _mSIDI_CodingTimeInMilliseconds.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel19.setText(Bundle.getMessage("LabelDlgDefSignalTime"));

        _mSIDI_TimeLockingTimeInMilliseconds.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel22.setText(Bundle.getMessage("LabelDlgDefLockTime"));

        jLabel27.setText(Bundle.getMessage("LabelDlgDefSwitchTime"));

        _mSaveAndClose.setText(Bundle.getMessage("ButtonSaveClose"));
        _mSaveAndClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _mSaveAndCloseActionPerformed(evt);
            }
        });

        jLabel18.setText(Bundle.getMessage("InfoDlgDefSignalSep"));

        jLabel17.setText(Bundle.getMessage("InfoDlgDefTimeRange"));

        jLabel23.setText(Bundle.getMessage("InfoDlgDefSwitchSep"));

        jLabel1.setText(Bundle.getMessage("LabelDlgDefLockEnabled"));

        jLabel2.setText(Bundle.getMessage("LabelDlgDefCodeDelay"));

        jLabel3.setText(Bundle.getMessage("InfoDlgDefCodeDelayZero"));

        jLabel4.setText(Bundle.getMessage("InfoDlgDefCodeDelayMore"));

        _mCodeButtonDelayTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel22)
                                            .addComponent(jLabel19)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(_mSIDI_CodingTimeInMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(40, 40, 40)
                                                        .addComponent(jLabel27))
                                                    .addComponent(_mSIDI_TimeLockingTimeInMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_mSWDI_CodingTimeInMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(_mCodeButtonDelayTime, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel4)
                                                            .addComponent(jLabel3)))
                                                    .addComponent(_mTUL_EnabledAtStartup, javax.swing.GroupLayout.Alignment.LEADING)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(133, 133, 133)
                                                .addComponent(jLabel23))))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(243, 243, 243)
                        .addComponent(_mSaveAndClose)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_mSIDI_CodingTimeInMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel27)
                    .addComponent(_mSWDI_CodingTimeInMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_mSIDI_TimeLockingTimeInMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(_mTUL_EnabledAtStartup)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(_mCodeButtonDelayTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(_mSaveAndClose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        _mAwtWindowProperties.saveWindowState(this, FORM_PROPERTIES);
        if (CommonSubs.allowClose(this, dataChanged())) dispose();
    }//GEN-LAST:event_formWindowClosing

    private void _mSaveAndCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__mSaveAndCloseActionPerformed
        _mProgramProperties._mSIDI_CodingTimeInMilliseconds = Integer.parseInt(_mSIDI_CodingTimeInMilliseconds.getText());
        _mProgramProperties._mSIDI_TimeLockingTimeInMilliseconds = Integer.parseInt(_mSIDI_TimeLockingTimeInMilliseconds.getText());
        _mProgramProperties._mSWDI_CodingTimeInMilliseconds = Integer.parseInt(_mSWDI_CodingTimeInMilliseconds.getText());
        _mOtherData._mTUL_EnabledAtStartup = _mTUL_EnabledAtStartup.isSelected();
        _mProgramProperties._mCodeButtonDelayTime = Integer.parseInt(_mCodeButtonDelayTime.getText());
        _mClosedNormally = true;
        _mAwtWindowProperties.saveWindowState(this, FORM_PROPERTIES);
        dispose();
    }//GEN-LAST:event__mSaveAndCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField _mCodeButtonDelayTime;
    private javax.swing.JFormattedTextField _mSIDI_CodingTimeInMilliseconds;
    private javax.swing.JFormattedTextField _mSIDI_TimeLockingTimeInMilliseconds;
    private javax.swing.JFormattedTextField _mSWDI_CodingTimeInMilliseconds;
    private javax.swing.JButton _mSaveAndClose;
    private javax.swing.JCheckBox _mTUL_EnabledAtStartup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
