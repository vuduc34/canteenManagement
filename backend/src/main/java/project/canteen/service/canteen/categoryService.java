package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import project.canteen.common.constant;
import project.canteen.entity.auth.account;
import project.canteen.model.auth.ResponMessage;
import project.canteen.repository.canteen.categoryRepository;

@Service
public class categoryService {
    @Autowired
    private categoryRepository categoryRepository;

    public ResponMessage findAlll() {
        ResponMessage responMessage = new ResponMessage();
        responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
        responMessage.setMessage(constant.MESSAGE.SUCCESS);
        responMessage.setData(categoryRepository.findAll());
        return responMessage;
    }


}
