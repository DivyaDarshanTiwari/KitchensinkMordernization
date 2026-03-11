package org.mongodb.springboot.kitchensinkmordernization.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.dto.*;
import org.mongodb.springboot.kitchensinkmordernization.services.AdminService;
import org.mongodb.springboot.kitchensinkmordernization.services.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;

    @GetMapping("/members")
    @Operation(summary = "Get details of all the users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})
    })
    public PaginatedResponseDto<MemberResponseForAdminDto> findAllMembers(
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String direction,
                                                      @RequestParam(required = false) String lastSortValue,
                                                      @RequestParam(required = false) Long lastId,
                                                      @RequestParam(required = false) String searchQuery
    ) {
        return memberService.getAllUsersPaginated(page,size,sortBy,direction,lastSortValue,lastId,searchQuery);
    }

    @PostMapping("/member")
    @Operation(summary = "adding a user", description = "Admin creating a new user, in which can also set the role")
    public ResponseEntity<Void> addUserByAdmin(@Valid @RequestBody AdminMemberDto memberDTO) {
        adminService.signUp(memberDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/member")
    public ResponseEntity<MemberResponseForAdminDto> updateMember(@AuthenticationPrincipal String id, @RequestBody @Valid UpdateMemberRequestByAdminDto memberDTO) {
        return ResponseEntity.ok(adminService.updateUserByAdmin(Long.valueOf(id),memberDTO));
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal String adminId , @PathVariable @NotNull Long id) {
        adminService.deleteMember(Long.valueOf(adminId),id);
        return ResponseEntity.ok().build();
    }


}
