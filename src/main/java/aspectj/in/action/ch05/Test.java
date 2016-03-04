package aspectj.in.action.ch05;

import aspectj.in.action.ch05.bean.Inventory;
import aspectj.in.action.ch05.bean.Item;
import aspectj.in.action.ch05.bean.ShoppingCart;
import aspectj.in.action.ch05.op.ShoppingCartOperator;
import org.apache.log4j.Logger;

/**
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午7:52.
 */
public class Test {

    static Logger _logger = Logger.getLogger("trace");

    public static void main(String[] args) {
        _logger.info("->Test->main->Entering");

        Inventory inventory = new Inventory();
        Item item1 = new Item("1", 30);
        Item item2 = new Item("2", 31);
        Item item3 = new Item("3", 32);
        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.addItem(item3);
        ShoppingCart sc = new ShoppingCart();
        ShoppingCartOperator.addShoppingCartItem(sc, inventory, item1);
        ShoppingCartOperator.addShoppingCartItem(sc, inventory, item2);
    }
}
