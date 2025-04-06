package com.example.demo;

import com.example.demo.services.UrlService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class urlServiceTest {
    private final UrlService urlService = new UrlService();

    @Test
    void testUrlRepository_shouldBuildCorrectUrl() {
        String name = "octocat";
        String expected = "https://api.github.com/users/octocat/repos";
        String actual = urlService.urlRepository(name);

        assertEquals(expected, actual);
    }

    @Test
    void testUrlRepository_withEmptyName_shouldStillReturnValidUrl() {
        String name = "";
        String expected = "https://api.github.com/users//repos";
        String actual = urlService.urlRepository(name);

        assertEquals(expected, actual);
    }

    @Test
    void testUrlBranches_shouldRemoveBranchPlaceholder() {
        String input = "https://api.github.com/repos/octocat/Hello-World/branches{/branch}";
        String expected = "https://api.github.com/repos/octocat/Hello-World/branches";
        String actual = urlService.urlBranches(input);

        assertEquals(expected, actual);
    }

    @Test
    void testUrlBranches_whenNoPlaceholder_shouldReturnUnchanged() {
        String input = "https://api.github.com/repos/octocat/Hello-World/branches";
        String expected = input;
        String actual = urlService.urlBranches(input);

        assertEquals(expected, actual);
    }
}
