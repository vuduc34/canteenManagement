package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.canteen.common.constant;
import project.canteen.config.jwtConfig.jwtProvider;
import project.canteen.entity.auth.account;
import project.canteen.entity.canteen.*;
import project.canteen.model.auth.ResponMessage;
import project.canteen.model.canteen.addCartItemModel;
import project.canteen.model.canteen.cartItemInfo;
import project.canteen.model.canteen.createOrderModel;
import project.canteen.model.canteen.orderResponse;
import project.canteen.repository.auth.accountRepository;
import project.canteen.repository.canteen.*;
import project.canteen.service.webSocket.webSocketService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class orderService {
    @Autowired
    private orderRepository orderRepository;
    @Autowired
    private orderItemRepository orderItemRepository;
    @Autowired
    private accountRepository accountRepository;
    @Autowired
    private cartRepository cartRepository;
    @Autowired
    private cartItemRepository cartItemRepository;
    @Autowired
    private webSocketService webSocketService;
    @Autowired
    private jwtProvider jwtProvider;
    @Autowired
    private actionRepository actionRepository;
    @Autowired
    private actionDetailRepository actionDetailRepository;

    public ResponMessage createOrder(createOrderModel createOrderModel) {
        ResponMessage responMessage = new ResponMessage();
        try{
            account account = accountRepository.findAccountById(createOrderModel.getAccount_id());
            cart cart = cartRepository.findCartById(createOrderModel.getCart_id());
            if(account == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found account id = "+createOrderModel.getAccount_id());
            } else if(cart == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found cart id = "+createOrderModel.getCart_id());
            } else if(account.getCart().getId() != createOrderModel.getCart_id()) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("account id = "+createOrderModel.getAccount_id()+" not mapping with cart id = "+createOrderModel.getCart_id());
            } else if(cart.getCartItems().size() == 0) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Cart is empty");
            } else {

                order order = new order();
                order.setTotalPrice(cart.getTotalPrice());
                order.setStatus(constant.STATUS.UN_CONFIRMED);
                order.setAccount(account);
                order.setCode(generateCode());
                orderRepository.save(order);
                order orderSaved = orderRepository.findOrderByCode(order.getCode());
                cart.getCartItems().forEach(cartItem -> {
                    orderItem orderItem = new orderItem();
                    orderItem.setOrder(orderSaved);
                    orderItem.setFoodItem(cartItem.getFoodItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItemRepository.save(orderItem);
                });
                webSocketService.sendNotification("newOrder");
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(orderSaved.toResponse());

                // remove cartItem
                cart.setTotalPrice(0L);
                cartRepository.save(cart);
                Iterator<cartItem> iterator = cart.getCartItems().iterator();
                while (iterator.hasNext()) {
                    cartItem cartItem = iterator.next();
                    cartItemRepository.deleteCartItemById(cartItem.getId());
                }
            }
        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }
    public ResponMessage cancelOrder(Long orderId, String token) {
        ResponMessage responMessage = new ResponMessage();
        try{
            order order = orderRepository.findOrderById(orderId);
            if(order == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found order id = "+orderId);
            } else if(!order.getStatus().equals(constant.STATUS.UN_CONFIRMED)) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Can not cancel order id = "+orderId+ " because status is not equal unconfirmed");
            }
            else {
                String username = jwtProvider.getLoginFormToke(token);
                account account = accountRepository.findUserByUsername(username);
                if(!account.getRoles().iterator().next().getName().equals(constant.ROLE.USER)) {
                    action action = actionRepository.findActionByName(constant.ACTION.CANCEL);
                    actionDetail actionDetail = new actionDetail();
                    actionDetail.setAction(action);
                    actionDetail.setOrder_id(orderId);
                    actionDetail.setAccount_id(account.getId());
                    actionDetailRepository.save(actionDetail);
                }
                order.setStatus(constant.STATUS.CANCEL);
                orderRepository.save(order);
                webSocketService.sendNotificationToUser(order.getAccount().getUsername(),"Đơn hàng của bạn đã bị hủy:"+orderId);
                webSocketService.sendNotification("cancelOrder");
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(order.toResponse());
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getOrderByAccountId(Long accountId) {
        ResponMessage responMessage = new ResponMessage();
        try{
            List<orderResponse> orderResponses = new ArrayList<>();
            orderRepository.findOrderByAccountId(accountId).forEach(order -> {
                orderResponses.add(order.toResponse());
            });
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(orderResponses);

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getAllOrder() {
        ResponMessage responMessage = new ResponMessage();
        try{
            List<orderResponse> orderResponses = new ArrayList<>();
            orderRepository.findAll().forEach(order -> {
                orderResponses.add(order.toResponse());
            });
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(orderResponses);

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }
    public ResponMessage getOrderPreparingOrUnconfirmed() {
        ResponMessage responMessage = new ResponMessage();
        try{
            List<orderResponse> orderResponses = new ArrayList<>();
            orderRepository.findOrderPreparingOrUnconfirmed().forEach(order -> {
                orderResponses.add(order.toResponse());
            });
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(orderResponses);

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getTotalRevenueLastDays(int day) {
        ResponMessage responMessage = new ResponMessage();
        try{
            List<Object[]> results = orderRepository.getTotalRevenueLastDays(day);
            List<Map<String, Object>> statistics = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();

                // Ép kiểu đúng: java.sql.Date -> LocalDate
                LocalDate orderDate = ((java.sql.Date) row[0]).toLocalDate();

                map.put("orderDate", orderDate.toString()); // Chuyển về String "yyyy-MM-dd"
                map.put("totalRevenue", row[1]); // Tổng doanh thu
                statistics.add(map);
            }
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(statistics);

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getTotalRevenueLastMonths(int month) {
        ResponMessage responMessage = new ResponMessage();
        try{
            List<Object[]> results = orderRepository.getTotalRevenueLastMonths(month);
            List<Map<String, Object>> statistics = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("orderMonth", row[0]); // Chuyển về String "yyyy-MM-dd"
                map.put("totalRevenue", row[1]); // Tổng doanh thu
                statistics.add(map);
            }
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(statistics);

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getTotalRevenueToday() {
        ResponMessage responMessage = new ResponMessage();
        try{
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(orderRepository.getTotalRevenueToday());

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getTotalOrdersTodayByStatus(String status) {
        ResponMessage responMessage = new ResponMessage();
        try{
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(orderRepository.getTotalOrdersTodayByStatus(status));

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage getOrderItemByOrderId(Long orderId) {
        ResponMessage responMessage = new ResponMessage();
        try{
            order order = orderRepository.findOrderById(orderId);
            if(order == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found order id = "+orderId);
            } else {
                List<cartItemInfo> orderItemInfo = new ArrayList<>();
                List<orderItem> orderItems = new ArrayList<>( order.getOrderItems());
                Collections.sort(orderItems, Comparator.comparing(orderItem::getId));
                orderItems.forEach(orderItem -> {
                    cartItemInfo info = new cartItemInfo();
                    foodItem foodItem = orderItem.getFoodItem();
                    info.setPrice(orderItem.getPrice());
                    info.setId(orderItem.getId());
                    info.setQuantity(orderItem.getQuantity());
                    info.setDescription(foodItem.getDescription());
                    info.setFoodId(foodItem.getId());
                    info.setFoodName(foodItem.getName());
                    info.setFoodPrice(foodItem.getPrice());
                    info.setImageUrl(foodItem.getImageUrl());
                    info.setStatus(foodItem.getStatus());
                    orderItemInfo.add(info);
                });
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(orderItemInfo);
            }

        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage preparingOrder(Long orderId,String token) {
        ResponMessage responMessage = new ResponMessage();
        try{
            order order = orderRepository.findOrderById(orderId);
            if(order == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found order id = "+orderId);
            } else if(order.getStatus().equals(constant.STATUS.UN_CONFIRMED)) {
                order.setStatus(constant.STATUS.PREPARING);
                orderRepository.save(order);
                String username = jwtProvider.getLoginFormToke(token);
                account account = accountRepository.findUserByUsername(username);
                action action = actionRepository.findActionByName(constant.ACTION.CONFIRM);
                actionDetail actionDetail = new actionDetail();
                actionDetail.setAction(action);
                actionDetail.setOrder_id(orderId);
                actionDetail.setAccount_id(account.getId());
                actionDetailRepository.save(actionDetail);
                webSocketService.sendNotificationToUser(order.getAccount().getUsername(),"preparingOrder");
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(order.toResponse());
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }


    public ResponMessage doneOrder(Long orderId,String token) {
        ResponMessage responMessage = new ResponMessage();
        try{
            order order = orderRepository.findOrderById(orderId);
            if(order == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found order id = "+orderId);
            } else if(order.getStatus().equals(constant.STATUS.PREPARING)) {
                order.setStatus(constant.STATUS.DONE);
                orderRepository.save(order);
                String username = jwtProvider.getLoginFormToke(token);
                account account = accountRepository.findUserByUsername(username);
                action action = actionRepository.findActionByName(constant.ACTION.DONE);
                actionDetail actionDetail = new actionDetail();
                actionDetail.setAction(action);
                actionDetail.setOrder_id(orderId);
                actionDetail.setAccount_id(account.getId());
                actionDetailRepository.save(actionDetail);
                webSocketService.sendNotificationToUser(order.getAccount().getUsername(),"doneOrder");
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(order.toResponse());
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage payOrder(Long orderId,String token) {
        ResponMessage responMessage = new ResponMessage();
        try{
            order order = orderRepository.findOrderById(orderId);
            if(order == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found order id = "+orderId);
            } else if(order.getStatus().equals(constant.STATUS.DONE)) {
                order.setStatus(constant.STATUS.PAID);
                orderRepository.save(order);
                String username = jwtProvider.getLoginFormToke(token);
                account account = accountRepository.findUserByUsername(username);
                action action = actionRepository.findActionByName(constant.ACTION.DONE);
                actionDetail actionDetail = new actionDetail();
                actionDetail.setAction(action);
                actionDetail.setOrder_id(orderId);
                actionDetail.setAccount_id(account.getId());
                actionDetailRepository.save(actionDetail);
                webSocketService.sendNotificationToUser(order.getAccount().getUsername(),"paidOrder");
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(order.toResponse());
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage rejectOrder(Long orderId,String token) {
        ResponMessage responMessage = new ResponMessage();
        try{
            order order = orderRepository.findOrderById(orderId);
            if(order == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found order id = "+orderId);
            } else if(order.getStatus().equals(constant.STATUS.DONE)) {
                order.setStatus(constant.STATUS.REJECTED);
                orderRepository.save(order);
                String username = jwtProvider.getLoginFormToke(token);
                account account = accountRepository.findUserByUsername(username);
                action action = actionRepository.findActionByName(constant.ACTION.REJECTED);
                actionDetail actionDetail = new actionDetail();
                actionDetail.setAction(action);
                actionDetail.setOrder_id(orderId);
                actionDetail.setAccount_id(account.getId());
                actionDetailRepository.save(actionDetail);
                webSocketService.sendNotificationToUser(order.getAccount().getUsername(),"rejectOrder");
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(order.toResponse());
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }







    public String generateCode() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }
}
