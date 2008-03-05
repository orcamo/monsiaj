/*      PANDA -- a simple transaction monitor

Copyright (C) 1998-1999 Ogochan.
              2000-2003 Ogochan & JMA (Japan Medical Association).
              2002-2006 OZAWA Sakuro.

This module is part of PANDA.

		PANDA is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY.  No author or distributor accepts responsibility
to anyone for the consequences of using it or for whether it serves
any particular purpose or works at all, unless he says so in writing.
Refer to the GNU General Public License for full details.

		Everyone is granted permission to copy, modify and redistribute
PANDA, but only under the conditions described in the GNU General
Public License.  A copy of this license is supposed to have been given
to you along with PANDA so you can know your rights and
responsibilities.  It should be in a file named COPYING.  Among other
things, the copyright notice and this notice must be preserved on all
copies.
*/

package org.montsuqi.monsia.builders;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import org.montsuqi.monsia.Interface;
import org.montsuqi.monsia.WidgetInfo;

/** <p>A builder to create TextArea widgets.</p>
 * <p>It sets wrapping mode to fit Gtk+'s behavior.</p>
 */
public class TextAreaBuilder extends WidgetBuilder {

	Component buildSelf(Interface xml, Container parent, WidgetInfo info) {
		Component c = super.buildSelf(xml, parent, info);
		JTextArea ta = (JTextArea)c;
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		/*
		if (!(c.getParent() instanceof JScrollPane)) {
			c.getParent.remove
			JScrollPane scroll = new JScrollPane(ta,
							JScrollPane.VERTICAL_SCROLLBAR_NEVER,
							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		*/
		return ta;
	}
}
