package com.driver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public static List<String> getAllOrderForPartner(String partnerId) {
        return OrderRepository.getAllOrderForPartner(partnerId);
    }

    public static List<String> getAllOrders() {
        return OrderRepository.getAllOrders();
    }

    public static Integer getCountOfUnassignedOrders() {
        return OrderRepository.getAllOrders().size() - OrderRepository.getAssignedOrders().size();
    }

    public static Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<String> orders = OrderRepository.getAllOrderForPartner(partnerId);
        List<Order> orderList = new ArrayList<>();
        for(String id : orders){
            Order order = OrderRepository.getOrderById(id).get();
            if(order.getDeliveryTime() > TimeUtils.convertTime(time)){
                orderList.add(order);
            }
        }
        return orderList.size();
    }

    public static String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders = OrderRepository.getAllOrderForPartner(partnerId);
        int max = 0;
        for(String id : orders){
            int maxTime = OrderRepository.getOrderById(id).get().getDeliveryTime();
            if(maxTime > max){
                max = maxTime;
            }
        }
        return TimeUtils.convertTime(max);
    }

    public static void deletePartnerById(String partnerId) {
        List<String> orders = OrderRepository.getAllOrderForPartner(partnerId);
        OrderRepository.deletePartner(partnerId);
        for(String id : orders){
            OrderRepository.unassignOrders(id);
        }
    }

    public static void deleteOrderById(String orderId) {
        String partner = OrderRepository.getPartnerForOrderid(orderId);
        OrderRepository.deleteOrders(orderId);
        if(Objects.nonNull(partner)) {
            List<String> orders = OrderRepository.getAllOrderForPartner(partner);
            orders.remove(orderId);
        }
    }

    public static void addOrder(Order order){
        OrderRepository.addOrder(order);
    }

    public static void addPartner(String partnerId){
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        OrderRepository.addPartner(partner);
    }
    public Order getOrderById(String orderId) throws RuntimeException{
        Optional<Order> orderOptional = OrderRepository.getOrderById(orderId);
        if(orderOptional.isPresent()){
            return orderOptional.get();
        }
        log.error("Order not found for the id: " + orderId);
        throw new RuntimeException("Order not found with the given id: " + orderId);
    }
    public static DeliveryPartner getPartnerById(String partnerId) throws RuntimeException{
        Optional<DeliveryPartner> partnerOptional = OrderRepository.getPartnerById(partnerId);
        if(partnerOptional.isPresent()){
            return partnerOptional.get();
        }
        log.error("Partner not found for the id: " + partnerId);
        throw new RuntimeException("Partner not found for the given id: " + partnerId);
    }
    public static void addOrderPartnerPair(String orderId , String partnerId) throws RuntimeException{
        Optional<Order> orderOptional = OrderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> partnerOptional = OrderRepository.getPartnerById(partnerId);

        if(orderOptional.isEmpty()){
            log.warn("Order is not present with given id: " + orderId);
            throw new RuntimeException("Order is not present with id: " + orderId);
        }
        if(partnerOptional.isEmpty()){
            log.warn("Partner is not present with given id: "+ partnerId);
            throw new RuntimeException("Partner is not present with id: " + partnerId);
        }

        DeliveryPartner updatePartner = partnerOptional.get();
        updatePartner.setNumberOfOrders(updatePartner.getNumberOfOrders()+1);
        OrderRepository.addPartner(updatePartner);

        OrderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public static Integer getOrderCountForPartner(String partnerId){
        Optional<DeliveryPartner> partnerOptional = OrderRepository.getPartnerById(partnerId);
        if(partnerOptional.isPresent()){
            return partnerOptional.get().getNumberOfOrders();
        }
        return 0;
    }
}
