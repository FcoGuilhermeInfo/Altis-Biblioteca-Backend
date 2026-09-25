package com.altis.library.dashboard.mappers;

import com.altis.library.dashboard.models.dtos.LastLoanDashboardDTO;
import com.altis.library.dashboard.models.dtos.UserLoanDashboardDTO;
import com.altis.library.loans.models.entities.LoanEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardMapper {

    public List<UserLoanDashboardDTO> toUserLoanDTOs(List<LoanEntity> loans) {
        return loans.stream()
                .map(this::toUserLoanDTO)
                .toList();
    }

    public LastLoanDashboardDTO toLastLoanDTO(LoanEntity loan) {
        if (loan == null) {
            return null;
        }

        return new LastLoanDashboardDTO(
                loan.getBook().getTitle(),
                loan.getUser().getName(),
                loan.getBorrowedAt(),
                loan.getDueDate()
        );
    }

    private UserLoanDashboardDTO toUserLoanDTO(LoanEntity loan) {
        return new UserLoanDashboardDTO(
                loan.getUser().getName(),
                loan.getBook().getTitle(),
                loan.getBorrowedAt(),
                loan.getDueDate(),
                loan.getReturnedAt(),
                loan.getStatus()
        );
    }
}
