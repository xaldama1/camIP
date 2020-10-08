package org.xas.uned.camip.dao;

import android.content.Context;
import android.content.Intent;

import org.xas.uned.camip.model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceTokenDAO {

    private static final Map<String, List<Token>> DEVICE_TOKEN_MAP = new ConcurrentHashMap<>();

    public static DeviceTokenDAO deviceTokenDAO = null;

    public static DeviceTokenDAO getInstance(){
        if(deviceTokenDAO == null){
            deviceTokenDAO = new DeviceTokenDAO();
        }
        return deviceTokenDAO;
    }

    public void addTokens(String device, List<Token> tokens){
        DEVICE_TOKEN_MAP.put(device, tokens);
    }

    public void clear(){
        DEVICE_TOKEN_MAP.clear();
    }

    public void addToken(String device, Token token){
        List<Token> tokens = DEVICE_TOKEN_MAP.get(device);
        if(tokens == null){
            tokens = new ArrayList<>();
            DEVICE_TOKEN_MAP.put(device, tokens);
        }
        tokens.add(token);
    }

    public List<Token> getDeviceTokens(String device){
        return DEVICE_TOKEN_MAP.get(device);
    }

    public Token getDeviceToken(String device, long id){
        List<Token> tokens = DEVICE_TOKEN_MAP.get(device);
        if(tokens != null){
            for(Token token : tokens){
                if(id == token.getId()){
                    return token;
                }
            }
        }
        return null;
    }

    public int getDeviceNum(){
        return DEVICE_TOKEN_MAP.size();
    }

}
