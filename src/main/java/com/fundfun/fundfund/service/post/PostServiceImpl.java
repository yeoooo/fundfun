package com.fundfun.fundfund.service.post;

import com.fundfun.fundfund.domain.product.post.Post;
import com.fundfun.fundfund.domain.product.post.StPost;
import com.fundfun.fundfund.domain.vote.Vote;
import com.fundfun.fundfund.dto.post.PostDto;
import com.fundfun.fundfund.repository.post.PostRepository;
import com.fundfun.fundfund.repository.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final VoteRepository voteRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public Post createPost(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
        return postRepository.save(post);
    }

    @Override
    public List<PostDto> selectAll() {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post p : postList){
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setWriteTime(p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Override
    public List<PostDto> selectAll(Pageable pageable) {
        return null;
    }

    @Override
    public int getTotalPages(List<PostDto> postDtoList) {
        return 0;
    }

    @Override
    public PostDto selectPostById(UUID postId) {
        System.out.println("postId = " + postId);
        Post post = postRepository.findById(postId).orElse(null);
        System.out.println("post의 id = " + post.getId());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PostDto result = modelMapper.map(post, PostDto.class);
        result.setWriteTime(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        return result;
    };


    @Override
    public List<PostDto> selectPostByKeyword(String title) {
        List<Post> postList = postRepository.findByTitleContaining(title);
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post p : postList){
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setWriteTime(p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Override
    public List<PostDto> selectPostByStatus(StPost status) {
        List<Post> postList = postRepository.findByStatusPost(status);
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post p : postList){
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setWriteTime(p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Override
    public List<PostDto> selectPostByCategory(String category) {
        List<Post> postList = postRepository.findByCategoryPost(category);
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post p : postList){
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setWriteTime(p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Override
    public void deletePost(UUID postId){
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            throw new RuntimeException("해당 게시물이 존재하지 않습니다.");
        postRepository.delete(post);
    }

    @Override
    public void updatePost(UUID postId, String newTitle, String newContent) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new RuntimeException("해당 게시물이 존재하지 않습니다");
        }

        // 게시물 업데이트 로직을 추가합니다.
        post.setTitle(newTitle);       // 제목 수정
        post.setContentPost(newContent);   // 내용 수정

        postRepository.save(post);
    }



    @Override
    public List<PostDto> getPostsOrderByLikes() {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "likePost"));
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post p : postList){
            PostDto postDto = modelMapper.map(p, PostDto.class);
            postDto.setWriteTime(p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            postDtoList.add(postDto);
        }

        return postDtoList;
    }

    @Override
    public void addLike(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

        post.setLikePost(post.getLikePost() + 1); // 좋아요 수 증가
        postRepository.save(post);

    }

    @Override
    public void updateStatus(PostDto postDto, StPost status) {
        Post post = modelMapper.map(postDto, Post.class);

        post.setStatusPost(status);
        Vote vote = new Vote();
        vote.linkPost(post);
        voteRepository.save(vote);
        post.linkVote(vote);
        postRepository.save(post);
    }


    @Override
    public int getLikeById(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
        return post.getLikePost();
    }
}