package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.model.streamingservice.StreamingService;
import com.streamingservicebackend.model.streamingservice.StreamingServiceDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/service")
public class StreamingServiceController {

    private final StreamingServiceDAO dao = new StreamingServiceDAO(new DatabaseHandler());

    @GetMapping
    @ResponseBody
    public StreamingService getService(@RequestParam String id) {
        return dao.get(id).orElse(null);
    }
}
