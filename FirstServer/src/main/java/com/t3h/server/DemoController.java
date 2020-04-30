package com.t3h.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    private DemoManager demoManager;
    @GetMapping("/api/searchSong")
    public Object searchSong(
          @RequestParam(value = "songName", required = false) String songName,
          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
    ){
        return demoManager.searchSong(songName,currentPage);
    }

    @GetMapping("/api/linkMusic")
    public Object getLinkMusic(
            @RequestParam(value = "linkSong") String linkSong
    ){
        return demoManager.getLinkMusic(linkSong);
    }
}
