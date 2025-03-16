package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ShoppingMall {

    interface Promotion {
        BigDecimal getItemDiscountRate();
    }

    static abstract class Product implements Promotion {
        private final String mItemName;
        private final BigDecimal mItemPrice;
        private final double mItemWeight;

        public Product(String itemName, double itemPrice, double itemWeight) {
            mItemName = itemName;
            mItemPrice = BigDecimal.valueOf(itemPrice);
            mItemWeight = itemWeight;
        }

        public String getItemName() {
            return mItemName;
        }

        public BigDecimal getItemPrice() {
            return mItemPrice;
        }

        public double getItemWeight() {
            return mItemWeight;
        }
    }


    static class Grocery extends Product {
        public Grocery(String itemName, double itemPrice, double itemWeight) {
            super(itemName, itemPrice, itemWeight);
        }

        @Override
        public BigDecimal getItemDiscountRate() {
            return BigDecimal.valueOf(2000L);
        }
    }

    static class Beauty extends Product {
        public Beauty(String itemName, double itemPrice, double itemWeight) {
            super(itemName, itemPrice, itemWeight);
        }

        @Override
        public BigDecimal getItemDiscountRate() {
            return BigDecimal.valueOf(10000L);
        }
    }

    static class LargeAppliance extends Product {
        public LargeAppliance(String itemName, double itemPrice, double itemWeight) {
            super(itemName, itemPrice, itemWeight);
        }

        @Override
        public BigDecimal getItemDiscountRate() {
            return BigDecimal.ZERO;
        }
    }


    static class Cart {
        private final ArrayList<Product> mItems;

        public Cart(Product... items) {
            mItems = new ArrayList<Product>();
            for (Product p: items) {
                mItems.add(p);
            }
        }

        public BigDecimal getTotalItemPriceNoDiscount() {
            BigDecimal sum = BigDecimal.ZERO;
            for (Product p : mItems) {
                sum = sum.add(p.getItemPrice());
            }
            return sum;
        }

        public BigDecimal getTotalItemPrice() {
            BigDecimal sum = BigDecimal.ZERO;
            for (Product p : mItems) {
                sum = sum.add(p.getItemPrice()).subtract(p.getItemDiscountRate());
            }
            return sum;
        }

        public double getTotalItemWeight() {
            double totalWeight = 0;
            for (Product p : mItems) {
                totalWeight += p.getItemWeight();
            }
            return totalWeight;
        }

        public BigDecimal calculateDeliveryCharge() {
            BigDecimal sum = BigDecimal.ZERO;
            BigDecimal deliveryPrice = BigDecimal.ZERO;
            double totalWeight = 0;
            for (Product p : mItems) {
                sum = sum.add(p.getItemPrice()).subtract(p.getItemDiscountRate());
                totalWeight += p.getItemWeight();
            }

            if (sum.compareTo(BigDecimal.valueOf(100000L)) >= 0) {
                return deliveryPrice; // ZERO
            }

            if (totalWeight < 3) {
                deliveryPrice = BigDecimal.valueOf(1000L);
            } else if (totalWeight < 10) {
                deliveryPrice = BigDecimal.valueOf(3000L);
            } else {
                deliveryPrice = BigDecimal.valueOf(10000L);
            }

            if (sum.compareTo(BigDecimal.valueOf(30000L)) >= 0) {
                deliveryPrice = deliveryPrice.subtract(BigDecimal.valueOf(1000L));
            }
            return deliveryPrice;
        }
    }

    public static void main(String[] args) {
        Product beauty = new Beauty("beauty", 30000, 2);
        Product grocery = new Grocery("grocery", 20000, 3);
        Product largeAppliance = new LargeAppliance("largeAppliance", 50000, 5);

        Cart cart = new Cart(new Product[] {beauty, grocery, largeAppliance});
        BigDecimal totalDeliveryCharge = cart.calculateDeliveryCharge();
        System.out.println(totalDeliveryCharge);    // 결과: 9000
    }
}