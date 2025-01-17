package jmri.jmrit.display.palette;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jmri.InstanceManager;
import jmri.Sensor;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.OPath;
import jmri.jmrit.picker.PickListModel;
import jmri.jmrit.picker.PickPanel;
import jmri.util.swing.JmriJOptionPane;

/**
 * Panel for Occupancy and Error detection.
 */
public class DetectionPanel extends JPanel {

    private final JTextField _occDetectorName = new JTextField(); // can be either a Sensor or OBlock name
    private JFrame _pickFrame;
    private final JButton _openPicklistButton;
    private final JPanel _trainIdPanel;
    private JCheckBox _showTrainName;
    private OBlock _block;
    private final JPanel _blockPathPanel;
    private final JPanel _sensorBlurbPanel;
    private final ItemPanel _parent;
    private ArrayList<JCheckBox> _pathBoxes;
    private JPanel _checkBoxPanel;

    /**
     * Add _blockPathPanel to this ItemPanel.
     * @param parent the indicator track item panel
     */
    public DetectionPanel(ItemPanel parent) {
        super();
        _parent = parent;
        _occDetectorName.addActionListener(e -> checkDetection());
        _occDetectorName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                checkDetection();
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(makeSensorPanel(_occDetectorName, "OccupancySensor", "ToolTipOccupancySensor"));
        _openPicklistButton = new JButton(Bundle.getMessage("OpenPicklist"));
        _openPicklistButton.addActionListener(a -> {
            if (_pickFrame == null) {
                openPickList();
            } else {
                closePickList();
            }
        });
        JPanel p = new JPanel();
        p.add(_openPicklistButton);
        panel.add(p);
        add(panel);

        _trainIdPanel = makeTrainIdPanel();
        add(_trainIdPanel);

        _blockPathPanel = new JPanel();
        _blockPathPanel.setLayout(new BoxLayout(_blockPathPanel, BoxLayout.Y_AXIS));
        p = new JPanel();
        p.add(new JLabel(Bundle.getMessage("SelectPathIcons")));
        _blockPathPanel.add(p);
        _checkBoxPanel = new JPanel();
        _blockPathPanel.add(_checkBoxPanel);
        _blockPathPanel.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        _blockPathPanel.setToolTipText(Bundle.getMessage("ToolTipSelectPathIcons"));
        add(_blockPathPanel);

        _sensorBlurbPanel = new JPanel();
        JPanel blurb = new JPanel();
        blurb.setLayout(new BoxLayout(blurb, BoxLayout.Y_AXIS));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        blurb.add(new JLabel(Bundle.getMessage("DetectorNote")));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        _sensorBlurbPanel.add(blurb);
        add(_sensorBlurbPanel);
    }

