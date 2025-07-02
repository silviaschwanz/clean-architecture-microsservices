package com.curso.account.infra.controller;

import com.curso.account.application.GetAccount;
import com.curso.account.application.Signup;
import com.curso.account.application.dto.ErrorOutput;
import com.curso.account.application.dto.SignupInput;
import com.curso.account.application.dto.SignupOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class AccountController {

    @Autowired
    Signup signup;

    @Autowired
    GetAccount getAccount;


    @PostMapping()
    public ResponseEntity<?> signup(@RequestBody SignupInput signupInput) {
        try {
            SignupOutput response = signup.execute (signupInput);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorOutput(e.getMessage()));
        }

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable String accountId) {
        try {
            var accountOutput = getAccount.execute(accountId);
            return ResponseEntity.ok().body(accountOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOutput(e.getMessage()));
        }

    }

}


