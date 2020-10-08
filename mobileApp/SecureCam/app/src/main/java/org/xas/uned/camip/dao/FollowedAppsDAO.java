package org.xas.uned.camip.dao;

import org.xas.uned.camip.model.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FollowedAppsDAO {

    private static final Set<String> APPS_SET = new HashSet<>();

    public static FollowedAppsDAO followedAppsDAO = null;

    public static FollowedAppsDAO getInstance(){
        if(followedAppsDAO == null){
            followedAppsDAO = new FollowedAppsDAO();
        }
        return followedAppsDAO;
    }

    public void addApp(String app){
        APPS_SET.add(app);
    }

    public List<String> getFollowedApps(){
        return new ArrayList<>(APPS_SET);
    }

}
