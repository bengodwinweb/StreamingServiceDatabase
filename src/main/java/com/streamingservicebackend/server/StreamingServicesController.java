package com.streamingservicebackend.server;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.model.BaseMedia;
import com.streamingservicebackend.model.show.Show;
import com.streamingservicebackend.model.streamingservice.StreamingService;
import com.streamingservicebackend.model.streamingservice.StreamingServiceDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/services")
public class StreamingServicesController {

    private final StreamingServiceDAO dao = new StreamingServiceDAO(new DatabaseHandler());

//    @GetMapping
//    @ResponseBody
//    public List<StreamingService> getAll() {
//        return dao.getAll();
//    }

    @GetMapping
    public String getAll(Model model) {
        List<StreamingService> services = dao.getAll();
        model.addAttribute("services", services);
        return "services";
    }
}
