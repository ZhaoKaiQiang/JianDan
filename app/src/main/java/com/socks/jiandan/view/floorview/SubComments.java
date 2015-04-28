/**
 *
 */
package com.socks.jiandan.view.floorview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author JohnnyShieh
 * @ClassName: SubComments
 * @Description:TODO
 * @date Feb 20, 2014		10:03:22 AM
 */
public class SubComments {

	private List<? extends Commentable> list;

	public SubComments(List<? extends Commentable> cmts) {
		if (cmts != null) {
			list = new ArrayList<>(cmts);
		} else {
			list = null;
		}
	}

	public int size() {
		return list == null ? 0 : list.size();
	}

	public int getFloorNum() {
		return list.get(list.size() - 1).getCommentFloorNum();
	}

	public Commentable get(int index) {
		return list.get(index);
	}

	public Iterator<? extends Commentable> iterator() {
		return list == null ? null : list.iterator();
	}
}
