package aspectj.in.action.ch05.bean;

import java.util.List;
import java.util.Vector;

/**
 * Inventory
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午7:48.
 */
public class Inventory {

    private List _items = new Vector();

    public void addItem(Item item) {
        _items.add(item);
    }

    public void removeItem(Item item) {
        _items.remove(item);
    }
}
