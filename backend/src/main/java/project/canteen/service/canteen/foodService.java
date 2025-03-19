package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.canteen.common.constant;
import project.canteen.entity.canteen.category;
import project.canteen.entity.canteen.foodItem;
import project.canteen.model.auth.ResponMessage;
import project.canteen.repository.canteen.cartItemRepository;
import project.canteen.repository.canteen.categoryRepository;
import project.canteen.repository.canteen.foodRepository;
import project.canteen.service.auth.imageService;
import project.canteen.service.webSocket.webSocketService;

@Service
public class foodService {
    @Autowired
    private foodRepository foodRepository;
    @Autowired
    private imageService imageService;
    @Autowired
    private categoryRepository categoryRepository;
    @Autowired
    private webSocketService webSocketService;
    @Autowired
    private cartItemRepository cartItemRepository;

    public ResponMessage findAlll() {
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(foodRepository.findAllFood());
        return responMessage;
    }

    public ResponMessage findAllAvailable() {
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(foodRepository.findAllAvailable());
        return responMessage;
    }

    public ResponMessage findFilter(String text) {
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(foodRepository.findFilter(text));
        return responMessage;
    }

    public ResponMessage findAllPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(foodRepository.findAll(pageable));
        return responMessage;
    }

    public ResponMessage findByCategory(Long categoryId) {
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(foodRepository.findByCategory(categoryId,constant.STATUS.AVAILABLE));
        return responMessage;
    }

    //Staff
    public ResponMessage create(foodItem foodItem, Long category_id) {
        ResponMessage responMessage = new ResponMessage();
        try{
            category category = categoryRepository.findCategoryById(category_id);
            foodItem.setCategory(category);
            foodItem.setStatus(constant.STATUS.AVAILABLE);
            foodItem.setId(null);
            if(category == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found category with id = "+category_id);
            } else {
                foodRepository.save(foodItem);
                webSocketService.sendNotification("updateFood");
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

    public ResponMessage update(foodItem foodItem) {
        ResponMessage responMessage = new ResponMessage();
        try{
           foodItem current = foodRepository.findFoodById(foodItem.getId());
           if(current == null) {
               responMessage.setResultCode(constant.RESULT_CODE.ERROR);
               responMessage.setMessage(constant.MESSAGE.ERROR);
               responMessage.setData("Not found food_item with id = "+foodItem.getId());
           } else {
               if(!current.getImageUrl().equals(foodItem.getImageUrl())) {
                   imageService.deleteImage(current.getImageUrl());
                   current.setImageUrl(foodItem.getImageUrl());
               }
               current.setDescription(foodItem.getDescription());
               current.setName(foodItem.getName());
               current.setPrice(foodItem.getPrice());
               if(foodItem.getStatus().equals(constant.STATUS.AVAILABLE)) {
                   current.setStatus(foodItem.getStatus());
               } else {
                   current.setStatus(constant.STATUS.UN_AVAILABLE);
                   cartItemRepository.deleteCartItemByItemId(current.getId());
               }
               foodRepository.save(current);
               webSocketService.sendNotification("updateFood");
               responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
               responMessage.setMessage(constant.MESSAGE.SUCCESS);
               responMessage.setData(current);
           }
        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage delete(Long id) {
        ResponMessage responMessage = new ResponMessage();
        try{
           foodItem foodItem = foodRepository.findFoodById(id);
           if(foodItem != null) {
               foodItem.setStatus(constant.STATUS.DELETEED);
               foodRepository.save(foodItem);
               cartItemRepository.deleteCartItemByItemId(id);
               webSocketService.sendNotification("updateFood");
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
}
