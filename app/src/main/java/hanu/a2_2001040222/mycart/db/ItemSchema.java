package hanu.a2_2001040222.mycart.db;

public class ItemSchema {

    public final class ShoppingCartTable {
        public final static String NAME = "ListItem";

        public final class Columns {
            public static final String ID = "id";

            public static final String PRODUCT_ID = "productId" ;

            public static final String NAME = "name";

            public static final String PRICE = "price";

            public static final String THUMBNAIL = "thumbnail";

            public static final String QUANTITY = "quantity";

        }
    }

}
