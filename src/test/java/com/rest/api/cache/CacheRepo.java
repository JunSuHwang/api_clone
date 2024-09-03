package com.rest.api.cache;

import com.rest.api.entity.board.Post;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheRepo {

    private static final String CACHE_KEY = "CACHE_TEST";

    @Cacheable(value = CACHE_KEY, key = "#p0")
    public Post getPost(long postId) {
        Post post = new Post();
        post.setPostId(postId);
        post.setTitle("title_" + postId);
        post.setAuthor("author_" + postId);
        post.setContent("content_" + postId);
        return post;
    }

    @CachePut(value = CACHE_KEY, key = "#p0.postId")
    public Post updatePost(Post post) {
        return post;
    }

    @Cacheable(value = CACHE_KEY, key = "{#p0, #p1}")
    public Post getPostMultiKey(long postId, String title) {
        Post post = new Post();
        post.setPostId(postId);
        post.setTitle("title_" + postId);
        post.setAuthor("author_" + postId);
        post.setContent("content_" + postId);
        return post;
    }

    @CachePut(value = CACHE_KEY, key = "{#p0.postId, #p0.title}")
    public Post updatePostMultiKey(Post post) {
        return post;
    }

    @CacheEvict(cacheNames = {CACHE_KEY}, allEntries = true)
    public void clearCache() {}

    @Cacheable(value = CACHE_KEY, key = "{#p0}", condition = "#p0 > 10")
    public Post getCondition(long postId) {
        Post post = new Post();
        post.setPostId(postId);
        post.setTitle("title_" + postId);
        post.setAuthor("author_" + postId);
        post.setContent("content_" + postId);
        return post;
    }

    @Cacheable(value = CACHE_KEY, key = "T(com.rest.api.cache.CustomKeyGenerator).create(#p0, #p1)")
    public Post getPostKeyGenerator(long postId, String title) {
        Post post = new Post();
        post.setPostId(postId);
        post.setTitle("title_" + postId);
        post.setAuthor("author_" + postId);
        post.setContent("content_" + postId);
        return post;
    }
}
