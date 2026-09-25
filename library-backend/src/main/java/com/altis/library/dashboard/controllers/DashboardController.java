package com.altis.library.dashboard.controllers;

import com.altis.library.dashboard.models.dtos.AdminDashboardResponseDTO;
import com.altis.library.dashboard.models.dtos.UserDashboardResponseDTO;
import com.altis.library.dashboard.services.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public UserDashboardResponseDTO getUserDashboard(Authentication authentication) {
        return dashboardService.getUserDashboard(authentication);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminDashboardResponseDTO getAdminDashboard() {
        return dashboardService.getAdminDashboard();
    }
}
