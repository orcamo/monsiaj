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

import java.awt.AWTEvent;

public class CalendarEvent extends AWTEvent {

	protected static final int PREVIOUS_MONTH = AWTEvent.RESERVED_ID_MAX + 1;
	protected static final int NEXT_MONTH = AWTEvent.RESERVED_ID_MAX + 2;
	protected static final int DAY_SELECTED = AWTEvent.RESERVED_ID_MAX + 3;

	public CalendarEvent(Object source, int id) {
		super(source, id);
	}
}
