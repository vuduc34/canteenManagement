package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.canteen.common.constant;
import project.canteen.entity.auth.account;
import project.canteen.entity.canteen.actionDetail;
import project.canteen.entity.canteen.foodItem;
import project.canteen.entity.canteen.order;
import project.canteen.model.auth.ResponMessage;
import project.canteen.model.canteen.actionDetailResponse;
import project.canteen.repository.auth.accountRepository;
import project.canteen.repository.canteen.actionDetailRepository;
import project.canteen.repository.canteen.orderRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class actionDetailService {
    @Autowired
    private actionDetailRepository actionDetailRepository;
    @Autowired
    private accountRepository accountRepository;
    @Autowired
    private orderRepository orderRepository;

    public ResponMessage findActionDetailByUserName(String username) {
        ResponMessage responMessage = new ResponMessage();
        try{
            account account = accountRepository.findUserByUsername(username);
            if(account == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData("Not found account with username = "+username);
            } else {
                List<actionDetail> actionDetails = actionDetailRepository.findActionDetailByAccountId(account.getId());
                List<actionDetailResponse> actionDetailResponses = new ArrayList<>();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


                actionDetails.forEach(actionDetail -> {
                    actionDetailResponse actionDetailResponse = new actionDetailResponse();
                    // convert to VN time
//                    ZonedDateTime vietnamTime = actionDetail.getTimeCreate().atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
                    actionDetailResponse.setTimeCreate(actionDetail.getTimeCreate().plusHours(7).format(formatter));
                    actionDetailResponse.setAccountStaffName(account.getFullname());


                    order order = orderRepository.findOrderById(actionDetail.getOrder_id());
                    actionDetailResponse.setTotalPrice(order.getTotalPrice());
                    actionDetailResponse.setAccountUserName(order.getAccount().getFullname());
                    actionDetailResponse.setActionName(actionDetail.getAction().getName());
                    actionDetailResponses.add(actionDetailResponse);
                });
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(actionDetailResponses);
            }
        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }
}
