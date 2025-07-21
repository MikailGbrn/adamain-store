package com.adamain.store.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasRole('adamain_user')")
    public String grantAccess() {
        return "Access Granted for Adamain User";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('adamain_admin')")
    public String grantAccessAdmin() {
        return "Access Granted for Adamain Admin User";
    }
}
