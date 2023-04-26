package com.cosain.trilo.unit.trip.command.preesentation.api.trip;

import com.cosain.trilo.support.RestControllerTest;
import com.cosain.trilo.trip.command.presentation.trip.TripDeleteController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("여행 삭제 API 테스트")
@WebMvcTest(TripDeleteController.class)
class TripDeleteControllerTest extends RestControllerTest {

    @Test
    @DisplayName("인증된 사용자 요청 -> 미구현 500")
    @WithMockUser
    public void deleteTrip_with_authorizedUser() throws Exception {
        mockMvc.perform(delete("/api/trips/1"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @DisplayName("미인증 사용자 요청 -> 인증 실패 401")
    @WithAnonymousUser
    public void deleteTrip_with_unauthorizedUser() throws Exception {
        mockMvc.perform(delete("/api/trips"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorMessage").exists());
    }

}