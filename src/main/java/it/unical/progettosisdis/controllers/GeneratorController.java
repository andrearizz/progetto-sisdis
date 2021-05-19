package it.unical.progettosisdis.controllers;


import it.unical.progettosisdis.payload.generator.request.PwdReq;
import it.unical.progettosisdis.payload.generator.response.PwdRes;
import it.unical.progettosisdis.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/generator")
public class GeneratorController {


    @Autowired
    private Generator generator;

    @PostMapping("/password")
    public ResponseEntity<?> generate(@Valid @RequestBody PwdReq pwdReq) {
        String pwd = generator.passwordGenerator(pwdReq.getLenght(),
                pwdReq.isContainsChar(), pwdReq.isContainsDigit(), pwdReq.isContainsSymbols());

       return ResponseEntity.ok(new PwdRes(pwd));
    }
}