    JPanel makeSensorPanel(JTextField field, String text, String toolTip) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(Bundle.getMessage(text));
        panel.add(label);
        java.awt.Dimension dim = field.getPreferredSize();
        dim.width = 500;
        field.setMaximumSize(dim);
        dim.width = 200;
        field.setMinimumSize(dim);
        field.setColumns(20);
        field.setDragEnabled(true);
        field.setTransferHandler(new jmri.util.DnDStringImportHandler());
        label.setToolTipText(Bundle.getMessage(toolTip));
        field.setToolTipText(Bundle.getMessage(toolTip));
        panel.setToolTipText(Bundle.getMessage(toolTip));
        panel.add(field);
        return panel;
    }

    @SuppressWarnings("raw")
    void openPickList() {
        _pickFrame = new JFrame();
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel blurb = new JPanel();
        blurb.setLayout(new BoxLayout(blurb, BoxLayout.Y_AXIS));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        blurb.add(new JLabel(Bundle.getMessage("DragOccupancyName", Bundle.getMessage("OccupancySensor"))));
        blurb.add(new JLabel(Bundle.getMessage("DetectorNote")));
//        blurb.add(new JLabel(Bundle.getMessage("DragErrorName", Bundle.getMessage("ErrorSensor"))));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        JPanel panel = new JPanel();
        panel.add(blurb);
        content.add(panel);
        PickListModel<?>[] models = {PickListModel.oBlockPickModelInstance(),
            PickListModel.sensorPickModelInstance()
        };
        content.add(new PickPanel(models));

        _pickFrame.setContentPane(content);
        _pickFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closePickList();
            }
        });
        _pickFrame.setLocationRelativeTo(this);
        _pickFrame.toFront();
        _pickFrame.setVisible(true);
        _pickFrame.pack();
        _openPicklistButton.setText(Bundle.getMessage("ClosePicklist"));
    }

    void closePickList() {
        _pickFrame.dispose();
        _pickFrame = null;
        _openPicklistButton.setText(Bundle.getMessage("OpenPicklist"));
    }

    private JPanel makeTrainIdPanel() {
        JPanel panel = new JPanel();
        _showTrainName = new JCheckBox(Bundle.getMessage("ShowTrainName"));
        _showTrainName.setToolTipText(Bundle.getMessage("ToolTipShowTrainName"));
        JPanel p = new JPanel();
        p.add(_showTrainName);
        p.setToolTipText(Bundle.getMessage("ToolTipShowTrainName"));
        panel.add(p);
        return panel;
    }

    public void dispose() {
        if (_pickFrame != null) {
            _pickFrame.dispose();
            _pickFrame = null;
        }
    }

    /*
     * **************** Getters & Setters **************************
     */
    public boolean getShowTrainName() {
        return _showTrainName.isSelected();
    }

    public void setShowTrainName(boolean show) {
        _showTrainName.setSelected(show);
    }

    public String getOccSensor() {
        String name = _occDetectorName.getText();
        if (name != null && name.trim().length() > 0) {
            if (InstanceManager.sensorManagerInstance().getSensor(name) != null) {
                return name;
            }
        }
        return null;
    }

    public String getOccBlock() {
        String name = _occDetectorName.getText();
        if (name != null && name.trim().length() > 0) {
            if (InstanceManager.getDefault(jmri.jmrit.logix.OBlockManager.class).getOBlock(name) != null) {
                return name;
            }
        }
        return null;
    }

    /**
     * Name of either Sensor or OBlock for detection
     * @param name detector name
     */
    public void setOccDetector(String name) {
        _occDetectorName.setText(name);
        checkDetection();
    }

    public ArrayList<String> getPaths() {
        ArrayList<String> paths = new ArrayList<>();
        if (_pathBoxes != null) {
            for (JCheckBox pathBox : _pathBoxes) {
                if (pathBox.isSelected()) {
                    // displayed path names are padded to 25 charts
                    paths.add(pathBox.getName().trim());
                }
            }
        }
        return paths;
    }

    public void setPaths(ArrayList<String> iconPath) {
        if (iconPath == null || _block == null) {
            _pathBoxes = null;
            return;
        }
        for (String s : iconPath) {
            for (JCheckBox pathBox : _pathBoxes) {
                // displayed path names are padded to 25 chars
                String name = pathBox.getName().trim();
                if (s.equals(name)) {
                    pathBox.setSelected(true);
                }
            }
        }
    }

    /*
     * ******************************************
     */
    private void checkDetection() {
        String name = _occDetectorName.getText();
        if (name != null && name.trim().length() > 0) {
            OBlock block = InstanceManager.getDefault(jmri.jmrit.logix.OBlockManager.class).getOBlock(name);
            if (block != null) {
                if (block.equals(_block)) {
                    return;
                }
                makePathList(block);
                showPanels(true);
           } else {
                Sensor sensor = InstanceManager.sensorManagerInstance().getSensor(name);
                if (sensor == null) {
                    JmriJOptionPane.showMessageDialog(_parent._frame,
                            Bundle.getMessage("InvalidOccDetector", name),
                            Bundle.getMessage("WarningTitle"), JmriJOptionPane.WARNING_MESSAGE);
                    _occDetectorName.setText(null);
                }
                showPanels(false);
            }
        } else {
            showPanels(false);
        }
    }

    private void showPanels(boolean hasOBlock) {
        _trainIdPanel.setVisible(hasOBlock);
        _blockPathPanel.setVisible(hasOBlock);
        _sensorBlurbPanel.setVisible(!hasOBlock);
        if (!hasOBlock) {
            _block = null;
        }
        invalidate();
        _parent.hideIcons();    // resizes panel properly
    }

    private void makePathList(OBlock block) {
        _blockPathPanel.remove(_checkBoxPanel);
        _checkBoxPanel = new JPanel();
        _checkBoxPanel.setLayout(new BoxLayout(_checkBoxPanel, BoxLayout.Y_AXIS));
        _checkBoxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(java.awt.Color.black),
                Bundle.getMessage("circuitPaths")));
        _checkBoxPanel.add(Box.createHorizontalStrut(100));
        _block = block;
        _pathBoxes = new ArrayList<>();
        _block.getPaths().stream().filter(o -> o instanceof OPath)
                      .map(o -> ((OPath) o).getName()).forEach( name -> {
            if (name.length() < 25) {
                char[] ca = new char[25];
                for (int j = 0; j < name.length(); j++) {
                    ca[j] = name.charAt(j);
                }
                for (int j = name.length(); j < 25; j++) {
                    ca[j] = ' ';
                }
                name = new String(ca);
            }
            JCheckBox box = new JCheckBox(name);
            box.setName(name);
            _pathBoxes.add(box);
            _checkBoxPanel.add(box);
        });
        _blockPathPanel.add(_checkBoxPanel, 1);
    }

}
