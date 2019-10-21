package br.sp.gov.fatec.cancerconsultapi.controller;

import br.sp.gov.fatec.cancerconsultapi.Result;
import br.sp.gov.fatec.cancerconsultapi.service.FileManagementService;
import br.sp.gov.fatec.cancerconsultapi.service.TrainedModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Controller
@RestController
@CrossOrigin
@RequestMapping("/api/consult")
public class ConsultController {
    @Autowired
    private FileManagementService fileManagementService;

    @Autowired
    private TrainedModelService trainedModelService;

    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<String> consult(@RequestParam("image") MultipartFile file) {
        try {
            String filename = fileManagementService.saveFile(file.getInputStream());
            List<Result> resultList = trainedModelService.consultTrainedModel(filename);
            return new ResponseEntity(resultList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
