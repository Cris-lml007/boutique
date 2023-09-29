/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author metallica
 */
public class md5 {
    public static String getMD5Hash(String text){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array=md.digest(text.getBytes());
            BigInteger number=new BigInteger(1,array);
            String hashText=number.toString(16);
            while(hashText.length()<32){
                hashText="0"+hashText;
            }
            return hashText;   
        }catch(NoSuchAlgorithmException e){
            System.out.println("error MD5: "+e);
            return null;
        }
    }
}
