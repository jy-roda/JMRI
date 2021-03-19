package jmri.jmrit.beantable;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JTextField;
import jmri.Block;
import jmri.BlockManager;
import jmri.InstanceManager;
import jmri.Section;
import jmri.SectionManager;
import jmri.Transit;
import jmri.TransitSection;
import jmri.util.JUnitUtil;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.jupiter.api.*;
import org.netbeans.jemmy.operators.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class TransitTableActionTest extends AbstractTableActionBase<Transit> {

    @Test
    public void testCTor() {
        Assert.assertNotNull("exists", a);
    }

    @Override
    public String getTableFrameName() {
        return Bundle.getMessage("TitleTransitTable");
    }

    @Override
    @Test
    public void testGetClassDescription() {
        Assert.assertEquals("Transit Table Action class description", "Transit Table", a.getClassDescription());
    }

    /**
     * Check the return value of includeAddButton. The table generated by this
     * action includes an Add Button.
     */
    @Override
    @Test
    public void testIncludeAddButton() {
        Assert.assertTrue("Default include add button", a.includeAddButton());
    }

    @Override
    public String getAddFrameName() {
        return Bundle.getMessage("TitleAddTransit");
    }

    @Test
    @Override
    public void testAddThroughDialog() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assume.assumeTrue(a.includeAddButton());
        a.actionPerformed(null);
        JFrame f = JFrameOperator.waitJFrame(getTableFrameName(), true, true);

        // find the "Add... " button and press it.
        jmri.util.swing.JemmyUtil.pressButton(new JFrameOperator(f), Bundle.getMessage("ButtonAdd"));
        new org.netbeans.jemmy.QueueTool().waitEmpty();
        JFrame f1 = JFrameOperator.waitJFrame(getAddFrameName(), true, true);
        JFrameOperator jf = new JFrameOperator(f1);
        //Enter 1 in the text field labeled "System Name:"
        JLabelOperator jlo = new JLabelOperator(jf, Bundle.getMessage("LabelSystemName"));
        ((JTextField) jlo.getLabelFor()).setText("1");
        //and press create
        jmri.util.swing.JemmyUtil.pressButton(jf, Bundle.getMessage("ButtonCreate"));
        JUnitUtil.dispose(f1);
        JUnitUtil.dispose(f);
    }

    @Test
    @Override
    public void testEditButton() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assume.assumeTrue(a.includeAddButton());
        Transit t1 = InstanceManager.getDefault(jmri.TransitManager.class).createNewTransit("t1", "test transit");
        TransitSection ts1 = new TransitSection(InstanceManager.getDefault(SectionManager.class).getBySystemName("TS1"), 0, 0);
        TransitSection ts2 = new TransitSection(InstanceManager.getDefault(SectionManager.class).getBySystemName("TS2"), 0, 0);
        t1.addTransitSection(ts1);
        t1.addTransitSection(ts2);

        a.actionPerformed(null);
        JFrame f = JFrameOperator.waitJFrame(getTableFrameName(), true, true);
        JFrameOperator jfo = new JFrameOperator(f);

        JTableOperator tbl = new JTableOperator(jfo, 0);
        // find the "Edit" button and press it.  This is in the table body.
        tbl.clickOnCell(0, BeanTableDataModel.NUMCOLUMN);
        JFrame f2 = JFrameOperator.waitJFrame(getEditFrameName(), true, true);
        jmri.util.swing.JemmyUtil.pressButton(new JFrameOperator(f2), Bundle.getMessage("ButtonCancel"));
        JUnitUtil.dispose(f2);
        JUnitUtil.dispose(f);
    }

    @Override
    public String getEditFrameName() {
        return "Edit Transit";
    }

    @Override
    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        helpTarget = "package.jmri.jmrit.beantable.TransitTable";
        InstanceManager.setDefault(jmri.BlockManager.class, new jmri.BlockManager());
        JUnitUtil.initSectionManager();
        a = new TransitTableAction();
        Block b1 = InstanceManager.getDefault(BlockManager.class).provideBlock("IB12");
        Block b2 = InstanceManager.getDefault(BlockManager.class).provideBlock("IB13");
        Section s = InstanceManager.getDefault(SectionManager.class).createNewSection("TS1");
        s.addBlock(b1);
        Section s2 = InstanceManager.getDefault(SectionManager.class).createNewSection("TS2");
        s2.addBlock(b2);
        jmri.util.JUnitAppender.suppressWarnMessage("Block IB12 does not have a user name,may not work correctly in Section IY:AUTO:0001");
        jmri.util.JUnitAppender.suppressWarnMessage("Block IB13 does not have a user name,may not work correctly in Section IY:AUTO:0001");
    }

    @Override
    @AfterEach
    public void tearDown() {
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.deregisterEditorManagerShutdownTask();
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(TransitTableActionTest.class);
}
