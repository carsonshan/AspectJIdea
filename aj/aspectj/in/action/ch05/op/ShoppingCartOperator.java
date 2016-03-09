package aspectj.in.action.ch05.op;

import aspectj.in.action.ch05.bean.Inventory;
import aspectj.in.action.ch05.bean.Item;
import aspectj.in.action.ch05.bean.ShoppingCart;

/**
 * ShoppingCartOperator
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午7:50.
 */
public class ShoppingCartOperator {

    public static void addShoppingCartItem(ShoppingCart sc, Inventory inventory, Item item) {
        inventory.removeItem(item);
        sc.addItem(item);
    }

    public static void removeShoppingCartItem(ShoppingCart sc, Inventory inventory, Item item) {
        sc.removeItem(item);
        inventory.addItem(item);
    }
}
