/*
 * Copyright 1997-1999 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package java.beans.beancontext;

import java.beans.beancontext.BeanContextMembershipEvent;

import java.util.EventListener;

/**
 * <p>
 * Compliant BeanContexts fire events on this interface when the state of
 * the membership of the BeanContext changes.
 * </p>
 *
 * @author      Laurence P. G. Cable
 * @since       1.2
 * @see         java.beans.beancontext.BeanContext
 */

public interface BeanContextMembershipListener extends EventListener {

    /**
     * Called when a child or list of children is added to a
     * <code>BeanContext</code> that this listener is registered with.
     * @param bcme The <code>BeanContextMembershipEvent</code>
     * describing the change that occurred.
     */
    void childrenAdded(BeanContextMembershipEvent bcme);

    /**
     * Called when a child or list of children is removed
     * from a <code>BeanContext</code> that this listener
     * is registered with.
     * @param bcme The <code>BeanContextMembershipEvent</code>
     * describing the change that occurred.
     */
    void childrenRemoved(BeanContextMembershipEvent bcme);
}
