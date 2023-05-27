package com.driver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
//@AllArgsConstructor
//@NoArgsConstructor
public class OrderRepository {
    private static HashMap<String , Order> orderMap = new HashMap<>();
    private static HashMap<String , DeliveryPartner> partnerMap = new HashMap<>();
    private static HashMap<String , String> orderPartnerMap = new HashMap<>();
    private static HashMap<String , ArrayList<String>> partnerOrdersMap = new HashMap<>();

    public static void addOrder(Order order){
        orderMap.put(order.getId() , order);
    }
    public static void addPartner(DeliveryPartner partner){
        partnerMap.put(partner.getId(), partner);
    }
    public static Optional<Order> getOrderById(String orderId){
        if(orderMap.containsKey(orderId)){
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }
    public static Optional<DeliveryPartner> getPartnerById(String partnerId){
        if(partnerMap.containsKey(partnerId)){
            return Optional.of(partnerMap.get(partnerId));
        }
        return Optional.empty();
    }
    public static void addOrderPartnerPair(String orderId , String partnerId){
        ArrayList<String> orders = partnerOrdersMap.getOrDefault(partnerId , new ArrayList<>());
        orders.add(orderId);
        partnerOrdersMap.put(partnerId,orders);

        orderPartnerMap.put(orderId,partnerId);
    }

    public static List<String> getAllOrderForPartner(String partnerId) {
        return partnerOrdersMap.get(partnerId);
    }

    public static List<String> getAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public static List<String> getAssignedOrders() {
        return new ArrayList<>(orderPartnerMap.keySet());
    }

    public static void deletePartner(String partnerId) {
        if(partnerMap.containsKey(partnerId)){
            partnerMap.remove(partnerId);
            partnerOrdersMap.remove(partnerId);
        }
    }

    public static void deleteOrders(String id) {
        orderMap.remove(id);
        orderPartnerMap.remove(id);
    }

    public static void unassignOrders(String id) {
        orderPartnerMap.remove(id);
    }

    public static String getPartnerForOrderid(String orderId) {
        return orderPartnerMap.get(orderId);
    }
}
