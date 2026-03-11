package org.mongodb.springboot.kitchensinkmordernization.services;


import org.mongodb.springboot.kitchensinkmordernization.dto.*;



public interface MemberService {

    MemberResponseDTO getMemberById(Long id);

    PaginatedResponseDto<MemberResponseForAdminDto> getAllUsersPaginated(
            int page,
            int size,
            String sortBy,
            String direction,
            String lastSortValue,
            Long lastId,
            String searchQuery
    );
    void updateUser(String userId, UpdateMemberRequestDto updateDto);

}
