package com.n26;

import com.n26.model.BucketLinkedList;
import com.n26.model.InputData;
import com.n26.model.OutputData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by melihgurgah on 19/05/2017.
 */
@RestController
public class Controller {

    @Autowired
    private BucketLinkedList buffer;

    private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @RequestMapping("/statistics")
    public OutputData getStatistics() {
        long threshold = (System.currentTimeMillis() / 1000) - 60;
        return buffer.calculateStats(threshold);
    }


    @RequestMapping(name = "/transactions", method = RequestMethod.POST)
    public String postTransactions(@RequestBody InputData input, HttpServletResponse response) {
        long currentSeconds =  System.currentTimeMillis() / 1000;
        LOGGER.info("currentTime :" + currentSeconds + ", "+input.toString());

        if(input.getAmount() == null|| input.getTimestamp() == null || input.getTimestamp() > currentSeconds
                || currentSeconds - input.getTimestamp() > 60){

            response.setStatus(204);
            return "Invalid data";
        }


        long threshold = (System.currentTimeMillis() / 1000) - 60;
        buffer.insertData( threshold, input );
        response.setStatus(201);
        return "Data saved";

    }

}
