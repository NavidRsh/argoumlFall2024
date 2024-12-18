/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
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

package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * An Action to add client dependencies to some modelelement.
 *
 * @author Michiel
 */
public class ActionAddSupplierDependencyAction extends
        AbstractActionAddModelElement2 {

    /**
     * The constructor.
     */
    public ActionAddSupplierDependencyAction() {
        super();
        setMultiSelect(true);
    }

    /*
     * Constraint: This code only deals with 1 supplier per dependency!
     * TODO: Do we need more?
     * 
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.List)
     */
    protected void doIt(Collection selected) {
        Set oldSet = new HashSet(getSelected());
        for (Object supplier : oldSet) {
            if (oldSet.contains(supplier)) {
                oldSet.remove(supplier); //to be able to remove dep's later
            } else {
                Model.getCoreFactory().buildDependency(supplier, getTarget());
            }
        }

        Collection toBeDeleted = new ArrayList();
        Collection c =  Model.getFacade().getSupplierDependencies(getTarget());
        for (Object dependency : c) {
            if (oldSet.containsAll(
                    Model.getFacade().getClients(dependency))) {
                toBeDeleted.add(dependency);
            }
        }
        ProjectManager.getManager().getCurrentProject()
                .moveToTrash(toBeDeleted);
    }

}
