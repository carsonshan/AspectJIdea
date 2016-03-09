package aspectj.in.action.ch05.bean;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Vector;

/**
 * ShoppingCart
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午7:49.
 */
public class ShoppingCart {

    private List _items = new Vector();
    static Logger _logger = Logger.getLogger("trace");

    public void addItem(Item item) {
        _logger.info("->ShoppingCart->addItem->Entering");
        _items.add(item);
    }

    public void removeItem(Item item) {
        _logger.info("->ShoppingCart->removeItem->Entering");
        _items.remove(item);
    }

    public void empty() {
        _logger.info("->ShoppingCart->empty->Entering");
        _items.clear();
    }

    public float totalValue() {
        _logger.info("->ShoppingCart->totalValue->Entering");
        // unimplemented... free!
        return 0;
    }
}
