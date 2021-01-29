package lk.ijse.dep.pos.business.custom;

import lk.ijse.dep.pos.business.SuperBO;
import lk.ijse.dep.pos.dto.GenAccDTO;

import java.util.List;

public interface GenAccBO extends SuperBO {

    void saveAcc(GenAccDTO genAcc) throws Exception;

    void updateAcc(GenAccDTO genAcc) throws Exception;

    void deleteAcc(String accNo) throws Exception;

    List<GenAccDTO> findAllAcc() throws Exception;

    GenAccDTO findAcc(String accNo) throws Exception;


    List<GenAccDTO> findAccByCate(String categoryCh) throws Exception;
}
