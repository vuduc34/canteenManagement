package project.canteen.service.canteen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.canteen.common.constant;
import project.canteen.entity.canteen.canteenInfo;
import project.canteen.model.auth.ResponMessage;
import project.canteen.repository.canteen.canteenInfoRepository;

@Service
public class CanteenInfoService {
    @Autowired
    private canteenInfoRepository canteenInfoRepositry;

    public ResponMessage update(canteenInfo canteenInfo) {
        ResponMessage responMessage = new ResponMessage();
        try{
            if(canteenInfoRepositry.findAll().size() == 0) {
                canteenInfoRepositry.save(canteenInfo);
            } else {
                canteenInfo current =  canteenInfoRepositry.findAll().getFirst();
                current.setName(canteenInfo.getName());
                current.setAdress(canteenInfo.getAdress());
                current.setDescription(canteenInfo.getDescription());
                current.setEmail(canteenInfo.getEmail());
                current.setPhone(canteenInfo.getPhone());
                current.setOpenTime(canteenInfo.getOpenTime());
                canteenInfoRepositry.save(current);
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

    public ResponMessage getCanteenInfo() {
        ResponMessage responMessage = new ResponMessage();
        try{
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(canteenInfoRepositry.findAll().getFirst());
        }catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
        }
        return responMessage;
    }



}
