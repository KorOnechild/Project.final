package com.project.cafesns.controller;

import com.project.cafesns.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class PostListController {

    private final PostListService postListService;

    //메인페이지 게시글 목록 조회
    @GetMapping("/api/posts/{region}")
    public ResponseEntity<?> getPostListOrderByDesc(@PathVariable String region){
        return postListService.getPostListOrderByDesc(region);
    }


    //마이페이지 게시글 조회
    @GetMapping("/api/user/posts")
    public ResponseEntity<?> getUserPostList(HttpServletRequest request){

        return postListService.getUserPostList(request);
    }

    //카페 상세페이지 리뷰 조회
    @GetMapping("/api/{cafeId}/posts")
    public ResponseEntity<?> getPostListInCafePage(@PathVariable Long cafeId){
        return postListService.getPostListInCafePage(cafeId);
    }

}