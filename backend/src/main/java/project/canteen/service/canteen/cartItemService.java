package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.canteen.common.constant;
import project.canteen.entity.auth.account;
import project.canteen.entity.canteen.cart;
import project.canteen.entity.canteen.cartItem;
import project.canteen.entity.canteen.foodItem;
import project.canteen.model.auth.ResponMessage;
import project.canteen.model.canteen.addCartItemModel;
import project.canteen.model.canteen.cartItemInfo;
import project.canteen.model.canteen.updateCartItemModel;
import project.canteen.repository.auth.accountRepository;
import project.canteen.repository.canteen.cartItemRepository;
import project.canteen.repository.canteen.cartRepository;
import project.canteen.repository.canteen.foodRepository;

import java.util.*;

@Service
public class cartItemService {
    @Autowired
    private cartItemRepository cartItemRepository;
    @Autowired
    private cartRepository cartRepository;
    @Autowired
    private accountRepository accountRepository;
    @Autowired
    private foodRepository foodRepository;

    public ResponMessage addFoodToCart(addCartItemModel addCartItemModel) {
        ResponMessage responMessage = new ResponMessage();
        try{
            account account = accountRepository.findAccountById(addCartItemModel.getAccountId());
            if(account == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found account id = "+addCartItemModel.getAccountId());
            } else {
                cart cart = cartRepository.findCartByAccountId(addCartItemModel.getAccountId());

                if(cart == null) {
                    cart newCart = new cart();
                    newCart.setTotalPrice(0L);
                    newCart.setAccount(account);
                    cartRepository.save(newCart);
                    cart = cartRepository.findCartByAccountId(addCartItemModel.getAccountId());
                }
                foodItem foodItem = foodRepository.findFoodById(addCartItemModel.getFoodId());
                if(foodItem == null) {
                    responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                    responMessage.setMessage(constant.MESSAGE.ERROR);
                    responMessage.setData("Not found account id = "+addCartItemModel.getFoodId());
                } else if(cart.getCartItems().stream().anyMatch(item -> item.getFoodItem().getId() == addCartItemModel.getFoodId())){
                    cartItem cartItemFound = null;
                    for (cartItem item : cart.getCartItems()) {
                        if (item.getFoodItem().getId()==addCartItemModel.getFoodId()) {
                            cartItemFound = item;
                            break; // Dừng vòng lặp ngay khi tìm thấy
                        }
                    }
                    cartItemFound.setQuantity(cartItemFound.getQuantity()+addCartItemModel.getQuantity());
                    cartItemFound.setPrice(cartItemFound.getPrice()+foodItem.getPrice()*addCartItemModel.getQuantity());
                    cart.setTotalPrice(cart.getTotalPrice()+foodItem.getPrice()*addCartItemModel.getQuantity());
                    cartRepository.save(cart);
                    cartItemRepository.save(cartItemFound);
                    responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                    responMessage.setMessage(constant.MESSAGE.SUCCESS);
                    responMessage.setData("Successfully");
                } else {
                    cartItem cartItem = new cartItem();
                    cartItem.setCart(cart);
                    cartItem.setFoodItem(foodItem);
                    cartItem.setQuantity(addCartItemModel.getQuantity());
                    cartItem.setPrice(foodItem.getPrice()*addCartItemModel.getQuantity());
                    cart.setTotalPrice(cart.getTotalPrice()+cartItem.getPrice());
                    cartRepository.save(cart);
                    cartItemRepository.save(cartItem);
                    responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                    responMessage.setMessage(constant.MESSAGE.SUCCESS);
                    responMessage.setData("Successfully");
                }

            }
        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage updateCartItem(updateCartItemModel updateCartItemModel) {
        ResponMessage responMessage = new ResponMessage();
        try{
            cartItem cartItem = cartItemRepository.findCartItemById(updateCartItemModel.getCartItemId());
            if(cartItem == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found cartItem id = "+updateCartItemModel.getCartItemId());
            } else {
                //update total price
                foodItem foodItem = cartItem.getFoodItem();
                cartItem.setQuantity(updateCartItemModel.getQuantity());
                cart cart = cartItem.getCart();
                Long newPrice = foodItem.getPrice()* updateCartItemModel.getQuantity();
                cart.setTotalPrice(cart.getTotalPrice()- cartItem.getPrice() +newPrice);
                cartItem.setPrice(newPrice);
                cartRepository.save(cart);
                cartItemRepository.save(cartItem);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData("Successfully");
            }

        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage deleteCartItem(Long cartItemId) {
        ResponMessage responMessage = new ResponMessage();
        try{
            cartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
            if(cartItem != null){
                cart cart = cartItem.getCart();
                cart.setTotalPrice(cart.getTotalPrice()-cartItem.getPrice());
                cartRepository.save(cart);
                cartItemRepository.delete(cartItem);
            }
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData("Successfully");
        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }
    public ResponMessage getCartInfo(Long cartId) {
        ResponMessage responMessage = new ResponMessage();
        try{
            cart cart = cartRepository.findCartById(cartId);
            if(cart == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found cart id = "+cartId);
            } else {
                //update total price
//                Set<cartItem> cartItems = cart.getCartItems();
                List<cartItem> cartItems = new ArrayList<>(cart.getCartItems());
                Collections.sort(cartItems, Comparator.comparing(cartItem::getId));
                List<cartItemInfo> infoList = new ArrayList<>();
                cartItems.forEach(cartItem -> {
                    cartItemInfo info = new cartItemInfo();
                    foodItem foodItem = cartItem.getFoodItem();
                    info.setId(cartItem.getId());
                    info.setPrice(cartItem.getPrice());
                    info.setQuantity(cartItem.getQuantity());
                    info.setDescription(foodItem.getDescription());
                    info.setFoodId(foodItem.getId());
                    info.setFoodName(foodItem.getName());
                    info.setFoodPrice(foodItem.getPrice());
                    info.setImageUrl(foodItem.getImageUrl());
                    info.setStatus(foodItem.getStatus());
                    infoList.add(info);
                });
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(infoList);
            }

        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }
}
