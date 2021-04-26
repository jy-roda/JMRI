package jmri.jmrit.display.layoutEditor.LayoutEditorDialogs;

import javax.annotation.Nonnull;

import jmri.jmrit.display.layoutEditor.LayoutEditor;

/**
 * MVC Editor component for LayoutRHXOver objects.
 *
 * @author Bob Jacobsen  Copyright (c) 2020
 * 
 */
public class LayoutRHXOverEditor extends LayoutXOverEditor {

    /**
     * constructor method.
     * @param layoutEditor main layout editor.
     */
    public LayoutRHXOverEditor(@Nonnull LayoutEditor layoutEditor) {
        super(layoutEditor);
    }

    // These now reflect to code in the base class; eventually this heirarchy will
    // expand and the code will be brought here
    

    // private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LayoutRHXOverEditor.class);
}
