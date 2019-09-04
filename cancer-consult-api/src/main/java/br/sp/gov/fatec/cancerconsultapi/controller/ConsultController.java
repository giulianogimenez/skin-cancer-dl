package br.sp.gov.fatec.cancerconsultapi.controller;

import br.sp.gov.fatec.cancerconsultapi.service.FileManagementService;
import br.sp.gov.fatec.cancerconsultapi.service.TrainedModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping(name = "/api/consult")
public class ConsultController {
    @Autowired
    private FileManagementService fileManagementService;

    @Autowired
    private TrainedModelService trainedModelService;

    @PostMapping("/")
    public void consult(@RequestParam("file") MultipartFile file) {
        try {
            String filename = fileManagementService.saveFile(file.getInputStream());
            trainedModelService.consultTrainedModel(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
