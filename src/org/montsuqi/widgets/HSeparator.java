/*      PANDA -- a simple transaction monitor

Copyright (C) 1998-1999 Ogochan.
              2000-2003 Ogochan & JMA (Japan Medical Association).

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

package org.montsuqi.widgets;

import java.awt.Rectangle;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;

public class HSeparator extends JSeparator {
	public HSeparator() {
		super(SwingConstants.HORIZONTAL);
	}

	public void setBounds(int x, int y, int width, int height) {
		// displacement to fit Gtk+'s way of placeing separators
		y += height / 2;
		super.setBounds(x, y, width, height);
	}

	public void setBounds(Rectangle r) {
		// displacement to fit Gtk+'s way of placeing separators
		r.y += r.height / 2;
		super.setBounds(r);
	}
}
