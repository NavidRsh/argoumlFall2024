/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.util.ArgoFrame;

/**
 * Abstract action that is the parent to all add actions that add the
 * modelelements via the UMLAddDialog.
 * 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
@UmlModelMutator
public abstract class AbstractActionAddModelElement2 extends UndoableAction {

    private Object target;
    private boolean multiSelect = true;
    private boolean exclusive = true;

    /**
     * Construct an action to add a model element to some list.
     */
    protected AbstractActionAddModelElement2() {
        super(Translator.localize("menu.popup.add-modelelement"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("menu.popup.add-modelelement"));
    }

    /**
     * Construct a named action to add a model element to some list.
     * @param name name for action
     */
    public AbstractActionAddModelElement2(String name) {
        super(name);
    }

    /**
     * Construct an action to add a model element to some list with the
     * given name and icon.
     * @param name name for action
     * @param icon icon for action
     */
    public AbstractActionAddModelElement2(String name, Icon icon) {
        super(name, icon);
    }
    

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        UMLAddDialog dialog =
            new UMLAddDialog(getChoices(), getSelected(), getDialogTitle(),
                             isMultiSelect(),
                             isExclusive());
        int result = dialog.showDialog(ArgoFrame.getFrame());
        if (result == JOptionPane.OK_OPTION) {
            doIt(dialog.getSelected());
        }
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected List getChoices() {
        List ret = new ArrayList();
        Object model =
            ProjectManager.getManager().getCurrentProject().getModel();
        if (getTarget() != null) {
            Object modelElementType = Model.getMetaTypes().getModelElement();

            ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(model, modelElementType));
            ret.remove(getTarget());
        }
        return ret;
    }


    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected List getSelected() {
        List v = new ArrayList();
        Collection c =  Model.getFacade().getSupplierDependencies(getTarget());
        for (Object supplierDependency : c) {
            v.addAll(Model.getFacade().getClients(supplierDependency));
        }
        return v;
    }

    /**
     * The action that has to be done by ArgoUml after the user clicks ok in the
     * UMLAddDialog.
     * @param selected The choices the user has selected in the UMLAddDialog
     */
    protected abstract void doIt(Collection selected);

    /*
     * @see javax.swing.Action#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return !getChoices().isEmpty();
    }
    
    
    /**
     * Returns the UML model target.
     * @return UML ModelElement
     */
    protected Object getTarget() {
        return target;
    }

    /**
     * Sets the UML model target.
     * @param theTarget The target to set
     */
    public void setTarget(Object theTarget) {
        target = theTarget;
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-supplier-dependency");
    }

    /**
     * Returns the exclusive.
     * @return boolean
     */
    public boolean isExclusive() {
        return exclusive;
    }

    /**
     * Returns the multiSelect.
     * @return boolean
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * Sets the exclusive.
     * @param theExclusive The exclusive to set
     */
    public void setExclusive(boolean theExclusive) {
        exclusive = theExclusive;
    }

    /**
     * Sets the multiSelect.
     * @param theMultiSelect The multiSelect to set
     */
    public void setMultiSelect(boolean theMultiSelect) {
        multiSelect = theMultiSelect;
    }

}
