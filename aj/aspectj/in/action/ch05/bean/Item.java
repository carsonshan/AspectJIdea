package aspectj.in.action.ch05.bean;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Item
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午7:48.
 */
public class Item {

    private String _id;
    private float _price;
    static Logger _logger = Logger.getLogger("trace");

    public Item(String id, float price) {
        _id = id;
        _price = price;
    }

    public String getID() {
        _logger.info("->Item->getID->Entering");
        return _id;
    }

    public float getPrice() {
        _logger.info("->Item->getPrice->Entering");
        return _price;
    }

    public String toString() {
        _logger.info("->Item->toString->Entering");
        return "Item: " + _id;
    }
}
