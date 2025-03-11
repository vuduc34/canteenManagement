package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.repository.canteen.cartRepository;

@Service
public class cartService {
    @Autowired
    private cartRepository cartRepository;

    public ResponMessage findByCartId(Long cartId) {
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(cartRepository.findCartById(cartId));
        return responMessage;
    }
}
